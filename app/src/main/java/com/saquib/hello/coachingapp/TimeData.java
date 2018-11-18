package com.saquib.hello.coachingapp;

public class TimeData {
    private String Standard, Subject, Teacher, Days, StartingTime, EndingTime;
    TimeData()
    { }

    public TimeData(String standard, String subject, String teacher, String days, String startingTime, String endingTime) {
        Standard = standard;
        Subject = subject;
        Teacher = teacher;
        Days = days;
        StartingTime = startingTime;
        EndingTime = endingTime;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getStartingTime() {
        return StartingTime;
    }

    public void setStartingTime(String startingTime) {
        StartingTime = startingTime;
    }

    public String getEndingTime() {
        return EndingTime;
    }

    public void setEndingTime(String endingTime) {
        EndingTime = endingTime;
    }
}
