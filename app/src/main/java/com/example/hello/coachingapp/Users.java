package com.example.hello.coachingapp;

public class Users {
    private String Email,Name,Password,Type,Imgurl,TokenId;

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String tokenId) {
        TokenId = tokenId;
    }

    public Users(String email, String name, String password, String type, String imgurl, String tokenId) {
        Type = type;
        Email = email;
        Name = name;
        Password = password;
        Imgurl = imgurl;
        TokenId = tokenId;

    }

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }

    public Users() {}

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
