package com.example.tourguide.helperclass;

public class Update {
    String bio, contact, job, location;

    public Update(String bio, String contact, String job, String location) {
        this.bio = bio;
        this.contact = contact;
        this.job = job;
        this.location = location;
    }
    public Update() {

    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
