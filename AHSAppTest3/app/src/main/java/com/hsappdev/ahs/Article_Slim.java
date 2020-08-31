package com.hsappdev.ahs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Article_Slim implements Parcelable, Comparable<Article_Slim> {

    private final String ID;
    private final long time_updated;
    private final String title;
    private final String story;
    private final String imagePath;

    private final Article.Type type;

    public Article_Slim(Article article) {
        this.ID = article.getID();
        this.time_updated = article.getTimeUpdated();
        this.title = article.getTitle();
        String story = article.getStory();
        this.story = (story.length() > 200) ? story.substring(0, 200) : story;
        if(article.getImagePaths().length > 0)
            this.imagePath = article.getImagePaths()[0];
        else
            this.imagePath = "";
        this.type = article.getType();
    }

    public static ArrayList<Article_Slim> toArticle_Slim(ArrayList<Article> articles) {
        ArrayList<Article_Slim> article_slims= new ArrayList<>();
        for(Article article: articles) {
            article_slims.add(new Article_Slim(article));
        }
        return article_slims;
    }

    public Article_Slim(
            @NonNull
            String ID,
            long time_updated,
            @NonNull
            String title,
            @NonNull
            String story,
            @NonNull
            String imagePath,
            Article.Type type
    )
    {
        this.ID = ID;
        this.time_updated = time_updated;
        this.title = title;
        this.story = (story.length() > 200) ? story.substring(0, 200) : story;
        this.imagePath = imagePath;
        this.type = type;
    }

    public static final Creator<Article_Slim> CREATOR = new Creator<Article_Slim>() {
        @Override
        public Article_Slim createFromParcel(Parcel in) {
            return new Article_Slim(in);
        }

        @Override
        public Article_Slim[] newArray(int size) {
            return new Article_Slim[size];
        }
    };

    public String getID() {return ID;}
    public long getTimeUpdated()
    {
        return time_updated;
    }
    public String getTitle()
    {
        return title;
    }
    public String getStory()
    {
        return story;
    }
    public String getImagePath()
    {
        return imagePath;
    }

    public Article.Type getType() { return type;}

    @NonNull
    @Override
    public String toString()
    {
        return "ID::\t" + ID + "\n" +
        "time::\t" + time_updated + "\n" +
        "title::\t" + title + "\n" +
        "story::\t" + ((story.length() > 40) ? story.substring(0,40) : story) + "\n" + // so output might not be overly long
        "type::\t" + type.toString();
    }

    // The following methods are for the purpose of extending Parcelable
    // make sure to update methods should a new field be added
    protected Article_Slim(Parcel in) {
        ID = in.readString();
        time_updated = in.readLong();
        title = in.readString();
        story = in.readString();
        imagePath = in.readString();
        type = (Article.Type) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeLong(time_updated);
        dest.writeString(title);
        dest.writeString(story);
        dest.writeString(imagePath);
        dest.writeSerializable(type);
    }

    @Override
    public int compareTo(Article_Slim o) {
        return (int) (o.getTimeUpdated() - time_updated);
    }
}
