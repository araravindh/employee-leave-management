package com.dinesh.leave;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
public class Main {
 private static final Scanner in=new Scanner(System.in);
 private static final EmployeeDAO employees=new EmployeeDAO();
 private static final LeaveDAO leaves=new LeaveDAO();
 public static void main(String[] args){
  try(Connection c=DBConnection.getConnection()){System.out.println("Database connected.");}
  catch(SQLException e){System.out.println("Connection failed: "+e.getMessage());return;}
  while(true){
   System.out.println("\n=== EMPLOYEE LEAVE MANAGEMENT ===");
   System.out.println("1 Register Employee  2 View Employees  3 Apply Leave");
   System.out.println("4 View All Requests  5 View Employee Requests  6 Approve Leave");
   System.out.println("7 Reject Leave  8 View Leave Balance  0 Exit");
   int choice=integer("Choose: ");
   try{switch(choice){
    case 1->employees.add(text("Name: "),text("Email: "),text("Department: "),text("Phone: "));
    case 2->employees.list();
    case 3->leaves.apply(integer("Employee ID: "),text("Leave type: "),integer("Number of days: "),LocalDate.parse(text("Start date YYYY-MM-DD: ")),LocalDate.parse(text("End date YYYY-MM-DD: ")),text("Reason: "));
    case 4->leaves.list(null);
    case 5->leaves.list(integer("Employee ID: "));
    case 6->leaves.approve(integer("Leave request ID: "));
    case 7->leaves.reject(integer("Leave request ID: "));
    case 8->leaves.balance(integer("Employee ID: "));
    case 0->{System.out.println("Thank you.");in.close();return;}
    default->System.out.println("Invalid option.");
   }}catch(SQLException|IllegalArgumentException e){System.out.println("Operation failed: "+e.getMessage());}
  }
 }
 private static String text(String label){System.out.print(label);return in.nextLine().trim();}
 private static int integer(String label){while(true)try{return Integer.parseInt(text(label));}catch(NumberFormatException e){System.out.println("Enter a valid integer.");}}
}
