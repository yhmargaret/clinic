package schoolclinictracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Prescriptions {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();
    
    public void prescriptionsConfig(){
        
        int opt;

        do {    
            try {
                System.out.println("\nPrescriptions Menu\n");
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
                        String query = "SELECT pres.id, stu.s_name, med.medication_name, pres.dosage, pres.prescription_date "
                                    + "FROM prescriptions pres "
                                    + "JOIN medications med "
                                        + "ON pres.med_id = med.id "
                                    + "JOIN students stu "
                                        + "ON pres.student_id = stu.id";
                        viewPrescriptions(query);
                        break;
                    }
                    case 2:{
                        addPrescription();
                        break;
                    }
                    case 3:{ 
                        if (emptyTable) {
                            System.out.println("Prescriptions Table is Empty.");
                            break;
                        }
                        deletePrescription();
                        break;
                    }
                    case 4:{ 
                        if (emptyTable) {
                            System.out.println("Prescriptions Table is Empty.");
                            break;
                        }
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
        String[] prescriptionsHeaders = {"ID", "Student Name", "Medication Name", "Dosage", "Prescription Date"};
        String[] prescriptionsColumns = {"id", "s_name", "medication_name", "dosage", "prescription_date"};
        int spacing = 20;
        
        conf.viewRecords(query, spacing, prescriptionsHeaders, prescriptionsColumns);
    }
    
    public void addPrescription(){
        Students stu = new Students();
        Medications med = new Medications();
        
        System.out.println("Enter Prescription Details:");
        
        System.out.println("\n\t\t\t\t\t\t\t\t  + STUDENTS LIST +");
        stu.viewStudents("SELECT * FROM students");    
        int s_id;
        do{
            System.out.print("\nStudent ID: ");
            s_id = scan.nextInt();
            if(!conf.doesIDExist("students", s_id)){
                System.out.println("Student ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("students", s_id));
        
        System.out.println("\n\t+ MEDICINES LIST +");
        med.viewMedications("SELECT * FROM medications");
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

        String sql = "INSERT INTO prescriptions (student_id, med_id, dosage, prescription_date) VALUES (?, ?, ?, ?)";       
        conf.addRecord(sql, s_id, med_id, dosage, dateToday());
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
        
        String sql = "UPDATE prescriptions SET student_id = ?, med_id = ?, dosage = ?, prescription_date = ? WHERE id = ?";
        conf.updateRecord(sql, s_id, med_id, dosage, dateToday(), id);   
    }
    
    public String dateToday(){       
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy");       
        return LocalDateTime.now().format(format);
    }
}
