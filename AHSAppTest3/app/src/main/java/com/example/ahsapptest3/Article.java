package com.example.ahsapptest3;
import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private int ID;
    private long time_updated;
    private String title, author, story;
    private String [] imagePaths;
    private boolean is_bookmarked, notified;

    private boolean blank; // false

    public Article(
            int ID,
            long time_updated,
            String title,
            String author,
            String story,
            String[] imagePaths,

            // determined by current articles on phone.
            // false by default
            // (this must be determined on creation, creating overloaded constructor is too unwieldy)
            boolean is_bookmarked,

            // determined by current articles on phone.
            // false by default
            // for notification page only
            boolean has_notified
    )
    {
        this.ID = ID;
        this.time_updated = time_updated;
        this.title = title;
        this.author = author;
        this.story = story;
        this.imagePaths = imagePaths;
        this.is_bookmarked = is_bookmarked;
        this.notified = has_notified;
    }

    public Article() //blank template, used to construct invisible articles for the purpose of getting the proper view height
    {
        blank = true;
    }

    public boolean isBlank()
    {
        return blank;
    }
    public void swapBookmark()
    {
        is_bookmarked = !is_bookmarked;
    }
    public long getTimeUpdated()
    {
        return time_updated;
    }
    public String getAuthor() {return author;}
    public String getTitle()
    {
        return title;
    }
    public String getStory()
    {
        return story;
    }
    public String[] getImagePaths()
    {
        return imagePaths;
    }
    public boolean isBookmarked()
    {
        return is_bookmarked;
    }
    public boolean alreadyNotified() {return notified;}

    @Override
    public String toString()
    {
        String returner =
                "ID::\t" + ID + "\n" +
                "time::\t" + time_updated + "\n" +
                "title::\t" + title + "\n" +
                "author::\t" + author + "\n" +
                "story::\t" + ((story.length() > 40) ? story.substring(0,40) : story) + "\n" + // so output might not be overly long
                "bookmarked?\t" + is_bookmarked + "\n" +
                "notified?\t" + notified
                ;
        return returner;
    }

    // The following methods are for the purpose of extending Parcelable
    // make sure to update methods should a new field be added
    protected Article(Parcel in) {
        ID = in.readInt();
        time_updated = in.readLong();
        title = in.readString();
        author = in.readString();
        story = in.readString();
        imagePaths = in.createStringArray();
        is_bookmarked = in.readByte() != 0;
        notified = in.readByte() != 0;
        blank = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeLong(time_updated);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(story);
        dest.writeStringArray(imagePaths);
        dest.writeByte((byte) (is_bookmarked ? 1 : 0));
        dest.writeByte((byte) (notified ? 1 : 0));
        dest.writeByte((byte) (blank ? 1 : 0));
    }
}
