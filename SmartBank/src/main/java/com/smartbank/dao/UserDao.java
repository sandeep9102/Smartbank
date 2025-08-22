package com.smartbank.dao;
import com.smartbank.db.Database; import com.smartbank.model.Role; import com.smartbank.model.User; import com.smartbank.util.PasswordUtil;
import javax.sql.DataSource; import java.sql.*; import java.util.*;
public class UserDao {
  private final DataSource ds=Database.get();
  public User findByUsername(String username) throws Exception {
    String sql="SELECT id, username, role FROM users WHERE username=?";
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,username); ResultSet rs=ps.executeQuery();
      if(rs.next()) return new User(rs.getLong("id"), rs.getString("username"), Role.valueOf(rs.getString("role"))); return null; } }
  public long getUserIdByUsername(String username) throws Exception {
    String sql="SELECT id FROM users WHERE username=?";
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,username); ResultSet rs=ps.executeQuery();
      if(rs.next()) return rs.getLong(1); throw new Exception("User not found: "+username); } }
  public List<User> listUsers() throws Exception {
    String sql="SELECT id, username, role FROM users ORDER BY created_at DESC"; List<User> list=new ArrayList<>();
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ResultSet rs=ps.executeQuery();
      while(rs.next()) list.add(new User(rs.getLong("id"), rs.getString("username"), Role.valueOf(rs.getString("role")))); }
    return list; }
  public void createUser(String username, char[] password, Role role) throws Exception {
    String check="SELECT 1 FROM users WHERE username=?";
    try(Connection c=ds.getConnection(); PreparedStatement ck=c.prepareStatement(check)){ ck.setString(1,username); if(ck.executeQuery().next()) throw new Exception("Username already exists"); }
    String sql="INSERT INTO users(username, password_hash, password_salt, role) VALUES(?,?,?,?)";
    byte[] salt=PasswordUtil.genSalt(); byte[] hash=PasswordUtil.hash(password, salt);
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,username); ps.setBytes(2,hash); ps.setBytes(3,salt); ps.setString(4,role.name()); ps.executeUpdate(); }
  }
  public boolean verifyLogin(String username, char[] password) throws Exception {
    String sql="SELECT password_hash, password_salt FROM users WHERE username=?";
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,username); ResultSet rs=ps.executeQuery();
      if(rs.next()) return PasswordUtil.verify(password, rs.getBytes(1), rs.getBytes(2)); return false; } }
}
