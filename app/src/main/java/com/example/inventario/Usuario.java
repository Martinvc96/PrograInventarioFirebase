package com.example.inventario;

public class Usuario {
    private String UserName;
    private String Email;
    private String Password;

    public Usuario(String username, String email, String password){
        this.UserName = username;
        this.Email = email;
        this.Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName =  UserName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
