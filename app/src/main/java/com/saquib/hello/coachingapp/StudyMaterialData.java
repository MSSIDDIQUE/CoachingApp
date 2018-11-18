package com.saquib.hello.coachingapp;

public class StudyMaterialData {
    private String title,pdfurl;
    StudyMaterialData()
    {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public StudyMaterialData(String title, String pdfurl) {
        this.title = title;
        this.pdfurl = pdfurl;
    }


}
