# Hotel Reservation System

A Java Swing desktop application for hotel room browsing, customer registration, room management, reservations, and payment-status updates. It uses MySQL for persistent data storage through JDBC.

## Technology Stack

- Java (Swing)
- MySQL
- JDBC / MySQL Connector/J
- NetBeans-compatible Java source structure

## Features

- Customer registration and login
- Browse and search rooms
- Check room availability for selected dates
- Create and view reservations
- Update reservation/payment status
- Admin screens for managing rooms, customers, and reservations

## Requirements

1. JDK 8 or later (JDK 17 recommended).
2. MySQL Server running locally.
3. MySQL Connector/J JDBC driver.
4. Windows PowerShell or an IDE such as NetBeans.

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
            ├── ExplorePage.java
            ├── RoomSearchForm.java
            ├── RoomBookingForm.java
            ├── ReservationViewForm.java
            └── PaymentPage.java
```

## Database Setup

The application connects to a MySQL database named `hotel_db`. Create the database and tables in MySQL Workbench:

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
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (room_number) REFERENCES Rooms(room_number),
    CHECK (checkout_date > checkin_date)
);
```

Add a few rooms for testing:

```sql
INSERT INTO Rooms (room_number, room_type, price_per_night) VALUES
(101, 'Single', 1500.00),
(102, 'Double', 2500.00),
(201, 'Deluxe', 4000.00);
```

Run the sample `INSERT` only once. If the room numbers already exist, skip it.

## Configure MySQL Credentials

Open `src/hotel/DBConnection.java`. The supplied source currently uses:

```java
private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
private static final String USER = "root";
private static final String PASS = "root";
```

Change `USER` and `PASS` to match your local MySQL account. Make sure MySQL is running on port `3306`, or update the JDBC URL if you use a different port.

## Run on Windows (PowerShell)

### 1. Open the project source directory

If you extracted the repository into the same folder structure shown in the supplied instructions, run:

```powershell
cd "D:\Hotel_reservation\hotel_reseervation_system-main\HotelReservationSystem-main\HotelReservationSystem"
```

If your extraction location differs, `cd` to the folder that directly contains the `src` directory.

Verify the source exists:

```powershell
Test-Path .\src\hotel\ui\SplashScreen.java
```

This should return `True`.

### 2. Check Java

```powershell
java -version
javac -version
```

Both commands should work. If `javac` is not recognized, install a JDK and configure `PATH`.

### 3. Locate MySQL Connector/J

The current extracted copy may already have the driver here:

```text
dist\lib\mysql-connector-j-9.4.0.jar
```

Check that it exists:

```powershell
Test-Path .\dist\lib\mysql-connector-j-9.4.0.jar
```

If this returns `False`, download Connector/J from https://dev.mysql.com/downloads/connector/j/ and use the actual path to the `.jar` file. The repository ZIP may not include the `dist` folder.

### 4. Compile the Java source

Run these commands from the folder containing `src`:

```powershell
New-Item -ItemType Directory -Force -Path .\out | Out-Null

$sources = Get-ChildItem .\src -Recurse -Filter *.java |
    ForEach-Object { $_.FullName }

javac -cp ".\dist\lib\mysql-connector-j-9.4.0.jar" -d .\out $sources
```

If the JAR is in a different location, replace the JAR path in the `-cp` argument with its real path.

If compilation completes without errors, verify the main class:

```powershell
Test-Path .\out\hotel\ui\SplashScreen.class
```

It should return `True`.

### 5. Copy UI images

The UI source uses image assets. Copy them into the compiled package folder:

```powershell
New-Item -ItemType Directory -Force -Path .\out\hotel\ui | Out-Null

Copy-Item .\src\hotel\ui\*.png, .\src\hotel\ui\*.jpg, .\src\hotel\ui\*.jpeg `
    .\out\hotel\ui\ -ErrorAction SilentlyContinue
```

### 6. Launch the application

```powershell
java -cp ".\out;.\dist\lib\mysql-connector-j-9.4.0.jar" hotel.ui.SplashScreen
```

Use the actual JAR path if it differs. On Windows, classpath entries are separated with a semicolon (`;`).

## Run in NetBeans

1. Open NetBeans.
2. Create or open a Java project.
3. Add the files under `src` while preserving package names (`hotel`, `hotel.dao`, `hotel.models`, `hotel.ui`).
4. Add the MySQL Connector/J JAR under **Project Properties → Libraries**.
5. Confirm the MySQL database and credentials.
6. Run `hotel.ui.SplashScreen`.

## Troubleshooting

- **`error: no source files`**: PowerShell is not in the project directory containing `src`. Run `Get-ChildItem` and `Test-Path .\src\hotel\ui\SplashScreen.java`.
- **`ClassNotFoundException: hotel.ui.SplashScreen`**: Compile successfully first and verify `.\out\hotel\ui\SplashScreen.class` exists.
- **`No suitable driver` or driver class errors**: Confirm the Connector/J JAR path is correct and included in the runtime classpath.
- **`Access denied for user 'root'`**: Update the credentials in `DBConnection.java`.
- **`Unknown database 'hotel_db'`**: Create the database using the SQL above.
- **`Table doesn't exist` or `Unknown column`**: Confirm the tables/columns match the DAO SQL statements.
- **`Connection refused`**: Start MySQL and check the host and port in the JDBC URL.
- **Image not found**: Confirm the image files are copied into `out\hotel\ui`.

## Notes

- This is a Java desktop application; it does not run at a browser URL such as `http://localhost:3000`.
- The sample database schema is based on the SQL statements in the provided DAO source files. If you modify the application, update the schema accordingly.
- Do not commit real database passwords to a public repository.
