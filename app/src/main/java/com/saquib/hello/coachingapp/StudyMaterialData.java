package com.saquib.hello.coachingapp;

public class StudyMaterialData {
    private String title,pdfurl,coverurl;
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

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public StudyMaterialData(String title, String pdfurl, String coverurl) {
        this.title = title;
        this.pdfurl = pdfurl;
        this.coverurl = coverurl;
    }


}
