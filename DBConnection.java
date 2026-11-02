package com.dinesh.leave;
import java.sql.*;
public final class DBConnection {
 private static final String URL=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/employee_leave_management");
 private static final String USER=System.getenv().getOrDefault("DB_USER","root");
 private static final String PASSWORD=System.getenv().getOrDefault("DB_PASSWORD","your_password");
 private DBConnection(){}
 public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL,USER,PASSWORD); }
}
