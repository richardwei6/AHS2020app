package com.hsappdev.ahs;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Article implements Parcelable {

    private final String ID;
    private final long time_updated;
    private final String title;
    private final String author;
    private final String story;
    private final String [] imagePaths;
    private final String [] videoIDS;

    private final Type type;


    public Article(
            @NonNull
            String ID,
            long time_updated,
            @NonNull
            String title,
            @NonNull
            String author,
            @NonNull
            String story,
            String[] imagePaths,
            String[] videoIDS,
            Type type
    )
    {
        this.ID = ID;
        this.time_updated = time_updated;
        this.title = title;
        this.author = author;
        this.story = story;
        this.imagePaths = imagePaths;
        this.videoIDS = videoIDS;
        this.type = type;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
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
    public String getAuthor() {return author;}
    public String getStory()
    {
        return story;
    }
    public String[] getImagePaths()
    {
        return imagePaths;
    }
    public String[] getVideoIDS() {return videoIDS;}

    public Type getType() { return type;}

    @NonNull
    @Override
    public String toString()
    {
        return "ID::\t" + ID + "\n" +
        "time::\t" + time_updated + "\n" +
        "title::\t" + title + "\n" +
        "author::\t" + author + "\n" +
        "story::\t" + ((story.length() > 40) ? story.substring(0,40) : story) + "\n" + // so output might not be overly long
        "type::\t" + type.toString();
    }

    // The following methods are for the purpose of extending Parcelable
    // make sure to update methods should a new field be added
    protected Article(Parcel in) {
        ID = in.readString();
        time_updated = in.readLong();
        title = in.readString();
        author = in.readString();
        story = in.readString();
        imagePaths = in.createStringArray();
        videoIDS = in.createStringArray();
        type = (Type) in.readSerializable();
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
        dest.writeString(author);
        dest.writeString(story);
        dest.writeStringArray(imagePaths);
        dest.writeStringArray(videoIDS);
        dest.writeSerializable(type);
    }

    /**
     * Be extremely!!! careful when changing these types! May cause problems with type conversion in ArticleDatabase
     * Need to be careful even when refactoring
     */
    public enum Type {
        GENERAL_INFO("General Info", 3), DISTRICT("District", 2),  ASB("ASB", 1),;
        private final String name;
        private final int numCode;
        Type(String name, int numCode){
            this.name =  name;
            this.numCode = numCode;
        }
        public String getName() {
            return name;
        }

        public int getNumCode() {
            return numCode;
        }
        public static Type getTypeFromNumCode(int numCode) {
            for(Type type:  values()) {
                if(type.getNumCode() == numCode)
                    return type;
            }
            return null;
        }
    }
}
