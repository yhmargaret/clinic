package schoolclinictracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SchoolClinicTracker {
    static Config conf = new Config();
    static Scanner scan = new Scanner(System.in);
    
    static Students student = new Students();
    static Medications meds = new Medications();
    static Prescriptions pres = new Prescriptions();

    public static void main(String[] args) {
        
        int choice;
        do {    
            try {
                System.out.println("\n   + School Clinic Medication Tracker +\n");
                System.out.println("1. Students");
                System.out.println("2. Medications");
                System.out.println("3. Prescriptions");
                System.out.println("4. Generate Reports");
                System.out.println("5. Exit");
                
                System.out.print("\nEnter Option: ");
                choice = scan.nextInt();
                scan.nextLine();
                System.out.println("");

                switch (choice) {
                    case 1:  
                        student.studentConfig();
                        break; 
                    case 2:
                        meds.medicationConfig();
                        break;
                    case 3:
                        pres.prescriptionsConfig();
                        break;
                    case 4:
                        medReport();
                        break;
                    case 5:                      
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid Option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
                choice = -1; 
            }
        } while (choice != 5);  
    }
    
    public static void medReport(){
        System.out.println("\n\t\t\t\t\t\t\t     + STUDENTS LIST +");
        student.viewStudents("SELECT * FROM students");
        
        int s_id;
        do{
            System.out.print("\nStudent ID: ");
            s_id = scan.nextInt();
            if(!conf.doesIDExist("students", s_id)){
                System.out.println("Student ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("students", s_id));
        
        String s_name = conf.getDataFromID("students", s_id, "s_name");
        String age = conf.getDataFromID("students", s_id, "s_age");
        String gender = conf.getDataFromID("students", s_id, "s_gender");
        
        System.out.println("\n=================================================================================================================================================================");
        System.out.printf("%95s\n\n", "+ Student Medication Report +");
        System.out.println("=================================================================================================================================================================");
        
        
        System.out.println("\nStudent ID: " + s_id);
        System.out.println("Name: " + s_name);
        System.out.println("Gender: " + gender);
        System.out.println("Age: " + age);
        
        System.out.println("\nMedication History:");
        
        String sql = "SELECT "
                        + "medications.medication_name, "
                        + "prescriptions.dosage, "
                        + "prescriptions.prescription_date, "
                        + "medications.description "
                    + "FROM "
                         + "prescriptions "
                    + "JOIN "
                        + "students ON prescriptions.student_id = students.id "
                    + "JOIN "
                        + "medications ON prescriptions.med_id = medications.id WHERE students.id = " + s_id;
        
        String[] headers = {"Medication Name", "Dosage", "Prescription Date", "Medication Description"};
        String[] columns = {"medication_name", "dosage", "prescription_date", "description"};
        int spacing = 37;
        
        conf.viewRecords(sql, spacing, headers, columns);
        System.out.println("\n\n=================================================================================================================================================================\n");
    }
}
