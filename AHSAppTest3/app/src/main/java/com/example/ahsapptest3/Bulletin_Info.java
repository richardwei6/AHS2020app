package com.example.ahsapptest3;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Bulletin_Info implements Parcelable {
    private long time;
    private String title, body;
    private Bulletin.Type type;
    private int pos;


    /**
     * for use with SortedList in Bulletin Activity and related comparisons only
     * @param time
     * @param title
     * @param body
     * @param type
     * @param pos
     */
    public Bulletin_Info(long time, String title, String body, Bulletin.Type type, int pos)
    {
        this.time = time;
        this.title = title;
        this.body = body;
        this.type = type;
        this.pos = pos;
    }

    public int getPos()
    {
        return pos;
    }

    protected Bulletin_Info(Parcel in) {
        time = in.readLong();
        title = in.readString();
        body = in.readString();
        type = (Bulletin.Type) in.readSerializable();
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


    public String getBodyText()
    {
        return body;
    }


    public Bulletin.Type getType() {
        return type;
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

    @Override
    public String toString()
    {
        return "Title:\t" + title;
    }
}
