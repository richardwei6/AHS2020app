package com.example.ahsapptest3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bulletin_Info implements Parcelable {
    private long time;
    private String title, body;
    private Date Datetime;
    private String dateString;
    private BulletinActivity.Type type;

    public Bulletin_Info(String title, String dateString, String bodyText, BulletinActivity.Type type)
    {
        this.title = title;
        this.dateString = dateString;
        this.body = bodyText;
        this.type = type;
        SimpleDateFormat dateStringFormat = new SimpleDateFormat("dd:mm:yy");
        try {
            this.Datetime = dateStringFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Bulletin_Info(long time, String title, String body, BulletinActivity.Type type)
    {
        this.time = time;
        this.title = title;
        this.body = body;
        this.type = type;
    }

    protected Bulletin_Info(Parcel in) {
        time = in.readLong();
        title = in.readString();
        body = in.readString();
        type = (BulletinActivity.Type) in.readSerializable();
    }

    public static final Creator<Bulletin_Info> CREATOR = new Creator<Bulletin_Info>() {
        @Override
        public Bulletin_Info createFromParcel(Parcel in) {
            return new Bulletin_Info(in);
        }

        @Override
        public Bulletin_Info[] newArray(int size) {
            return new Bulletin_Info[size];
        }
    };

    public long getTime() { return time; }

    public String getTitle()
    {
        return title;
    }

    public String getTimeString()
    {
        return dateString;
    }

    public String getBodyText()
    {
        return body;
    }

    public Date getDateTime()
    {
        return Datetime;
    }

    public BulletinActivity.Type getType() {
        return type;
    }

    public void setTitle_toView(TextView view)
    {
        view.setText(this.title);
    }

    public void setBodyText_toView(TextView view)
    {
        view.setText(this.body);
    }

    public void setDate_toView(TextView view)
    {
        view.setText(this.dateString);
    }

    public void setImageType_toView(ImageView view)
    {
        int imageID;
        switch(getType())
        {
            case SENIORS:
                imageID = R.drawable.bulletin_seniors;
                break;
            case EVENTS:
                imageID = R.drawable.bulletin_events;
                break;
            case COLLEGES:
                imageID = R.drawable.bulletin_colleges;
                break;
            case REFERENCE:
                imageID = R.drawable.bulletin_reference;
                break;
            case ATHLETICS:
                imageID = R.drawable.bulletin_athletics;
                break;
            default:
                imageID = R.drawable.bulletin_others;
                break;
        }
        view.setImageResource(imageID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeSerializable(type);
    }
}
