package schoolclinictracker;

import java.util.InputMismatchException;
import java.util.Scanner;

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
                
                boolean emptyTable = conf.isTableEmpty("prescriptions");
                switch (opt) {
                    case 1:{
                        if (emptyTable) {
                            System.out.println("Prescriptions Table is Empty.");
                            break;
                        }
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
                        if (emptyTable) {
                            System.out.println("Prescriptions Table is Empty.");
                            break;
                        }
                        System.out.println("\n   + DELETING A PRESCRIPTIONS +\n");
                        deletePrescription();
                        break;
                    }
                    case 4:{ 
                        if (emptyTable) {
                            System.out.println("Prescriptions Table is Empty.");
                            break;
                        }
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

        int s_id;
        do{
            System.out.print("\nStudent ID: ");
            s_id = scan.nextInt();
            if(!conf.doesIDExist("students", s_id)){
                System.out.println("Patient ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("students", s_id));
        
        int med_id;
        do{
            System.out.print("Medication ID: ");
            med_id = scan.nextInt();
            if(!conf.doesIDExist("medications", med_id)){
                System.out.println("Patient ID Doesn't Exist.\n");
            }
        }while(!conf.doesIDExist("medications", med_id));
        scan.nextLine();

        System.out.print("Dosage: ");
        String dosage = scan.nextLine();

        System.out.print("Prescription Date: ");
        String date = scan.nextLine();

        String sql = "INSERT INTO prescriptions (student_id, med_id, dosage, prescription_date) VALUES (?, ?, ?, ?)";       
        conf.addRecord(sql, s_id, med_id, dosage, date);
    }
    
    public void deletePrescription(){
        
        System.out.print("Prescription ID you want to Edit: ");
        int id = scan.nextInt();

        conf.deleteRecord("prescriptions", id);
        
    }
    
    public void updatePrescription(){
        
        int id;        
        boolean idExists;
        do{
            System.out.print("Student ID you want to delete: ");
            id = scan.nextInt();
            
            idExists = conf.doesIDExist("prescriptions", id);
            if(!idExists){
                System.out.println("Student ID Doesn't Exist.\n");
                return;
            }
        }while(!idExists);
        
        String query = "SELECT * FROM prescriptions WHERE id = " + id;
        viewPrescriptions(query);

        System.out.println("Enter New Prescription Details:");

        int s_id;
        do{
            System.out.print("\nNew Student ID: ");
            s_id = scan.nextInt();
            if(!conf.doesIDExist("students", s_id)){
                System.out.println("Student ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("students", s_id));
        
        int med_id;
        do{
            System.out.print("New Medication ID: ");
            med_id = scan.nextInt();
            if(!conf.doesIDExist("medications", med_id)){
                System.out.println("Student ID Doesn't Exist.\n");
            }
        }while(!conf.doesIDExist("medications", med_id));
        scan.nextLine();

        System.out.print("New Dosage: ");
        String dosage = scan.nextLine();

        System.out.print("New Prescription Date: ");
        String date = scan.nextLine();
        
        String sql = "UPDATE prescriptions SET student_id = ?, med_id = ?, dosage = ?, prescription_date = ? WHERE id = ?";
        conf.updateRecord(sql, s_id, med_id, dosage, date, id);   
    }
}
