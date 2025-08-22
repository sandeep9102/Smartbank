package com.smartbank;
import com.smartbank.service.AuthService; import com.smartbank.service.BankingService; import com.smartbank.util.Console; import com.smartbank.model.Role; import com.smartbank.model.User; import java.util.Scanner;
public class App {
  public static void main(String[] args){
    Console.title("SmartBank – Java Banking System"); AuthService auth=new AuthService(); BankingService bank=new BankingService(); Scanner sc=new Scanner(System.in);
    Console.info("Tip: First run creates default admin: username=admin, password=admin123");
    while(true){
      System.out.println("\n1) Login  2) Exit"); System.out.print("Select: "); String c=sc.nextLine().trim(); if("2".equals(c)) break;
      try{
        System.out.print("Username: "); String u=sc.nextLine().trim();
        System.out.print("Password: "); String p=Console.readPassword(sc);
        User user=auth.login(u,p); Console.ok("Logged in as "+user.getUsername()+" ("+user.getRole()+")");
        if(user.getRole()==Role.ADMIN) adminMenu(sc,auth);
        else if(user.getRole()==Role.TELLER) tellerMenu(sc,bank,auth);
        else customerMenu(sc,bank,user);
      }catch(Exception e){ Console.err("Login failed: "+e.getMessage()); }
    }
    Console.info("Goodbye!");
  }
  private static void adminMenu(Scanner sc, AuthService auth){
    while(true){
      System.out.println("\n[ADMIN] 1) Create User  2) List Users  3) Logout"); System.out.print("Select: "); String c=sc.nextLine().trim();
      try{
        switch(c){
          case "1" -> { System.out.print("Username: "); String u=sc.nextLine().trim(); System.out.print("Password: "); String p=Console.readPassword(sc); System.out.print("Role (TELLER/CUSTOMER): "); String r=sc.nextLine().trim().toUpperCase(); auth.createUser(u,p,Role.valueOf(r)); Console.ok("User created."); }
          case "2" -> auth.listUsers().forEach(usr -> System.out.println("- "+usr));
          case "3" -> { return; }
          default -> System.out.println("Invalid.");
        }
      }catch(Exception e){ Console.err(e.getMessage()); }
    }
  }
  private static void tellerMenu(Scanner sc, BankingService bank, AuthService auth){
    while(true){
      System.out.println("\n[TELLER] 1) Create Account  2) Deposit  3) Withdraw  4) Transfer  5) Account Info  6) Tx History  7) Logout"); System.out.print("Select: ");
      String c=sc.nextLine().trim();
      try{
        switch(c){
          case "1" -> { System.out.print("Customer username: "); String uname=sc.nextLine().trim(); long userId=auth.getUserIdByUsername(uname); String acc=bank.createAccount(userId, "INR"); Console.ok("Account created: "+acc); }
          case "2" -> { System.out.print("Account number: "); String acc=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.deposit(acc, amt, "Teller cash"); Console.ok("Deposited."); }
          case "3" -> { System.out.print("Account number: "); String acc=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.withdraw(acc, amt, "Teller cash"); Console.ok("Withdrawn."); }
          case "4" -> { System.out.print("From account: "); String from=sc.nextLine().trim(); System.out.print("To account: "); String to=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.transfer(from,to,amt,"Teller transfer"); Console.ok("Transferred."); }
          case "5" -> { System.out.print("Account number: "); String acc=sc.nextLine().trim(); System.out.println(bank.accountInfo(acc)); }
          case "6" -> { System.out.print("Account number: "); String acc=sc.nextLine().trim(); bank.listTransactions(acc).forEach(System.out::println); }
          case "7" -> { return; }
          default -> System.out.println("Invalid.");
        }
      }catch(Exception e){ Console.err(e.getMessage()); }
    }
  }
  private static void customerMenu(Scanner sc, BankingService bank, User user){
    while(true){
      System.out.println("\n[CUSTOMER] 1) My Accounts  2) Deposit  3) Withdraw  4) Transfer  5) Balance  6) Tx History  7) Logout"); System.out.print("Select: "); String c=sc.nextLine().trim();
      try{
        switch(c){
          case "1" -> bank.listAccounts(user.getId()).forEach(System.out::println);
          case "2" -> { System.out.print("My account: "); String acc=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.deposit(acc, amt, "Self deposit"); Console.ok("Deposited."); }
          case "3" -> { System.out.print("My account: "); String acc=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.withdraw(acc, amt, "Self withdraw"); Console.ok("Withdrawn."); }
          case "4" -> { System.out.print("From my account: "); String from=sc.nextLine().trim(); System.out.print("To account: "); String to=sc.nextLine().trim(); System.out.print("Amount: "); double amt=Double.parseDouble(sc.nextLine().trim()); bank.transfer(from, to, amt, "Self transfer"); Console.ok("Transferred."); }
          case "5" -> { System.out.print("Account: "); String acc=sc.nextLine().trim(); Console.info("Balance: "+bank.getBalance(acc)); }
          case "6" -> { System.out.print("Account: "); String acc=sc.nextLine().trim(); bank.listTransactions(acc).forEach(System.out::println); }
          case "7" -> { return; }
          default -> System.out.println("Invalid.");
        }
      }catch(Exception e){ Console.err(e.getMessage()); }
    }
  }
}
