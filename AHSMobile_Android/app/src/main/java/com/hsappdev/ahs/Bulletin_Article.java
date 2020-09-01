package com.hsappdev.ahs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Bulletin_Article implements Parcelable {
    private final String ID;
    private final long time;
    private final String title;
    private final String body;
    private final Type type;
    private boolean already_read;

    public Bulletin_Article(String ID, long time, String title, String body, Type type, boolean already_read)
    {
        this.ID = ID;
        this.time = time;
        this.title = title;
        this.body = body;
        this.type = type;
        this.already_read = already_read;

    }


    protected Bulletin_Article(Parcel in) {
        ID = in.readString();
        time = in.readLong();
        title = in.readString();
        body = in.readString();
        type = (Type) in.readSerializable();
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

    public static final Creator<Bulletin_Article> CREATOR = new Creator<Bulletin_Article>() {
        @Override
        public Bulletin_Article createFromParcel(Parcel in) {
            return new Bulletin_Article(in);
        }

        @Override
        public Bulletin_Article[] newArray(int size) {
            return new Bulletin_Article[size];
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
    public Type getType() {
        return type;
    }
    public boolean isAlready_read() {
        return already_read;
    }

    public void setAlready_read(boolean already_read) {
        this.already_read = already_read;
    }

    @NonNull
    @Override
    public String toString()
    {
        return "Title:\t" + title;
    }

    public enum Type
    {
        ACADEMICS("Academics", 1), ATHLETICS("Athletics", 2), CLUBS("Clubs", 3),
        COLLEGES("Colleges", 4), REFERENCE("Reference", 5);
        private final String name;
        private final int numCode;
        Type(String name, int numCode) {
            this.name = name;
            this.numCode = numCode;
        }
        public String getName() {
            return name;
        }

        public int getNumCode() {
            return numCode;
        }

        public static Type getTypeFromNumCode(int numCode){
            for(Type type: values()) {
                if(type.getNumCode() == numCode)
                    return type;
            }
            return null;
        }
    }
}
