package com.smartbank.service;
import com.smartbank.dao.AccountDao; import com.smartbank.dao.TransactionDao; import com.smartbank.db.Database; import com.smartbank.model.Account;
import javax.sql.DataSource; import java.sql.Connection; import java.math.BigDecimal;
public class BankingService {
  private final DataSource ds=Database.get(); private final AccountDao accountDao=new AccountDao(); private final TransactionDao txDao=new TransactionDao();
  public String createAccount(long userId, String currency) throws Exception { return accountDao.createAccount(userId, currency); }
  public void deposit(String accountNumber, double amount, String ref) throws Exception {
    if(amount<=0) throw new Exception("Amount must be > 0"); try(Connection c=ds.getConnection()){ c.setAutoCommit(false);
      Account a=accountDao.getForUpdate(c, accountNumber); if(a==null) throw new Exception("Account not found");
      BigDecimal newBal=a.getBalance().add(BigDecimal.valueOf(amount)); accountDao.updateBalance(c, a.getId(), newBal);
      txDao.record(c, a.getId(), "DEPOSIT", BigDecimal.valueOf(amount), ref); c.commit(); } }
  public void withdraw(String accountNumber, double amount, String ref) throws Exception {
    if(amount<=0) throw new Exception("Amount must be > 0"); try(Connection c=ds.getConnection()){ c.setAutoCommit(false);
      Account a=accountDao.getForUpdate(c, accountNumber); if(a==null) throw new Exception("Account not found");
      if(a.getBalance().compareTo(BigDecimal.valueOf(amount))<0) throw new Exception("Insufficient funds");
      BigDecimal newBal=a.getBalance().subtract(BigDecimal.valueOf(amount)); accountDao.updateBalance(c, a.getId(), newBal);
      txDao.record(c, a.getId(), "WITHDRAW", BigDecimal.valueOf(amount), ref); c.commit(); } }
  public void transfer(String fromAcc, String toAcc, double amount, String ref) throws Exception {
    if(amount<=0) throw new Exception("Amount must be > 0"); if(fromAcc.equals(toAcc)) throw new Exception("Cannot transfer to same account");
    try(Connection c=ds.getConnection()){ c.setAutoCommit(false); Account from=accountDao.getForUpdate(c, fromAcc); Account to=accountDao.getForUpdate(c, toAcc);
      if(from==null||to==null) throw new Exception("Account not found"); if(from.getBalance().compareTo(BigDecimal.valueOf(amount))<0) throw new Exception("Insufficient funds");
      BigDecimal amt=BigDecimal.valueOf(amount); accountDao.updateBalance(c, from.getId(), from.getBalance().subtract(amt)); accountDao.updateBalance(c, to.getId(), to.getBalance().add(amt));
      txDao.record(c, from.getId(), "TRANSFER_OUT", amt, ref); txDao.record(c, to.getId(), "TRANSFER_IN", amt, ref); c.commit(); } }
  public String accountInfo(String accountNumber) throws Exception { return accountDao.accountInfo(accountNumber); }
  public double getBalance(String accountNumber) throws Exception { String info=accountInfo(accountNumber); return Double.parseDouble(info.split("Balance ")[1].split(" ")[0]); }
  public java.util.List<com.smartbank.model.Account> listAccounts(long userId) throws Exception { return accountDao.listByUser(userId); }
  public java.util.List<com.smartbank.model.Transaction> listTransactions(String accountNumber) throws Exception { return new TransactionDao().list(accountNumber); }
}
