import java.util.ArrayList;
import java.util.Scanner;

class Student {
    int id;
    String name;
    int age;
    String course;

    Student(int id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    void display() {
        System.out.println("ID: " + id + " | Name: " + name + " | Age: " + age + " | Course: " + course);
    }
}

public class StudentManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while (true) {
            System.out.println("=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Remove Student");
            System.out.println("4. Search Student");
            System.out.println("5. List All Students");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 1) { 
                System.out.print("Enter ID: ");
                int id = sc.nextInt();
                System.out.print("Enter Name (no spaces): ");
                String name = sc.next();
                System.out.print("Enter Age: ");
                int age = sc.nextInt();
                System.out.print("Enter Course (no spaces): ");
                String course = sc.next();

                Student s = new Student(id, name, age, course);
                students.add(s);
                System.out.println("Student added.\n");

            } else if (choice == 2) { 
                System.out.print("Enter ID to update: ");
                int id = sc.nextInt();
                boolean found = false;

                for (Student s : students) {
                    if (s.id == id) {
                        System.out.print("Enter new Name (no spaces): ");
                        s.name = sc.next();
                        System.out.print("Enter new Age: ");
                        s.age = sc.nextInt();
                        System.out.print("Enter new Course (no spaces): ");
                        s.course = sc.next();
                        System.out.println("Student updated.\n");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Student not found.\n");
                }

            } else if (choice == 3) {
                System.out.print("Enter ID to remove: ");
                int id = sc.nextInt();
                boolean removed = false;

                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).id == id) {
                        students.remove(i);
                        System.out.println("Student removed.\n");
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    System.out.println("Student not found.\n");
                }

            } else if (choice == 4) { 
                System.out.print("Enter ID to search: ");
                int id = sc.nextInt();
                boolean found = false;

                for (Student s : students) {
                    if (s.id == id) {
                        System.out.println("Student found:");
                        s.display();
                        System.out.println();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Student not found.\n");
                }

            } else if (choice == 5) { 
                if (students.isEmpty()) {
                    System.out.println("No students.\n");
                } else {
                    System.out.println("All students:");
                    for (Student s : students) {
                        s.display();
                    }
                    System.out.println();
                }

            } else if (choice == 6) { 
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice.\n");
            }
        }

        sc.close();
    }
}
