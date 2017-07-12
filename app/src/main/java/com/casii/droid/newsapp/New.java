package com.casii.droid.newsapp;

/**
 * Created by Casi on 09.07.2017.
 */

public class New {

    private String title;
    private String author;
    private String url;

    public New(String title, String author, String url) {
        super();
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebUrl() {
        return url;
    }
}
