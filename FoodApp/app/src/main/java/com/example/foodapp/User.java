package com.example.foodapp;

public class User {
    private String username;
    private String phone;
    private String email;
    private String pass;
    public User(){

    }
    public User(String username,String phone, String email, String pass){
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.pass = pass;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public void setPass(String pass){
        this.pass = pass;
    }
    public String getPass(){
        return this.pass;
    }
}
