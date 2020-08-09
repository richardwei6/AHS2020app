package com.example.ahsapptest3;
import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private String ID;
    private long time_updated;
    private String title, author, story;
    private String [] imagePaths, videoIDS;

    private Type type;

    private boolean blank; // false

    public Article(
            String ID,
            long time_updated,
            String title,
            String author,
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

    public Article() //blank template, used to construct invisible articles for the purpose of getting the proper view height
    {
        blank = true;
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

    public boolean isBlank()
    {
        return blank;
    }

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

    @Override
    public String toString()
    {
        String returner =
                "ID::\t" + ID + "\n" +
                "time::\t" + time_updated + "\n" +
                "title::\t" + title + "\n" +
                "author::\t" + author + "\n" +
                "story::\t" + ((story.length() > 40) ? story.substring(0,40) : story) + "\n" + // so output might not be overly long
                "type::\t" + type.toString()
                ;
        return returner;
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
        blank = in.readByte() != 0;
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
        dest.writeByte((byte) (blank ? 1 : 0));
    }

    /**
     * Be extremely!!! careful when changing these types! May cause problems with type conversion in ArticleDatabase
     * Need to be careful even when refactoring
     */
    public enum Type {
        ASB("ASB", 1), SPORTS("Sports", 2), DISTRICT("District", 3);
        private String name;
        private int numCode;
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
