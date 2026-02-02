.

📚 Library Management System (Java + MySQL)

A console-based Library Management System developed using Java and MySQL.
This project demonstrates full CRUD operations, JDBC integration, authentication, fine calculation, and relational database design.

🚀 Features

🔐 Admin Login Authentication

📚 Add / View / Update / Delete Books

👨‍🎓 Add / Update / Delete Students

📖 Issue Book

🔄 Return Book with Fine Calculation

🔍 Search Book (Using SQL LIKE)

📊 View Issued Books (Using JOIN)

🛡 Duplicate Issue Prevention

📦 Inventory Management

🗃 Full CRUD Operations

🛠 Technologies Used

Java (JDK 17)

JDBC

MySQL

IntelliJ IDEA

Git & GitHub

🗄 Database Design
📚 Books Table

id (Primary Key)

title

author

quantity

👨‍🎓 Students Table

id (Primary Key)

name

department

📖 Issued Books Table

issue_id (Primary Key)

student_id (Foreign Key)

book_id (Foreign Key)

issue_date

return_date

🔐 Admin Table

id (Primary Key)

username

password

🧠 DBMS Concepts Implemented

Primary Key

Foreign Key

JOIN

LIKE Operator

Prepared Statements

Auto Increment

Relational Integrity

📂 Project Structure
LibraryManagementSystem
 ├── src/com/library
 │     ├── Main.java
 │     ├── LibraryService.java
 │     └── DBConnection.java
 ├── .gitignore
 └── README.md

⚙️ How to Run the Project

1.Clone the repository:
git clone https://github.com/harikrishna000m/Library-Management-System-Java.git
2.Open in IntelliJ IDEA

3.Add MySQL Connector JAR to project

4.Create database library_db in MySQL

5.Run Main.java

🎯 Sample Admin Login
Username: admin
Password: 1234

📌 Future Enhancements

GUI version using Swing

Role-based authentication

Password encryption

Report generation

Web-based version using Spring Boot

👨‍💻 Author

Harikrishna M
BCA – East Point College of Management

⭐ If You Like This Project

Give this repository a ⭐ on GitHub!
