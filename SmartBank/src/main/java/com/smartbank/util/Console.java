package com.smartbank.util;
import java.util.Scanner;
public class Console { public static void title(String s){System.out.println("\n==== "+s+" ====\n");} public static void info(String s){System.out.println("[i] "+s);} public static void ok(String s){System.out.println("[\u2713] "+s);} public static void err(String s){System.out.println("[x] "+s);} public static String readPassword(Scanner sc){return sc.nextLine().trim();}}
