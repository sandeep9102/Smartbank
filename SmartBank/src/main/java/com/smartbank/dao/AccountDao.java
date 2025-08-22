package com.smartbank.dao;
import com.smartbank.db.Database; import com.smartbank.model.Account;
import javax.sql.DataSource; import java.sql.*; import java.util.*; import java.math.BigDecimal;
public class AccountDao {
  private final DataSource ds=Database.get();
  public String createAccount(long userId, String currency) throws Exception {
    String accNum=generateAccountNumber(); String sql="INSERT INTO accounts(user_id, account_number, balance, currency, status) VALUES(?,?,?,?, 'ACTIVE')";
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setLong(1,userId); ps.setString(2,accNum); ps.setBigDecimal(3,BigDecimal.ZERO); ps.setString(4,currency); ps.executeUpdate(); return accNum; } }
  private String generateAccountNumber(){ return "SB"+System.currentTimeMillis(); }
  public Account getForUpdate(Connection c, String acc) throws Exception {
    String sql="SELECT * FROM accounts WHERE account_number=? FOR UPDATE"; try(PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,acc); ResultSet rs=ps.executeQuery(); if(!rs.next()) return null; return map(rs);} }
  public List<Account> listByUser(long userId) throws Exception {
    String sql="SELECT * FROM accounts WHERE user_id=? ORDER BY created_at DESC"; List<Account> res=new ArrayList<>();
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setLong(1,userId); ResultSet rs=ps.executeQuery(); while(rs.next()) res.add(map(rs)); } return res; }
  public String accountInfo(String acc) throws Exception {
    String sql="SELECT account_number, balance, currency, status, created_at FROM accounts WHERE account_number=?";
    try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,acc); ResultSet rs=ps.executeQuery();
      if(rs.next()) return "Account "+rs.getString(1)+" | Balance "+rs.getBigDecimal(2)+" "+rs.getString(3)+" | Status "+rs.getString(4)+" | Opened "+rs.getTimestamp(5);
      throw new Exception("Account not found"); } }
  private Account map(ResultSet rs) throws Exception {
    return new Account(rs.getLong("id"), rs.getLong("user_id"), rs.getString("account_number"), rs.getBigDecimal("balance"), rs.getString("currency"), rs.getString("status")); }
}
