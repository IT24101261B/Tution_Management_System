# Web-based Educational Institute Management System

This is the repository for the SE2030 group project (2025-Y2S1-MLB-WE1G2-05).

## Team Members
- Sherangi S.A.G.A (IT24100958)
- Perera W.A.M.V (IT24101454)
- Samarasinghe S.R.G.N.B (IT24101261)
- Jeramy S.R (IT24101449)
- Chandrarathna K.M.D.S. (IT24101280)
- Kiritharan M. (IT24101277)

## Technologies Used
- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, HTML, CSS
- **Database:** MySQL

## Prerequisites
Before you can run this project, you MUST have the following software installed on your computer:
1.  **Java JDK (Version 17 or newer)**
2.  **Apache Maven** (usually comes with your IDE)
3.  **IntelliJ IDEA** or **VS Code**
4.  **MySQL Workbench** (or another tool to manage the MySQL database)

## How to Run the Project

**Step 1: Set up the Database**
1.  Open MySQL Workbench.
2.  Create a new database schema (database) named exactly `tuition_db`.

**Step 2: Configure the Database Connection**
1.  In the project code, navigate to `src/main/resources/application.properties`.
2.  Find these lines:
    ```properties
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    ```
3.  Change `your_mysql_username` and `your_mysql_password` to your own local MySQL username and password (the ones you use for MySQL Workbench). **The project will not run without this step.**

**Step 3: Run the Application**
1.  Open the project in IntelliJ IDEA.
2.  Wait for it to download all the Maven dependencies.
3.  Navigate to `src/main/java/com/tms/tuition_management/TuitionManagementApplication.java`.
4.  Right-click on the file and select "Run 'TuitionManagementApplication'".
5.  The server will start.

**Step 4: Access the Application**
-   Open your web browser and go to the main homepage: **http://localhost:8080/**
-   The default **Admin** login credentials are:
    -   **Email:** `admin@tms.com`
    -   **Password:** `admin123`