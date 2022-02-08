package com.example.tourguide.helperclass;

public class cmnHelper {

    String comment_username, comment_content, comment_rating;
    String comment_user_imageUri;
    String comment_date;

    public cmnHelper() {
    }

    public String getComment_user_imageUri() {
        return comment_user_imageUri;
    }

    public void setComment_user_imageUri(String comment_user_imageUri) {
        this.comment_user_imageUri = comment_user_imageUri;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public cmnHelper(String comment_username, String comment_content, String comment_rating, String comment_user_imageUri, String comment_date) {
        this.comment_username = comment_username;
        this.comment_content = comment_content;
        this.comment_rating = comment_rating;
        this.comment_user_imageUri = comment_user_imageUri;
        this.comment_date = comment_date;
    }

    public String getComment_username() {
        return comment_username;
    }

    public void setComment_username(String comment_username) {
        this.comment_username = comment_username;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_rating() {
        return comment_rating;
    }

    public void setComment_rating(String comment_rating) {
        this.comment_rating = comment_rating;
    }
}
