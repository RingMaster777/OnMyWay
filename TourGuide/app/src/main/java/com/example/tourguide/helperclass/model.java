package com.example.tourguide.helperclass;

public class model {
    String title, rating, description, state, image_uri;

    public model() {
    }

    public model(String title, String rating, String description, String state, String image_uri ) {
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.state = state;
        this.image_uri = image_uri;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
