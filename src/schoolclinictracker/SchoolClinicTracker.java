package schoolclinictracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SchoolClinicTracker {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Students student = new Students();
        Medications meds = new Medications();
        Prescriptions pres = new Prescriptions();
        
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

}
