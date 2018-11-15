package com.example.hello.coachingapp;

public class TeachersData {
    private String from, yop, degree, field, about, Since, Classes, Subjects, email, password, name, imgurl, type, tokenId;
    TeachersData()
    {}
    public TeachersData(String from, String yop, String degree, String field)
    {
        this.from = from;
        this.yop = yop;
        this.degree = degree;
        this.field = field;
    }
    public TeachersData(String from, String yop)
    {
        this.from = from;
        this.yop = yop;
    }

    public TeachersData(String since, String classes, String subjects) {
        Since = since;
        Classes = classes;
        Subjects = subjects;
    }

    public TeachersData(String about) {
        this.about = about;
    }

    public TeachersData( String email, String password, String name, String imageurl, String type, String tokenId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imgurl = imageurl;
        this.type = type;
        this.tokenId = tokenId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getType() {
        return type;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getFrom() {
        return from;
    }

    public String getYop() {
        return yop;
    }

    public String getDegree() {
        return degree;
    }

    public String getField() {
        return field;
    }

    public String getAbout() {
        return about;
    }

    public String getSince() {
        return Since;
    }

    public String getClasses() {
        return Classes;
    }

    public String getSubjects() {
        return Subjects;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setYop(String yop) {
        this.yop = yop;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setSince(String since) {
        Since = since;
    }

    public void setClasses(String classes) {
        Classes = classes;
    }

    public void setSubjects(String subjects) {
        Subjects = subjects;
    }
}
