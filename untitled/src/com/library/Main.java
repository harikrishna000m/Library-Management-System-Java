package com.library;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LibraryService service = new LibraryService();

        System.out.println("===== Library Admin Login =====");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (service.login(username, password)) {

            System.out.println("Login Successful!");

            while (true) {
                System.out.println("1. Add Book");
                System.out.println("2. Add Student");
                System.out.println("3. Issue Book");
                System.out.println("4. Return Book");
                System.out.println("5. View Books");
                System.out.println("6. View Issued Books");
                System.out.println("7. Search Book");
                System.out.println("8. Update Book");
                System.out.println("9. Delete Book");
                System.out.println("10. Update Student");
                System.out.println("11. Delete Student");
                System.out.println("12. Exit");
                System.out.print("enter your choice:");



                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        sc.nextLine();
                        System.out.print("Enter Book Title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter Author: ");
                        String author = sc.nextLine();
                        System.out.print("Enter Quantity: ");
                        int quantity = sc.nextInt();
                        service.addBook(title, author, quantity);
                        break;

                    case 2:
                        sc.nextLine();
                        System.out.print("Enter Student Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Department: ");
                        String department = sc.nextLine();
                        service.addStudent(name, department);
                        break;

                    case 3:
                        System.out.print("Enter Student ID: ");
                        int studentId = sc.nextInt();
                        System.out.print("Enter Book ID: ");
                        int bookId = sc.nextInt();
                        service.issueBook(studentId, bookId);
                        break;

                    case 4:
                        System.out.print("Enter Issue ID: ");
                        int issueId = sc.nextInt();
                        service.returnBook(issueId);
                        break;

                    case 5:
                        service.viewBooks();
                        break;

                    case 6:
                        service.viewIssuedBooks();
                        break;

                    case 7:
                        sc.nextLine();
                        System.out.print("Enter book title keyword: ");
                        String keyword = sc.nextLine();
                        service.searchBook(keyword);
                        break;

                    case 8:
                        System.out.print("Enter Book ID to update: ");
                        int updateId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter New Title: ");
                        String newTitle = sc.nextLine();

                        System.out.print("Enter New Author: ");
                        String newAuthor = sc.nextLine();

                        System.out.print("Enter New Quantity: ");
                        int newQuantity = sc.nextInt();

                        service.updateBook(updateId, newTitle, newAuthor, newQuantity);
                        break;

                    case 9:
                        System.out.print("Enter Book ID to delete: ");
                        int deleteId = sc.nextInt();
                        service.deleteBook(deleteId);
                        break;

                    case 10:
                        System.out.print("Enter Student ID to update: ");
                        int stuId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter New Name: ");
                        String newName = sc.nextLine();

                        System.out.print("Enter New Department: ");
                        String newDept = sc.nextLine();

                        service.updateStudent(stuId, newName, newDept);
                        break;

                    case 11:
                        System.out.print("Enter Student ID to delete: ");
                        int delStuId = sc.nextInt();
                        service.deleteStudent(delStuId);
                        break;

                    case 12:
                        System.out.println("Exiting...");
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } else {
            System.out.println("Invalid Username or Password!");
        }
    }
}