package com.example.tema3.models;

public class Comment extends Element {
    private String content;
    private String authorEmail;
    private boolean isSolution;
    private final boolean isDefault;
    private String key;


    public Comment(String content, String authorEmail, boolean isSolution, boolean isDefault) {
        this.content = content;
        this.authorEmail = authorEmail;
        this.isSolution = isSolution;
        this.isDefault = isDefault;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public boolean isSolution() {
        return isSolution;
    }

    public void setSolution(boolean solution) {
        isSolution = solution;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
