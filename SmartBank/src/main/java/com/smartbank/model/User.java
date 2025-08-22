package com.smartbank.model;
public class User {
  private long id; private String username; private Role role;
  public User(long id, String username, Role role){this.id=id;this.username=username;this.role=role;}
  public long getId(){return id;} public String getUsername(){return username;} public Role getRole(){return role;}
  public String toString(){return "User{id=%d, username='%s', role=%s}".formatted(id, username, role);}}
