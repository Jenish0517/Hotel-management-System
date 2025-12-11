📘 Hotel Management System (Java + MySQL)

A simple and efficient console-based Hotel Management System built using Java and MySQL.
It allows hotel staff to manage guest bookings with full CRUD operations, automatic bill calculation, and secure database integration through JDBC and the DAO design pattern.

🚀 Features

➕ Add Booking (Guest details, room type, stay duration)

✏️ Update Booking

❌ Remove Booking

🔍 Search Booking by ID

📄 List All Bookings

💰 Automatic Bill Calculation based on room type (AC / Non-AC)

🗄️ MySQL Database Integration with table auto-creation

🔐 Secure Queries using Prepared Statements

🧱 DAO Pattern for clean & modular code structure

🏗️ Project Architecture
HotelManagementSystem (Main Class)
│
├── Booking (Model Class)
│
└── BookingDAO (Data Access Object)
      ├── addBooking()
      ├── updateBooking()
      ├── removeBooking()
      ├── getBookingById()
      └── getAllBookings()

🛠️ Tech Stack
Technology	Purpose
Java (JDK 8+)	Core application logic
MySQL	Database storage
JDBC	Database connectivity
DAO Pattern	Structured data access
📦 Database Setup

The program automatically creates:

Database: paymentsdb

Table: bookings

SQL Structure:

CREATE TABLE bookings (
  bookingId INT PRIMARY KEY,
  guestName VARCHAR(100),
  roomNumber INT,
  roomType VARCHAR(20),
  days INT,
  totalBill DOUBLE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

▶️ How to Run

Install MySQL and start the server.

Add the MySQL JDBC Driver (mysql-connector-j.jar) to your project.

Clone the project:

git clone https://github.com/your-username/hotel-management-system.git


Compile and run:

javac HotelManagementSystem.java
java HotelManagementSystem

💡 How It Works

User selects an option from the menu.

Inputs are taken using Scanner.

Bill is calculated automatically.

DAO methods handle all database insert/update/delete/search operations.

Results are shown on the console.

📸 Sample Menu Output
=== Hotel Management System ===
1. Add Booking
2. Update Booking
3. Remove Booking
4. Search Booking
5. List All Bookings
6. Exit
Enter your choice:

🧾 Example Bill Logic
AC Room     → Rs. 2000/day  
Non-AC Room → Rs. 1000/day  
totalBill = ratePerDay * days

📚 Learning Outcomes

✔ JDBC Connectivity
✔ SQL CRUD Operations
✔ DAO Pattern
✔ Java Exception Handling
✔ Modular Project Design

🤝 Contributing

Feel free to fork this repository and submit pull requests!
