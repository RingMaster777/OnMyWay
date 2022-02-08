package com.example.tourguide.helperclass;

public class sliderInfo {

    String division,title,url,reference;

    public sliderInfo() {
    }

    public sliderInfo(String division, String title, String reference,String url) {
        this.division = division;
        this.title = title;
        this.url = url;
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
