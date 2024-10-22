package schoolclinictracker;

import java.util.InputMismatchException;
import java.util.Scanner;;

public class Students {
    Scanner scan = new Scanner(System.in);
    Config conf = new Config();
        
    public void studentConfig(){
        
        int opt;

        do {    
            try {
                System.out.println("\n   + Student Config +\n");
                System.out.println("1. View All Student");
                System.out.println("2. Add a Student");
                System.out.println("3. Delete a Student");
                System.out.println("4. Edit a Student");
                System.out.println("5. Go back..");
                
                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine(); 
                
                boolean emptyTable = conf.isTableEmpty("students");
                switch (opt) {
                    case 1:{
                        if (emptyTable) {
                            System.out.println("Students Table is Empty.");
                            break;
                        }
                        System.out.println("\n\t\t\t\t\t\t\t\t  + STUDENTS LIST +");
                        String query = "SELECT * FROM students";
                        viewStudents(query);    
                        break;
                    }
                    case 2:{
                        System.out.println("\n   + ADDING NEW STUDENT +\n");
                        addStudent();
                        break;
                    }
                    case 3:{ 
                        if (emptyTable) {
                            System.out.println("Students Table is Empty.");
                            break;
                        }
                        System.out.println("\n   + DELETING A STUDENT +\n");
                        deleteStudent();
                        break;
                    }
                    case 4:{ 
                        if (emptyTable) {
                            System.out.println("Students Table is Empty.");
                            break;
                        }
                        System.out.println("\n   + EDITING A STUDENT +\n");
                        updateStudent();                     
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
    
    public void viewStudents(String query){
        String[] studentsHeaders = {"ID", "Name", "Age", "Gender", "Email"};
        String[] studentsColumns = {"id", "s_name", "s_age", "s_gender", "s_email"};
        int spacing = 25;
        
        conf.viewRecords(query, spacing, studentsHeaders, studentsColumns);
    }
    
    public void addStudent(){
        System.out.println("Enter Student Details:");

        System.out.print("\nStudent Name: ");
        String name = scan.nextLine();

        System.out.print("Age: ");
        int age = scan.nextInt();

        System.out.print("Gender: ");
        String gender = scan.next();
        scan.nextLine();

        System.out.print("Email: ");
        String email = scan.nextLine();

        String sql = "INSERT INTO students (s_name, s_age, s_gender, s_email) VALUES (?, ?, ?, ?)";       
        conf.addRecord(sql, name, age, gender, email);
    }
    
    public void deleteStudent(){
        
        System.out.print("Student ID you want to delete: ");
        int id = scan.nextInt();

        conf.deleteRecord("students", id);
        
    }
    
    public void updateStudent(){

        int id;        
        boolean idExists;
        do{
            System.out.print("Student ID you want to delete: ");
            id = scan.nextInt();
            
            idExists = conf.doesIDExist("students", id);
            if(!idExists){
                System.out.println("Student ID Doesn't Exist.\n");
            }
        }while(!idExists);
        scan.nextLine();
        
        String query = "SELECT * FROM students WHERE id = " + id;
        viewStudents(query);
        
        System.out.println("Enter New Student Details:");

        System.out.print("\nNew Student Name: ");
        String name = scan.nextLine();

        System.out.print("New Age: ");
        int age = scan.nextInt();

        System.out.print("New Gender: ");
        String gender = scan.next();
        scan.nextLine();

        System.out.print("New Email: ");
        String email = scan.nextLine();
        
        String sql = "UPDATE students SET s_name = ?, s_age = ?, s_gender = ?, s_email = ? WHERE id = ?";
        conf.updateRecord(sql, name, age, gender, email, id);
    }
    
}
