package schoolclinictracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import static schoolclinictracker.Config.connectDB;

public class Prescriptions {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();
    
    public void prescriptionsConfig(){
        
        int opt;

        do {    
            try {
                System.out.println("\n\t=== Prescriptions Menu ===\n");
                System.out.println("1. View All Prescriptions");
                System.out.println("2. Add New Prescription");
                System.out.println("3. Delete Prescription");
                System.out.println("4. Update Prescription Information");
                System.out.println("5. Back to Main Menu");
                
                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine(); 

                switch (opt) {
                    case 1:{
                        System.out.println("\n\t\t\t\t\t     + PRESCRIPTIONS LIST +");
                        String query = "SELECT * FROM prescriptions";
                        viewPrescriptions(query);
                        break;
                    }
                    case 2:{
                        System.out.println("\n   + ADDING NEW PRESCRIPTIONS +\n");
                        addPrescription();
                        break;
                    }
                    case 3:{ 
                        System.out.println("\n   + DELETING A PRESCRIPTIONS +\n");
                        deletePrescription();
                        break;
                    }
                    case 4:{ 
                        System.out.println("\n   + EDITING A PRESCRIPTIONS +\n");
                        updatePrescription();
                        break;
                    }  
                    case 5:
                        System.out.println("\nGoing back to Main Menu...");
                        System.out.println("------------------------------------------------------------------");
                        break;
                    default:
                        System.out.println("Invalid Option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
                opt = -1; 
            }
        } while (opt != 5);
    }
    
    public void viewPrescriptions(String query){
        String[] prescriptionsHeaders = {"ID", "Student ID", "Medication ID", "Dosage", "Prescription Date"};
        String[] prescriptionsColumns = {"id", "student_id", "med_id", "dosage", "prescription_date"};
        int spacing = 20;
        
        conf.viewRecords(query, spacing, prescriptionsHeaders, prescriptionsColumns);
    }
    
    public void addPrescription(){
        System.out.println("Enter Prescription Details:");

        System.out.print("\nStudent ID: ");
        int s_id = scan.nextInt();

        System.out.print("Medication ID: ");
        int med_id = scan.nextInt();
        scan.nextLine();

        System.out.print("Dosage: ");
        String dosage = scan.nextLine();

        System.out.print("Prescription Date: ");
        String date = scan.nextLine();

        String sql = "INSERT INTO prescriptions (s_name, s_age, s_gender, s_email) VALUES (?, ?, ?, ?)";       
        conf.addRecord(sql, s_id, med_id, dosage, date);
    }
    
    public void deletePrescription(){
        
        System.out.print("Prescription ID you want to delete: ");
        int id = scan.nextInt();

        conf.deleteRecord("prescriptions", id);
        
    }
    
    public void updatePrescription(){

        System.out.print("Prescription ID you want to Edit: ");
        int id = scan.nextInt();
        scan.nextLine();
        
        String findID = "SELECT * FROM prescriptions WHERE id = " + id;

        try (Connection con = connectDB();
            PreparedStatement findIDpst = con.prepareStatement(findID);
            ResultSet rs = findIDpst.executeQuery();){
            
            if(!rs.next()){
                System.out.println("Prescription with ID " + id + " Doesn't Exist.");
                return;
            }

            System.out.println("\nSelected  Prescription");               
            String query = "SELECT * FROM prescriptions WHERE ID = " + id;
            viewPrescriptions(query);
            
            System.out.println("");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        String[] prescriptionsHeaders = {"Student ID", "Medication ID", "Dosage", "Prescription Date"};
        String[] prescriptionsColumns = {"student_id", "med_id", "dosage", "prescription_date"};
        conf.updateRecord("prescriptions", prescriptionsHeaders, prescriptionsColumns, id);   
    }
}