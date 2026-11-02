package com.dinesh.leave;
import java.sql.*;
public class EmployeeDAO {
 public void add(String name,String email,String dept,String phone) throws SQLException {
  try(Connection c=DBConnection.getConnection()){
   c.setAutoCommit(false);
   try(PreparedStatement p=c.prepareStatement("INSERT INTO employees(employee_name,email,department,phone) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
    p.setString(1,name);p.setString(2,email);p.setString(3,dept);p.setString(4,phone);p.executeUpdate();
    try(ResultSet k=p.getGeneratedKeys()){if(!k.next())throw new SQLException("Employee ID not generated");try(PreparedStatement b=c.prepareStatement("INSERT INTO leave_balance(employee_id,total_days,used_days) VALUES(?,12,0)")){b.setInt(1,k.getInt(1));b.executeUpdate();}}
    c.commit();System.out.println("Employee registered. Initial leave balance: 12 days.");
   }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
  }
 }
 public void list() throws SQLException {
  String q="SELECT e.employee_id,e.employee_name,e.email,e.department,e.phone,b.total_days,b.used_days,b.total_days-b.used_days remaining FROM employees e JOIN leave_balance b ON e.employee_id=b.employee_id ORDER BY e.employee_id";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){
   System.out.println("ID | Name | Email | Department | Phone | Total | Used | Remaining");
   while(r.next())System.out.printf("%d | %s | %s | %s | %s | %d | %d | %d%n",r.getInt(1),r.getString(2),r.getString(3),r.getString(4),r.getString(5),r.getInt(6),r.getInt(7),r.getInt(8));
  }
 }
}
