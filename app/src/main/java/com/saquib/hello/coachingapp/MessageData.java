package com.saquib.hello.coachingapp;

public class MessageData {
    private String title, Messagge, From;
    public MessageData() {
    }

    public MessageData(String title, String messagge,String from) {
        this.title = title;
        Messagge = messagge;
        From = from;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessagge() {
        return Messagge;
    }

    public void setMessagge(String messagge) {
        Messagge = messagge;
    }
}
