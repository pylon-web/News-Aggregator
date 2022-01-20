package com.saurabh.newsaggregator.bean;

import java.io.Serializable;

public class NewsChannelList implements Serializable {

    private String Name;
    private String id;
    private String catagory;
    private String country;
    private String language;


    public NewsChannelList(String id, String name, String category, String country, String language) {
        this.id = id;
        this.Name = name;
        this.catagory = category;
        this.country = country;
        this.language = language;

    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return id;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }
}
