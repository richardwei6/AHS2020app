package com.example.ahsapptest3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

// important note: if there is a weird error, try clearing all storage on the device/ emulator
// especially when you change the columns somehow in the database

public class BookmarkHandler extends SQLiteOpenHelper {

    private static final String TAG = "BookmarkHandler";

    private static final String TABLE_NAME = "bookmark_table";
    private static final String COL0 = "ID";

    private static final String COL1 = "ARTICLE_ID";
    static final int ID_COL = 1;
    private static final String COL2 = "TIME";
    static final int TIME_COL = 2;
    private static final String COL3 = "TITLE";
    static final int TITLE_COL = 3;
    private static final String COL4 = "AUTHOR";
    static final int AUTHOR_COL = 4;
    private static final String COL5 = "STORY";
    static final int STORY_COL = 5;
    private static final String COL6 = "IPATHS";
    static final int IPATHS_COL = 6;
    private static final String COL7 = "BMARKED";
    static final int BMARKED_COL = 7;
    private static final String COL8 = "NOTIF";
    static final int NOTIF_COL = 8;


    public BookmarkHandler(@Nullable Context context) {
        super(context, TABLE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_NAME +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL1 + " INTEGER," +
                        COL2 + " INTEGER," + // "INTEGER" different from ints, no overflow despite time being long
                        COL3 + " TEXT," +
                        COL4 + " TEXT," +
                        COL5 + " TEXT," +
                        COL6 + " TEXT," +
                        COL7 + " INTEGER," + // No booleans in sqlite, store them as integers instead (0 or 1)
                        COL8 + " INTEGER);"
        ;
        db.execSQL(createTable);
        System.out.println("created table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Article article)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, article.getID());
        System.out.println("id input");
        contentValues.put(COL2, article.getTimeUpdated());
        System.out.println("title input");
        contentValues.put(COL3, article.getTitle());
        contentValues.put(COL4, article.getAuthor());
        contentValues.put(COL5, article.getStory());
        contentValues.put(COL6, convertArrayToString(article.getImagePaths()));
        contentValues.put(COL7, (article.isBookmarked()) ? 1 : 0);
        contentValues.put(COL8, (article.alreadyNotified()) ? 1 : 0);

        Log.d(TAG, "addData: Adding " + article + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        // if inserted incorrectly -1 is the return value
        if (result == -1)
            return false;
        return true;
    }

    /**
     *  Returns all the data from database
     */

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public ArrayList<Article> getAllArticles()
    {
        Cursor data = getAllData();
        ArrayList<Article> articles = new ArrayList<>();
        while(data.moveToNext())
        {
            articles.add(new Article(
                    data.getInt(ID_COL),
                    data.getLong(TIME_COL),
                    data.getString(TITLE_COL),
                    data.getString(AUTHOR_COL),
                    data.getString(STORY_COL),
                    convertStringToArray(data.getString(IPATHS_COL)),
                    (data.getInt(BMARKED_COL) == 1),
                    (data.getInt(NOTIF_COL) == 1)
            ));
        }
        return articles;
    }

    /**
     * https://stackoverflow.com/questions/9053685/android-sqlite-saving-string-array
     * A temp soln better to muck about with JSON:
     * https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases
     * @param str
     * @return
     */
    private static final String strSeparator = "__,__";
    private static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    private static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
