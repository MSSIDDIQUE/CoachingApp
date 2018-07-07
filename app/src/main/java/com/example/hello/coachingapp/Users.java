package com.example.hello.coachingapp;

public class Users {
    private String Email,Name,Password,Type;

    public Users(String email, String name, String password, String type) {
        Type = type;
        Email = email;
        Name = name;
        Password = password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {

        Name = name;
    }

    public String getPassword() {

        return Password;
    }

    public void setPassword(String password) {

        Password = password;
    }
}
