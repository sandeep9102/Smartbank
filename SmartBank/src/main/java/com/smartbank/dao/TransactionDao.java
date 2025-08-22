package com.smartbank.dao;
import com.smartbank.db.Database; import com.smartbank.model.Transaction;
import javax.sql.DataSource; import java.sql.*; import java.util.*;
public class TransactionDao {
  private final DataSource ds=Database.get();
  public void record(Connection c, long accountId, String type, java.math.BigDecimal amount, String ref) throws Exception {
    String sql="INSERT INTO transactions(account_id, type, amount, reference) VALUES(?,?,?,?)";
    try(PreparedStatement ps=c.prepareStatement(sql)){ ps.setLong(1,accountId); ps.setString(2,type); ps.setBigDecimal(3,amount); ps.setString(4,ref); ps.executeUpdate(); } }
  public java.util.List<Transaction> list(String accountNumber) throws Exception {
    String sql="SELECT t.id, t.account_id, t.type, t.amount, t.reference, t.created_at FROM transactions t JOIN accounts a ON a.id=t.account_id WHERE a.account_number=? ORDER BY t.created_at DESC LIMIT 100";
    java.util.List<Transaction> res=new java.util.ArrayList<>(); try(Connection c=ds.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){ ps.setString(1,accountNumber); ResultSet rs=ps.executeQuery();
      while(rs.next()){ res.add(new Transaction(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getBigDecimal(4), rs.getString(5), rs.getTimestamp(6).toLocalDateTime())); } } return res; }
}
