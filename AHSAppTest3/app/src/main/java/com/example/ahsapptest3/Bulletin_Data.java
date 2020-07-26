package com.example.ahsapptest3;

import android.os.Parcel;
import android.os.Parcelable;

public class Bulletin_Data implements Parcelable {
    private String ID;
    private long time;
    private String title, body;
    private Bulletin.Type type;
    private boolean already_read;

    /**
     * for use with SortedList in Bulletin Activity and related comparisons only
     * @param time
     * @param title
     * @param body
     * @param type
     */
    public Bulletin_Data(String ID, long time, String title, String body, Bulletin.Type type, boolean already_read)
    {
        this.ID = ID;
        this.time = time;
        this.title = title;
        this.body = body;
        this.type = type;
        this.already_read = already_read;

    }


    protected Bulletin_Data(Parcel in) {
        ID = in.readString();
        time = in.readLong();
        title = in.readString();
        body = in.readString();
        type = (Bulletin.Type) in.readSerializable();
        already_read = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeLong(time);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeSerializable(type);
        dest.writeByte((byte) (already_read ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bulletin_Data> CREATOR = new Creator<Bulletin_Data>() {
        @Override
        public Bulletin_Data createFromParcel(Parcel in) {
            return new Bulletin_Data(in);
        }

        @Override
        public Bulletin_Data[] newArray(int size) {
            return new Bulletin_Data[size];
        }
    };

    public String getID() { return ID; }
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
    public boolean isAlready_read() {
        return already_read;
    }

    public void setAlready_read(boolean already_read) {
        this.already_read = already_read;
    }

    @Override
    public String toString()
    {
        return "Title:\t" + title;
    }
}
