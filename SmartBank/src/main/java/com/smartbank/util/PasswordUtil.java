package com.smartbank.util;
import javax.crypto.SecretKeyFactory; import javax.crypto.spec.PBEKeySpec; import java.security.SecureRandom; import java.util.Arrays;
public class PasswordUtil {
  private static final int ITERATIONS=65536; private static final int KEY_LENGTH=256;
  public static byte[] genSalt(){byte[] s=new byte[32]; new SecureRandom().nextBytes(s); return s;}
  public static byte[] hash(char[] pw, byte[] salt){ try{ PBEKeySpec spec=new PBEKeySpec(pw,salt,ITERATIONS,KEY_LENGTH); SecretKeyFactory skf=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); return skf.generateSecret(spec).getEncoded(); }catch(Exception e){ throw new RuntimeException("Password hashing failed", e);} }
  public static boolean verify(char[] pw, byte[] salt, byte[] expected){ return Arrays.equals(hash(pw,salt), expected); }
}
