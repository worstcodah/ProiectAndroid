package com.example.tema3.models;

import java.util.ArrayList;

public class Topic extends Element {
    private String title;
    private String description;
    private String authorEmail;
    private ArrayList<Comment> commentList;

    public Topic(String title, String description, String authorEmail, ArrayList<Comment> commentList) {
        this.title = title;
        this.description = description;
        this.authorEmail = authorEmail;
        this.commentList = commentList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
}
