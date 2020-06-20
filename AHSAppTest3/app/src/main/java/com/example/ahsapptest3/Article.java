package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Article implements Parcelable {

    private long time_updated;
    private boolean is_bookmarked, notified;
    private String author, title, summary, imagePath, story;

    private boolean blank; // false

    public Article(Date time_updated, String title, String story, String imagePath, boolean is_bookmarked)
    {
        this.time_updated = time_updated.getTime();
        this.author = "Name Name"; //TODO: deprecate this constructor so we can actually customize the author name
        this.title = title;
        this.story = story;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
    }

    public Article(Date time_updated, String title, String story, String imagePath, boolean is_bookmarked, boolean notified)
    {
        this.time_updated = time_updated.getTime();
        this.author = "Name Name"; //TODO: delete all other overloaded constructors
        this.title = title;
        this.story = story;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
        this.notified = notified;
    }

    public Article(Date time_updated, String title, String story, String imagePath, Boolean is_bookmarked, String summary)
    {
        this.time_updated = time_updated.getTime();
        this.author = "Author";
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

    public Article(Date time_updated, String author, String title, String story, String imagePath, Boolean is_bookmarked)
    {
        this.time_updated = time_updated.getTime();
        this.author = author;
        this.title = title;
        this.story = story;
        this.imagePath = imagePath;
        this.is_bookmarked = is_bookmarked;
        //TODO: use this constructor instead
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
        author = in.readString();
        story = in.readString();
        imagePath = in.readString();
        notified = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time_updated);
        dest.writeByte((byte) (is_bookmarked ? 1 : 0));
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(story);
        dest.writeString(imagePath);
        dest.writeByte((byte) (notified ? 1 : 0));
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

    public String getAuthor(){return author;}

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

    public boolean alreadyNotified() {return notified;}

    public void setTime_Hours_UpdatedText_toView(TextView view)
    {
        Date currentTime = new Date();
        long time_difference = currentTime.getTime()-this.time_updated;
        int hours = (int) Math.round((time_difference/(3600000.0)));
        view.setText(view.getContext().getString(R.string.time_hours_updated_placeholder, hours));
    }

    public void setTime_Minutes_UpdatedText_toView(TextView view)
    {
        Date currentTime = new Date();
        long time_difference = currentTime.getTime()-this.time_updated;
        int hours = (int) Math.round((time_difference/(60000.0)));
        view.setText(view.getContext().getString(R.string.time_minutes_updated_placeholder, hours));
    }

    public void setDateText_toView(TextView view)
    {
        String dateString = new SimpleDateFormat("MMMM dd, yyyy").format(time_updated);
        view.setText(dateString);
    }

    public void setAuthorText_toView(TextView view){view.setText(view.getContext().getString(R.string.author_placeholder, author));}

    public void setTitleText_toView(TextView view)
    {
        view.setText(title);
    }

    public void setStoryText_toView(TextView view)
    {
        view.setText(story);
    }
    // TODO: fix this mess and distinguish between story and summary

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

    public void setArticleListener_toView(View view, final Context context)
    {
        final Article thisArticle = this;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notified = false;

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("data", thisArticle);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public String toString()
    {
        return title;
    }
}
