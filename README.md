📚 Web-based Educational Institute Management System

This is the repository for the SE2030 group project (Group ID: 2025-Y2S1-MLB-WE1G2-05).

A comprehensive, web-based platform designed to support the core operations of small to medium-sized tuition institutes. It provides a centralized, secure, and user-friendly system for managing students, tutors, schedules, attendance, lessons, and communication between all stakeholders.

👥 Team Members

Name

Student ID Number

[Your Name] (Group Leader)

[Your ID]

Sherangi S.A.G.A

IT24100958

Perera W.A.M.V

IT24101454

Samarasinghe S.R.G.N.B

IT24101261

Jeramy S.R

IT24101449

Chandrarathna K.M.D.S.

IT24101280

Kiritharan M.

IT24101277

💻 Technologies Used

Backend: Java, Spring Boot, Spring Security, Spring Data JPA 🌱

Frontend: Thymeleaf, HTML, CSS, Bootstrap 🎨

Database: MySQL 🛢️

Build Tool: Maven 📦

⚙️ Prerequisites

Before you can run this project, you MUST have the following installed:

☕ Java JDK (Version 17 or newer)

M Apache Maven (Usually included with your IDE)

💡 IntelliJ IDEA (Recommended) or VS Code

🐬 MySQL Workbench (or another MySQL client)

🚀 How to Run the Project

Follow these steps exactly:

1. Set up the Database 🗄️
* Open MySQL Workbench.
* Create a new database schema named exactly tuition_db. Execute:
sql CREATE DATABASE tuition_db; 

2. Configure the Database Connection 🔧
* In the project code, navigate to:
src/main/resources/application.properties
* Find and update these lines with your local MySQL username and password:
properties spring.datasource.username=your_mysql_username spring.datasource.password=your_mysql_password 
* Note: The project will not start without the correct credentials.

3. Run the Application ▶️
* Open the project folder in IntelliJ IDEA.
* Wait for Maven to download all dependencies (check the bottom status bar).
* Navigate to the main application file:
src/main/java/com/tms/tuition_management/TuitionManagementApplication.java
* Right-click on the file and select "Run 'TuitionManagementApplication'".
* The server will start. Look for Tomcat started on port(s): 8080 in the console.

4. Access the Application ✨
* Open your web browser and go to: http://localhost:8080/
* You should see the modern homepage.

🔑 Default Admin Credentials

Email: admin@tms.com

Password: admin123
