package com.example.ahsapptest3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.Date;


public class Article implements Parcelable {

    private long time_updated;
    private boolean is_bookmarked;
    private String title, summary, imagePath, story;

    private boolean blank; // false

    public Article(Date time_updated, String title, String story, String imagePath, Boolean is_bookmarked)
    {
        this.time_updated = time_updated.getTime();
        this.title = title;
        this.story = story;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
    }

    public Article(Date time_updated, String title, String story, String imagePath, Boolean is_bookmarked, String summary)
    {
        this.time_updated = time_updated.getTime();
        this.title = title;
        this.story = story;
        this.summary = summary;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
    }

    public Article(long time_updated, String title, String story, String imagePath, Boolean is_bookmarked)
    {
        this.time_updated = time_updated;
        this.title = title;
        this.story = story;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
    }

    public Article(long time_updated, String title, String story, String imagePath, Boolean is_bookmarked, String summary)
    {
        this.time_updated = time_updated;
        this.title = title;
        this.story = story;
        this.summary = summary;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
    }
    
    

    public Article() //blank template
    {
        blank = true;
    }

    public boolean isBlank()
    {
        return blank;
    }

    protected Article(Parcel in) {
        time_updated = in.readLong();
        is_bookmarked = in.readByte() != 0;
        title = in.readString();
        summary = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time_updated);
        dest.writeByte((byte) (is_bookmarked ? 1 : 0));
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
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

    private void swapBookmark()
    {
        is_bookmarked = !is_bookmarked;
    }

    public long getTimeUpdated()
    {
        return time_updated;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSummary()
    {
        return summary;
    }
    
    public String getStory()
    {
        return story;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public boolean isBookmarked()
    {
        return is_bookmarked;
    }

    public void setTimeUpdatedText_toView(TextView view)
    {
        Date currentTime = new Date();
        long time_difference = currentTime.getTime()-this.time_updated;
        int hours = (int) Math.round((time_difference/(3600000.0)));
        view.setText(view.getContext().getString(R.string.time_updated_placeholder, hours));
    }

    public void setTitleText_toView(TextView view)
    {
        view.setText(this.title);
    }

    public void setStoryText_toView(TextView view)
    {
        view.setText(this.story);
    }

    public void setImage_toView(ImageView view)
    {
        //TODO: implement this
        /*Bitmap image;
        try
        {
            image = BitmapFactory.decodeFile(this.imagePath);
        }
        catch(Exception e)
        {

        }*/
    }

    public void setBookmarked_toView(ImageButton view)
    {
        if(is_bookmarked)
        {
            view.setImageDrawable(ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.bookmarked_icon_active, null));
        }
        else
        {
            view.setImageDrawable(ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.bookmarked_icon_inactive, null));
        }
    }

    public void setBookMarkListener_toView(final ImageButton btn)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapBookmark();
                setBookmarked_toView(btn);
            }
        });
    }
}
