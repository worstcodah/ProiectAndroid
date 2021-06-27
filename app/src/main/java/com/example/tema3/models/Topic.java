package com.example.tema3.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Topic extends Element implements Parcelable {
    private String title;
    private String description;
    private String authorEmail;
    private boolean isSelected;
    private ArrayList<Comment> commentList;

    public Topic(String title, String description, String authorEmail, ArrayList<Comment> commentList) {
        this.title = title;
        this.description = description;
        this.authorEmail = authorEmail;
        this.commentList = commentList;
    }

    public Topic(String title, String description, String authorEmail) {
        this.title = title;
        this.description = description;
        this.authorEmail = authorEmail;
    }

    protected Topic(Parcel in) {
        title = in.readString();
        description = in.readString();
        authorEmail = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(authorEmail);
    }
}
