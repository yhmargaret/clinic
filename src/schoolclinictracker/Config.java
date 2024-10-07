package schoolclinictracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class Config {
    Scanner scan = new Scanner(System.in);
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver          
            con = DriverManager.getConnection("jdbc:sqlite:clinic.db"); // Establish connection
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        
        return con;
    }
    
    // Dynamic view method to display records from any table
    public void viewRecords(String sqlQuery,int spacing, String[] columnHeaders, String[] columnNames) {
        
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = Config.connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet rs = pstmt.executeQuery()) { 
           
            StringBuilder headerLine = new StringBuilder();
            
            int lineLength = columnHeaders.length * (spacing + 3) + 1;
            
            for (int i = 0; i < lineLength; i++) {
                 headerLine.append("-");
            }
            headerLine.append("\n| ");

            for (String header : columnHeaders) {
                headerLine.append(String.format("%-" + spacing + "s | ", header));
            }
            
            headerLine.append("\n");
            for (int i = 0; i < lineLength; i++) {
                 headerLine.append("-");
            }

            System.out.println(headerLine.toString());

            while (rs.next()) {
                StringBuilder row = new StringBuilder("| ");
                for (String colName : columnNames) {
                    String value = rs.getString(colName);
                    row.append(String.format("%-" + spacing + "s | ", value != null ? value : "")); 
                }
                System.out.println(row.toString());
            }
            for (int i = 0; i < lineLength; i++) {
                 System.out.print("-");
            }
            System.out.println("");
            
        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());  
        }
    }
    
    public void addRecord(String sql, Object... values) {
    try (Connection conn = Config.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
    
    public void updateRecord(String table, String[] columnHeaders, String[] columnNames, int id){
        
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        
        for (int i = 0; i < columnNames.length; i++) {
            sql.append(columnNames[i]);
            sql.append(" = ?");
            if (i < columnNames.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ID = ").append(id);
        
        String findID = "SELECT * FROM " + table + " WHERE ID = " + id;
        
        try (Connection con = connectDB();
            PreparedStatement pst = con.prepareStatement(findID);
            ResultSet rs = pst.executeQuery()) {
            
            ResultSetMetaData metaData = rs.getMetaData(); 
            PreparedStatement updPst = con.prepareStatement(sql.toString());

            for (int i = 0; i < columnNames.length; i++) {
                System.out.print("Enter new " + columnHeaders[i] + ": ");  

                String oldValue = rs.getString(columnNames[i]);
                int dataType = metaData.getColumnType(i + 2);  

                switch (dataType) {
                    case java.sql.Types.INTEGER:       
                        
                        String intValue = scan.nextLine();  
                        
                        if (intValue.equalsIgnoreCase("keep")){
                            intValue = oldValue;
                        }                      
                        updPst.setInt(i + 1, Integer.valueOf(intValue));                     
                        break;
                        
                    case java.sql.Types.REAL:
                        
                        String doubleValue = scan.nextLine();  
                        
                        if (doubleValue.equalsIgnoreCase("keep")){
                            doubleValue = oldValue;
                        }
                        updPst.setDouble(i + 1, Double.valueOf(doubleValue));
                        break;      
                        
                    case java.sql.Types.BOOLEAN:
                        
                        String boolValue = scan.nextLine(); 
                        
                        if (boolValue.equalsIgnoreCase("keep")){
                            boolValue = oldValue;
                        }
                        updPst.setBoolean(i + 1, Boolean.parseBoolean(boolValue));
                        break;      
                        
                    default:
                        
                        String stringValue = scan.nextLine();
                        
                        if (stringValue.equalsIgnoreCase("keep")){
                            stringValue = oldValue;
                        }
                        updPst.setString(i + 1, stringValue);
                        break;
                }
            }
            
            updPst.executeUpdate();
            System.out.println("\nRecord was edited successfully!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
    
    public void deleteRecord(String table, int id){
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        
        try {
            Connection con = connectDB();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            int success = pst.executeUpdate();

            if(success > 0){
                System.out.println("\nRecord Successfully Deleted.");
            }else{
                System.out.println("\nNo Record Found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
