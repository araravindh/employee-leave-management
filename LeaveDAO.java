package com.dinesh.leave;
import java.sql.*;
import java.time.LocalDate;
public class LeaveDAO {
 public void apply(int emp,String type,int days,LocalDate start,LocalDate end,String reason) throws SQLException {
  if(days<1||end.isBefore(start))throw new IllegalArgumentException("Invalid leave days or dates.");
  String q="INSERT INTO leave_requests(employee_id,leave_type,number_of_days,start_date,end_date,reason) VALUES(?,?,?,?,?,?)";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
   p.setInt(1,emp);p.setString(2,type);p.setInt(3,days);p.setDate(4,Date.valueOf(start));p.setDate(5,Date.valueOf(end));p.setString(6,reason);p.executeUpdate();System.out.println("Leave request submitted: PENDING");
  }
 }
 public void list(Integer emp) throws SQLException {
  String q="SELECT l.leave_id,e.employee_name,l.employee_id,l.leave_type,l.number_of_days,l.start_date,l.end_date,l.status,l.reason FROM leave_requests l JOIN employees e ON e.employee_id=l.employee_id"+(emp==null?"":" WHERE l.employee_id=?")+" ORDER BY l.applied_at DESC";
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
   if(emp!=null)p.setInt(1,emp);
   try(ResultSet r=p.executeQuery()){boolean found=false;while(r.next()){found=true;System.out.printf("%d | %s | Emp:%d | %s | %d days | %s to %s | %s | %s%n",r.getInt(1),r.getString(2),r.getInt(3),r.getString(4),r.getInt(5),r.getDate(6),r.getDate(7),r.getString(8),r.getString(9));}if(!found)System.out.println("No leave requests found.");}
  }
 }
 public void approve(int id) throws SQLException {
  try(Connection c=DBConnection.getConnection()){
   c.setAutoCommit(false);
   try{
    int emp,days;String status;
    try(PreparedStatement p=c.prepareStatement("SELECT employee_id,number_of_days,status FROM leave_requests WHERE leave_id=? FOR UPDATE")){
     p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("Leave request not found.");emp=r.getInt(1);days=r.getInt(2);status=r.getString(3);}
    }
    if(!"PENDING".equals(status))throw new SQLException("Only pending requests can be approved.");
    try(PreparedStatement p=c.prepareStatement("SELECT total_days-used_days FROM leave_balance WHERE employee_id=? FOR UPDATE")){
     p.setInt(1,emp);try(ResultSet r=p.executeQuery()){if(!r.next()||days>r.getInt(1))throw new SQLException("Insufficient leave balance.");}
    }
    try(PreparedStatement p=c.prepareStatement("UPDATE leave_balance SET used_days=used_days+? WHERE employee_id=?")){p.setInt(1,days);p.setInt(2,emp);p.executeUpdate();}
    try(PreparedStatement p=c.prepareStatement("UPDATE leave_requests SET status='APPROVED' WHERE leave_id=?")){p.setInt(1,id);p.executeUpdate();}
    c.commit();System.out.println("Leave approved; balance updated.");
   }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(true);}
  }
 }
 public void reject(int id) throws SQLException {
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("UPDATE leave_requests SET status='REJECTED' WHERE leave_id=? AND status='PENDING'")){
   p.setInt(1,id);System.out.println(p.executeUpdate()>0?"Leave rejected.":"Pending request not found.");
  }
 }
 public void balance(int emp) throws SQLException {
  try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT e.employee_name,b.total_days,b.used_days,b.total_days-b.used_days FROM employees e JOIN leave_balance b ON e.employee_id=b.employee_id WHERE e.employee_id=?")){
   p.setInt(1,emp);try(ResultSet r=p.executeQuery()){if(r.next())System.out.printf("%s | Total: %d | Used: %d | Remaining: %d%n",r.getString(1),r.getInt(2),r.getInt(3),r.getInt(4));else System.out.println("Employee not found.");}
  }
 }
}
