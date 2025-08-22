package com.smartbank.model;
import java.math.BigDecimal; import java.time.LocalDateTime;
public class Transaction {
  private long id; private long accountId; private String type; private BigDecimal amount; private String reference; private LocalDateTime createdAt;
  public Transaction(long id,long accountId,String type,BigDecimal amount,String reference,LocalDateTime createdAt){this.id=id;this.accountId=accountId;this.type=type;this.amount=amount;this.reference=reference;this.createdAt=createdAt;}
  public String toString(){return "Tx{id=%d, accountId=%d, type=%s, amount=%s, ref=%s, at=%s}".formatted(id,accountId,type,amount.toPlainString(),reference,createdAt);}}
