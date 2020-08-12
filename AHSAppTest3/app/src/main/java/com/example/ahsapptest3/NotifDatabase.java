package com.example.ahsapptest3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotifDatabase extends SQLiteOpenHelper {

    /*private static final String TAG = "NotifDatabase";*/
    private static final String current_Table = "NotifDatabase"; //may change later

    private static final String NOTIF_ID = "NOTIF_ID";
    static final int NOTIF_ID_COL = 1;
    private static final String TIME = "TIME";
    static final int TIME_COL = 2;
    private static final String TITLE = "TITLE";
    static final int TITLE_COL = 3;
    private static final String BODY = "BODY";
    static final int BODY_COL = 4;
    private static final String CAT = "CAT";
    static final int CAT_COL = 5;
    private static final String ART_ID = "ART_ID";
    static final int ART_ID_COL = 6;
    private static final String NOTIFIED = "NOTIFIED";
    static final int NOTIFIED_COL = 7;

    private static NotifDatabase thisDatabase;
    public static NotifDatabase getInstance(Context context){
        if(thisDatabase == null)
            thisDatabase = new NotifDatabase(context.getApplicationContext());
        return thisDatabase;
    }

    private NotifDatabase(@Nullable Context context)
    {
        super(context, current_Table, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + current_Table +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTIF_ID + " TEXT," +
                TIME + " INTEGER," +
                TITLE + " TEXT," +
                BODY + " TEXT," +
                CAT + " INTEGER," +
                ART_ID + " TEXT," +
                NOTIFIED + " INTEGER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + current_Table);
        onCreate(db);
    }

    public boolean add(Notif_Data... datas)
    {
        boolean succeeded = true;
        for(Notif_Data data: datas) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NOTIF_ID, data.getID());
            values.put(TIME, data.getTime());
            values.put(TITLE, data.getTitle());
            values.put(BODY, data.getBody());
            values.put(CAT, data.getCategory());
            values.put(ART_ID, data.getArticle_ID());
            values.put(NOTIFIED, (data.isNotified() ? 1 : 0));
            long result = db.insert(current_Table, null, values);
            succeeded = succeeded && (result != -1);
        }

        // if inserted incorrectly -1 is the return value
        return succeeded;
    }

    public boolean alreadyAdded(String ID)
    {
        String selectQuery = "SELECT "+ NOTIF_ID +" FROM " + current_Table + " WHERE  " + NOTIF_ID + " = '" + ID + "'";
        Cursor cursor = this.getWritableDatabase().rawQuery(selectQuery,null);
        boolean alreadyAdded = cursor.getCount() > 0;
        cursor.close();
        return alreadyAdded;
    }
/*
    public ArrayList<Notif_Data> getAllData()
    {
        String query = "SELECT * FROM " + current_Table;
        Cursor data = this.getWritableDatabase().rawQuery(query, null);
        ArrayList<Notif_Data> notif_data = new ArrayList<>();
        while(data.moveToNext())
        {
            notif_data.add(new Notif_Data(
                    data.getString(NOTIF_ID_COL),
                    data.getLong(TIME_COL),
                    data.getString(TITLE_COL),
                    data.getString(BODY_COL),
                    data.getInt(CAT_COL),
                    data.getString(ART_ID_COL),
                    data.getInt(NOTIFIED_COL) == 1
            ));
        }
        data.close();
        return notif_data;
    }*/

    public boolean getReadStatusByID(String ID)
    {
        String query = "SELECT "+ NOTIFIED + " FROM " + current_Table
                + " WHERE  " + NOTIF_ID + " = '" + ID + "'";
        Cursor data = this.getWritableDatabase().rawQuery(query, null);

        if(data.getCount()<1) // not found
            return false;
        data.moveToFirst();

        boolean read = data.getInt(0) == 1;
        data.close();
        return read;
    }
/*
    public boolean getReadStatusBy_ArticleID(String articleID){
        String query = "SELECT "+ NOTIFIED + " FROM " + current_Table
                + " WHERE  " + ART_ID + " = '" + articleID + "'";
        Cursor data = this.getWritableDatabase().rawQuery(query, null);

        if(data.getCount()<1) // not found
            return false;
        data.moveToFirst();

        boolean read = data.getInt(0) == 1;
        data.close();
        return read;
    }*/

    public void updateReadStatus(Notif_Data data)
    {
        String query = "UPDATE " + current_Table
                + " SET " + NOTIFIED + " = '" + (data.isNotified() ? 1 : 0)
                + "' WHERE " + NOTIF_ID + " = '" + data.getID() + "'";
        this.getWritableDatabase().execSQL(query);
    }
/*
    public void delete(Notif_Data data)
    {
        String query =
                "DELETE FROM " + current_Table +
                        " WHERE " + NOTIF_ID + " = '" + data.getID() +"'";
        this.getWritableDatabase().execSQL(query);
    }*/

    public void deleteAll()
    {
        String query =
                "DELETE FROM " + current_Table;
        this.getWritableDatabase().execSQL(query);
    }

    public void updateData(Notif_Data... data){
        /*ArrayList<Notif_Data> oldArticles = getAllData();
        Log.d(TAG, "~~~~~~~~~~~~~~ oldArticles ~~~~~~~~~~~~~");
        for(Notif_Data article: oldArticles)
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
        deleteAll();
        add(data);
        /*ArrayList<Notif_Data> newArticles = getAllData();
        Log.d(TAG, "~~~~~~~~~~~~~~ newArticles ~~~~~~~~~~~~~");
        for(Notif_Data article: newArticles)
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
    }
}
