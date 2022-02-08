package com.example.tourguide.helperclass;

public class PostInfo {

    public String title, description,rating,state, image_uri, root, reference;
    public countRate countRating;



    public PostInfo(String title, String description,String rating,countRate countRating,String state, String image_uri, String root, String reference) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.countRating = countRating;
        this.state =state;
        this.image_uri = image_uri;
        this.root = root;
        this.reference = reference;


    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public PostInfo() {

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

    public String getDescription() {
        return description;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public countRate getCountRating() {
        return countRating;
    }

    public void setCountRating(countRate countRating) {
        this.countRating = countRating;
    }
}
