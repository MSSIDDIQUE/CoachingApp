package com.saquib.hello.coachingapp;

public class ToppersData
{
    private String imgurl, name, resulturl, rollno, schoolname, percentage, session, stream;
    public ToppersData() {
    }

    public ToppersData(String imgurl, String name, String percentage, String resulturl, String rollno, String schoolname, String session, String stream) {
        this.imgurl = imgurl;
        this.name = name;
        this.resulturl = resulturl;
        this.rollno = rollno;
        this.schoolname = schoolname;
        this.percentage = percentage;
        this.session = session;
        this.stream = stream;
    }

    public String getResulturl() {
        return resulturl;
    }

    public void setResulturl(String resulturl) {
        this.resulturl = resulturl;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercetage(String percetage) {
        this.percentage = percetage;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
