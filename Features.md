Train Queue Management System

A Java-based application that simulates train ticket booking and efficiently manages passenger queues using JDBC and MySQL.

Features

Passenger Registration: Collects passenger information such as name, phone number, start location, and destination.

Seat Allocation: Random seat assignment ensuring no duplication within a booking session.

Payment Processing: Handles payments and stores payment records in the database.

Gate Assignment & Crowd Management: Automatically assigns the least crowded gate to passengers to optimize boarding.

Transaction Handling: Uses commit/rollback to ensure data consistency.

Booking Cancellation Handling: Supports safe cancellation before payment.

Technologies Used

Java

JDBC for database connectivity

MySQL as the backend database

Randomized algorithms for seat and gate allocation
