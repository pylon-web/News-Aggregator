package com.saurabh.newsaggregator.bean;

import java.io.Serializable;

public class News implements Serializable {

    private String Headline;
    private String Date;
    private String Author;
    private String Image;
    private String Description;
    private String url;

    public News(String headline, String author, String description, String date, String image,String url) {
        Headline = headline;
        Date = date;
        Author = author;
        Image = image;
        Description = description;
        this.url = url;
    }

    public String getHeadline() {
        return Headline;
    }

    public void setHeadline(String headline) {
        Headline = headline;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
