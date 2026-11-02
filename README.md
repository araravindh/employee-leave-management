# Employee Leave Management System (Java + JDBC + MySQL)

Menu-driven console application for employee registration, leave applications, approvals/rejections and leave balance tracking.

## Features
- Employee registration and employee listing
- Apply for leave and view request history
- Approve or reject pending leave requests
- Leave balance tracking (12 days initial allowance per employee)
- JDBC PreparedStatement, SQL JOIN and transaction handling

## Technologies
Java 17, JDBC, MySQL 8+, Maven.

## Setup
1. Run `database/employee_leave_management.sql` in MySQL Workbench.
2. Configure database credentials in `DBConnection.java` or set `DB_URL`, `DB_USER`, and `DB_PASSWORD` environment variables.
3. From the project root run `mvn clean compile`.
4. Run `mvn exec:java`.

## Test flow
Register employee -> apply leave -> view requests -> approve/reject -> check leave balance.

Use `YYYY-MM-DD` date format. New employees receive 12 leave days. Balance is deducted only when a pending request is approved. Pending and rejected requests do not deduct leave.

Do not commit real database credentials to GitHub.
