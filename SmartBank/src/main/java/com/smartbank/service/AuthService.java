package com.smartbank.service;
import com.smartbank.dao.UserDao; import com.smartbank.model.Role; import com.smartbank.model.User;
public class AuthService {
  private final UserDao userDao=new UserDao(); private boolean bootstrapped=false;
  public com.smartbank.model.User login(String username, String password) throws Exception {
    bootstrapAdminIfNeeded(); boolean ok=userDao.verifyLogin(username, password.toCharArray()); if(!ok) throw new Exception("Invalid credentials");
    User u=userDao.findByUsername(username); if(u==null) throw new Exception("User not found after auth"); return u; }
  public void createUser(String username, String password, Role role) throws Exception { userDao.createUser(username, password.toCharArray(), role); }
  public long getUserIdByUsername(String username) throws Exception { return userDao.getUserIdByUsername(username); }
  public java.util.List<String> listUsers() throws Exception { return userDao.listUsers().stream().map(User::toString).collect(java.util.stream.Collectors.toList()); }
  private void bootstrapAdminIfNeeded() throws Exception { if(bootstrapped) return; User u=userDao.findByUsername("admin"); if(u==null){ userDao.createUser("admin","admin123".toCharArray(), Role.ADMIN);} bootstrapped=true; }
}
