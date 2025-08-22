package com.smartbank.model;
import java.math.BigDecimal;
public class Account {
  private long id; private long userId; private String accountNumber; private BigDecimal balance; private String currency; private String status;
  public Account(long id,long userId,String accountNumber,BigDecimal balance,String currency,String status){this.id=id;this.userId=userId;this.accountNumber=accountNumber;this.balance=balance;this.currency=currency;this.status=status;}
  public long getId(){return id;} public long getUserId(){return userId;} public String getAccountNumber(){return accountNumber;} public BigDecimal getBalance(){return balance;} public String getCurrency(){return currency;} public String getStatus(){return status;}
  public String toString(){return "Account{id=%d, userId=%d, accountNumber='%s', balance=%s %s, status=%s}".formatted(id,userId,accountNumber,balance.toPlainString(),currency,status);}}
