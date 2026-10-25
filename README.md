# Hotel Reservation System

A Java desktop application for managing hotel rooms, customers, reservations, and payments. The interface is built with Java Swing, and MySQL is used to store application data.

## Features

- Customer registration and login
- Room search and booking
- Reservation viewing and management
- Payment status updates
- Customer, room, and reservation data access through DAO classes
- Admin and customer dashboards

## Technology Stack

- Java (Swing)
- MySQL
- JDBC (MySQL Connector/J)
- IDE: NetBeans or IntelliJ IDEA (optional)

## Prerequisites

Install the following before running the application:

1. **JDK** (JDK 8 or later; JDK 17 is a good choice).
2. **MySQL Server** and a client such as MySQL Workbench or phpMyAdmin.
3. **MySQL Connector/J** (the JDBC driver JAR).
4. An IDE such as NetBeans, or a terminal for compiling and running Java.

> The provided source uses the database `hotel_db`, MySQL username `root`, and password `root`. Change these values in `src/hotel/DBConnection.java` if your local MySQL credentials differ.

## Project Structure

```text
HotelReservationSystem/
├── manifest.mf
└── src/
    └── hotel/
        ├── DBConnection.java
        ├── dao/
        │   ├── CustomerDAO.java
        │   ├── ReservationDAO.java
        │   └── RoomDAO.java
        ├── models/
        │   ├── Customer.java
        │   ├── Reservation.java
        │   └── Room.java
        └── ui/
            ├── SplashScreen.java
            ├── LoginForm.java
            ├── RegisterForm.java
            ├── CustomerDashboard.java
            ├── AdminDashboard.java
            ├── RoomSearchForm.java
            ├── RoomBookingForm.java
            ├── ReservationViewForm.java
            └── PaymentPage.java
```

## Database Setup

The ZIP supplied with this project does not include a SQL dump, so create the database and tables before running the app. The following schema matches the table and column names used by the DAO classes. If your code contains additional columns, add them as required.

1. Start MySQL.
2. Open MySQL Workbench or a MySQL command line.
3. Run the SQL below:

```sql
CREATE DATABASE IF NOT EXISTS hotel_db;
USE hotel_db;

CREATE TABLE IF NOT EXISTS Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Rooms (
    room_number INT PRIMARY KEY,
    room_type VARCHAR(100) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    room_number INT NOT NULL,
    checkin_date DATE NOT NULL,
    checkout_date DATE NOT NULL,
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_number) REFERENCES Rooms(room_number),
    CONSTRAINT chk_reservation_dates CHECK (checkout_date > checkin_date)
);

-- Optional sample rooms
INSERT INTO Rooms (room_number, room_type, price_per_night) VALUES
(101, 'Single', 1500.00),
(102, 'Double', 2500.00),
(201, 'Deluxe', 4000.00);
```

If you run the SQL more than once, the sample `INSERT` statements may fail because the room numbers are primary keys. Run those inserts only once.

## Configure the Database Connection

Open `src/hotel/DBConnection.java` and check these values:

```java
private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
private static final String USER = "root";
private static final String PASS = "root";
```

Replace `USER` and `PASS` with your MySQL username and password. Ensure MySQL is running on port `3306`, or update the URL if you use another port.

## Run in NetBeans (recommended)

1. Extract the ZIP file.
2. Open NetBeans and choose **File → New Project → Java with Ant → Java Application** (or open the existing project if you have its complete NetBeans project metadata).
3. Add the extracted `src` files to the project's Source Packages, preserving the `hotel`, `hotel.dao`, `hotel.models`, and `hotel.ui` packages.
4. Download MySQL Connector/J from the official MySQL website.
5. In NetBeans, right-click the project → **Properties → Libraries → Add JAR/Folder**, then select the MySQL Connector/J JAR.
6. Confirm the database and connection credentials are correct.
7. Run `hotel.ui.SplashScreen` (it contains the `main` method).

## Run from the Windows Command Prompt

The commands below assume the extracted folder contains `src` and that you have downloaded the MySQL Connector/J JAR. Replace the JAR path with its actual location.

From the `HotelReservationSystem` directory:

```bat
mkdir out
javac -cp ".;C:\path\to\mysql-connector-j.jar" -d out src\hotel\DBConnection.java src\hotel\dao\*.java src\hotel\models\*.java src\hotel\ui\*.java
java -cp "out;C:\path\to\mysql-connector-j.jar" hotel.ui.SplashScreen
```

On Windows, use `;` to separate classpath entries. On macOS/Linux, use `:` instead. If compilation reports that a referenced image or another class is missing, confirm that all files from `src` are present and that package names match the folder structure.

## Common Problems

- **`Access denied for user 'root'`**: Update the username/password in `DBConnection.java` to match your MySQL setup.
- **`Unknown database 'hotel_db'`**: Create the database using the SQL above.
- **`No suitable driver` / `ClassNotFoundException: com.mysql.cj.jdbc.Driver`**: Add MySQL Connector/J to the project classpath.
- **`Table doesn't exist`**: Create the required tables in `hotel_db`; table names are `Customers`, `Rooms`, and `Reservations`.
- **Connection refused**: Start MySQL and verify the host/port in the JDBC URL.
- **Login/admin account issues**: Check the login and registration UI code for the credentials or account fields it expects. The included DAO for customers stores names and email, so authentication may depend on additional code or schema not represented by the DAO alone.

## Notes

- This is a Java **desktop application**, not a website that runs at a `localhost` URL.
- The supplied ZIP contains Java source files and images, but no database export or bundled MySQL JDBC driver.
- The README's schema is a starting point based on the SQL queries found in the DAO classes. Review all model/UI code and adjust the schema if your local version expects additional fields.
