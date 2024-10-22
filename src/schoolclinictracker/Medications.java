package schoolclinictracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Medications {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();
        
    public void medicationConfig(){
        
        int opt;

        do {    
            try {
                System.out.println("\n   + Medications Menu +\n");
                System.out.println("1. View All Medications");
                System.out.println("2. Add New Medication");
                System.out.println("3. Delete Medication");
                System.out.println("4. Edit Medication Information");
                System.out.println("5. Go back..");
                
                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine(); 
                
                boolean emptyTable = conf.isTableEmpty("medications");
                switch (opt) {
                    case 1:
                        if (emptyTable) {
                            System.out.println("Medications Table is Empty.");
                            break;
                        }
                        System.out.println("\n\t+ MEDICINES LIST +");
                        String query = "SELECT * FROM medications";
                        viewMedications(query);
                        break;
                    case 2:
                        System.out.println("\n   + ADDING NEW MEDICINE +\n");
                        addMedication();
                        break;
                    case 3:
                        if (emptyTable) {
                            System.out.println("Medications Table is Empty.");
                            break;
                        }
                        System.out.println("\n   + DELETING A MEDICINE +\n");
                        deleteMedicine();
                        break;
                    case 4:
                        if (emptyTable) {
                            System.out.println("Medications Table is Empty.");
                            break;
                        }
                        System.out.println("\n   + EDITING A MEDICINE +\n");
                        updateMedicine();
                        break;
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
    
    public void viewMedications(String query){
        String[] columnHeaders = {"ID", "Medicine", "Description"};
        String[] columnNames = {"id", "medication_name", "description"};
        int spacing = 37;
        
        conf.viewRecords(query, spacing, columnHeaders, columnNames);
    }
    
    public void addMedication(){
        System.out.println("Enter Medication Details:");
        
        System.out.print("\nMedicine Name: ");
        String name = scan.nextLine();
        
        System.out.print("Description: ");
        String desc = scan.nextLine();
        
        String sql = "INSERT INTO medications (medication_name, description) VALUES (?, ?)";
        conf.addRecord(sql, name, desc);
    }
    
    public void deleteMedicine(){
        
        System.out.print("Medicine ID you want to delete: ");
        int id = scan.nextInt();

        conf.deleteRecord("medications", id);
    }
    
    public void updateMedicine(){

        int id;        
        boolean idExists;
        do{
            System.out.print("Medicine ID you want to delete: ");
            id = scan.nextInt();
            
            idExists = conf.doesIDExist("medications", id);
            if(!idExists){
                System.out.println("Medicine ID Doesn't Exist.\n");
                return;
            }
        }while(!idExists);
        scan.nextLine();
        
        String query = "SELECT * FROM prescriptions WHERE id = " + id;
        viewMedications(query);
        
        System.out.println("Enter New Medicine Details:");

        System.out.print("\nNew Medicine Name: ");
        String name = scan.nextLine();
        
        System.out.print("Description: ");
        String desc = scan.nextLine();
        
        String sql = "UPDATE students SET medication_name = ?, description = ? WHERE id = ?";
        conf.updateRecord(sql, name, desc, id);
    }
}
