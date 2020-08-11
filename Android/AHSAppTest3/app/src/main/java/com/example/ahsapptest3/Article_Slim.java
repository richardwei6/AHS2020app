package com.example.ahsapptest3;
import android.os.Parcel;
import android.os.Parcelable;

public class Article_Slim implements Parcelable {

    private String ID;
    private long time_updated;
    private String title, story;
    private String imagePath;

    private Article.Type type;


    public Article_Slim(
            String ID,
            long time_updated,
            String title,
            String story,
            String imagePath,
            Article.Type type
    )
    {
        this.ID = ID;
        this.time_updated = time_updated;
        this.title = title;
        this.story = (story.length() > 100) ? story.substring(0, 100) : story;
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

    @Override
    public String toString()
    {
        String returner =
                "ID::\t" + ID + "\n" +
                "time::\t" + time_updated + "\n" +
                "title::\t" + title + "\n" +
                "story::\t" + ((story.length() > 40) ? story.substring(0,40) : story) + "\n" + // so output might not be overly long
                "type::\t" + type.toString()
                ;
        return returner;
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
}
