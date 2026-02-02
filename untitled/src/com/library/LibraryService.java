package com.library;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class LibraryService {

    public void addBook(String title, String author, int quantity) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO books(title, author, quantity) VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            System.out.println("Book added successfully!");

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addStudent(String name, String department) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO students(name, department) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, department);

            ps.executeUpdate();

            System.out.println("Student added successfully!");

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnBook(int issueId) {

        try {
            Connection con = DBConnection.getConnection();

            String selectQuery = "SELECT book_id, issue_date FROM issued_books WHERE issue_id = ?";
            PreparedStatement ps1 = con.prepareStatement(selectQuery);
            ps1.setInt(1, issueId);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {

                int bookId = rs.getInt("book_id");
                LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
                LocalDate today = LocalDate.now();

                long daysBetween = ChronoUnit.DAYS.between(issueDate, today);

                long fine = 0;
                if (daysBetween > 7) {
                    fine = (daysBetween - 7) * 5;
                }

                // Update return date
                String updateReturn = "UPDATE issued_books SET return_date = ? WHERE issue_id = ?";
                PreparedStatement ps2 = con.prepareStatement(updateReturn);
                ps2.setDate(1, java.sql.Date.valueOf(today));
                ps2.setInt(2, issueId);
                ps2.executeUpdate();

                // Increase book quantity back
                String updateQuantity = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
                PreparedStatement ps3 = con.prepareStatement(updateQuantity);
                ps3.setInt(1, bookId);
                ps3.executeUpdate();

                System.out.println("Book returned successfully!");
                System.out.println("Total Days: " + daysBetween);
                System.out.println("Fine Amount: ₹" + fine);

                ps2.close();
                ps3.close();

            } else {
                System.out.println("Invalid Issue ID!");
            }

            rs.close();
            ps1.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void issueBook(int studentId, int bookId) {

        try {
            Connection con = DBConnection.getConnection();

            // 🔹 Step 1: Check if student already has this book (not returned)
            String duplicateCheck = "SELECT * FROM issued_books WHERE student_id = ? AND book_id = ? AND return_date IS NULL";
            PreparedStatement checkStmt = con.prepareStatement(duplicateCheck);
            checkStmt.setInt(1, studentId);
            checkStmt.setInt(2, bookId);

            ResultSet duplicateRs = checkStmt.executeQuery();

            if (duplicateRs.next()) {
                System.out.println("This student already has this book and has not returned it!");
                duplicateRs.close();
                checkStmt.close();
                con.close();
                return;
            }

            duplicateRs.close();
            checkStmt.close();

            // 🔹 Step 2: Check if book is available
            String checkQuery = "SELECT quantity FROM books WHERE id = ?";
            PreparedStatement ps1 = con.prepareStatement(checkQuery);
            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");

                if (quantity > 0) {

                    // Insert into issued_books
                    String issueQuery = "INSERT INTO issued_books(student_id, book_id, issue_date) VALUES (?, ?, ?)";
                    PreparedStatement ps2 = con.prepareStatement(issueQuery);
                    ps2.setInt(1, studentId);
                    ps2.setInt(2, bookId);
                    ps2.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    ps2.executeUpdate();

                    // Reduce quantity
                    String updateQuery = "UPDATE books SET quantity = quantity - 1 WHERE id = ?";
                    PreparedStatement ps3 = con.prepareStatement(updateQuery);
                    ps3.setInt(1, bookId);
                    ps3.executeUpdate();

                    System.out.println("Book issued successfully!");

                    ps2.close();
                    ps3.close();

                } else {
                    System.out.println("Book not available!");
                }

            } else {
                System.out.println("Book ID not found!");
            }

            rs.close();
            ps1.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewBooks() {

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM books";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\n----- Available Books -----");
            System.out.printf("%-5s %-25s %-20s %-10s%n",
                    "ID", "Title", "Author", "Quantity");
            System.out.println("-----------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-25s %-20s %-10d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"));
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void viewIssuedBooks() {

        try {
            Connection con = DBConnection.getConnection();

            String query = """
                SELECT ib.issue_id,
                       s.name AS student_name,
                       b.title AS book_title,
                       ib.issue_date,
                       ib.return_date
                FROM issued_books ib
                JOIN students s ON ib.student_id = s.id
                JOIN books b ON ib.book_id = b.id
                """;

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- Issued Books -----");
            System.out.printf("%-8s %-20s %-25s %-12s %-12s%n",
                    "IssueID", "Student Name", "Book Title", "IssueDate", "ReturnDate");
            System.out.println("--------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-8d %-20s %-25s %-12s %-12s%n",
                        rs.getInt("issue_id"),
                        rs.getString("student_name"),
                        rs.getString("book_title"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date"));
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void searchBook(String keyword) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n----- Search Results -----");
            System.out.printf("%-5s %-25s %-20s %-10s%n",
                    "ID", "Title", "Author", "Quantity");
            System.out.println("-----------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-25s %-20s %-10d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity"));
            }

            if (!found) {
                System.out.println("No books found with that title.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean login(String username, String password) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public void updateBook(int id, String title, String author, int quantity) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE books SET title = ?, author = ?, quantity = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book updated successfully!");
            } else {
                System.out.println("Book ID not found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteBook(int id) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Book ID not found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateStudent(int id, String name, String department) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "UPDATE students SET name = ?, department = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, department);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student ID not found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM students WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
