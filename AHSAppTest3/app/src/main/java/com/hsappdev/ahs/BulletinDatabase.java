package com.hsappdev.ahs;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class BulletinDatabase extends SQLiteOpenHelper {

   /* private static final String TAG = "BulletinDatabase";*/
    private static final String current_Table = "BulletinDatabase"; //may change later

    private static final String BUL_ID = "BUL_ID";
    static final int BUL_COL = 1;
    private static final String TIME = "TIME";
    static final int TIME_COL = 2;
    private static final String TITLE = "TITLE";
    static final int TITLE_COL = 3;
    private static final String BODY = "BODY";
    static final int BODY_COL = 4;
    private static final String TYPE = "TYPE";
    static final int TYPE_COL = 5;
    private static final String READ = "READ";
    static final int READ_COL = 6;

    private static BulletinDatabase thisDatabase;
    public static BulletinDatabase getInstance(Context context){
        if(thisDatabase == null)
            thisDatabase = new BulletinDatabase(context.getApplicationContext());
        return thisDatabase;
    }

    private BulletinDatabase(@Nullable Context context)
    {
        super(context, current_Table, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + current_Table +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUL_ID + " TEXT," +
                TIME + " INTEGER," +
                TITLE + " TEXT," +
                BODY + " TEXT," +
                TYPE + " INTEGER," +
                READ + " INTEGER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + current_Table);
        onCreate(db);
    }

    public boolean add(Bulletin_Article... datas)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean succeeded = true;
        try {
            db.beginTransaction();
            for(Bulletin_Article data: datas) {

                ContentValues values = new ContentValues();
                values.put(BUL_ID, data.getID());
                values.put(TIME, data.getTime());
                values.put(TITLE, data.getTitle());
                values.put(BODY, data.getBodyText());
                values.put(TYPE, data.getType().getNumCode());
                values.put(READ, (data.isAlready_read()) ? 1 : 0);

                long result = db.insert(current_Table, null, values);
                succeeded = succeeded && (result != -1);
            }
            if(succeeded)
                db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
        // if inserted incorrectly -1 is the return value
        return succeeded;
    }
/*

    public boolean alreadyAdded(String ID)
    {
        String selectQuery = "SELECT "+ BUL_ID +" FROM " + current_Table + " WHERE  " + BUL_ID + " = '" + ID + "'";
        Cursor cursor = this.getWritableDatabase().rawQuery(selectQuery,null);
        boolean alreadyAdded = cursor.getCount() > 0;
        cursor.close();
        return alreadyAdded;
    }
*/

    @Nullable
    public Bulletin_Article getArticleByID(String ID) {
        String query = "SELECT * FROM " + current_Table
                + " WHERE  " + BUL_ID + " = '" + ID + "'";
        Cursor data = this.getWritableDatabase().rawQuery(query, null);
        if(data.getCount()<1)
            return null;
        data.moveToFirst();

        Bulletin_Article article = new Bulletin_Article(
                data.getString(BUL_COL),
                data.getLong(TIME_COL),
                data.getString(TITLE_COL),
                data.getString(BODY_COL),
                Bulletin_Article.Type.getTypeFromNumCode(data.getInt(TYPE_COL)),
                data.getInt(READ_COL) == 1);
        data.close();
        return article;
    }

    public boolean getReadStatusByID(String ID)
    {
        String query = "SELECT "+ READ + " FROM " + current_Table
                + " WHERE  " + BUL_ID + " = '" + ID + "'";
        Cursor data = this.getWritableDatabase().rawQuery(query, null);
        /*Log.d(TAG, "found:\t" + data.getCount());*/
        if(data.getCount()<1) // not found
            return false;
        data.moveToFirst();

        boolean read = data.getInt(0) == 1;
        /*Log.d(TAG, "read? " + read);*/
        data.close();
        return read;
    }

    public void updateReadStatus(Bulletin_Article data)
    {
        String query = "UPDATE " + current_Table
                + " SET " + READ + " = '" + (data.isAlready_read() ? 1 : 0)
                + "' WHERE " + BUL_ID + " = '" + data.getID() + "'";
        this.getWritableDatabase().execSQL(query);
    }
/*
    public void delete(Bulletin_Article data)
    {
        String query =
                "DELETE FROM " + current_Table +
                        " WHERE " + BUL_ID + " = '" + data.getID() +"'";
        this.getWritableDatabase().execSQL(query);
    }*/

    public void deleteAll()
    {
        String query =
                "DELETE FROM " + current_Table;
        this.getWritableDatabase().execSQL(query);
    }
/*
    public ArrayList<Bulletin_Article> getAllArticles()
    {
        String query = "SELECT * FROM " + current_Table;
        Cursor data = this.getWritableDatabase().rawQuery(query, null);
        ArrayList<Bulletin_Article> articles = new ArrayList<>();
        while(data.moveToNext())
        {
            articles.add(new Bulletin_Article(
                    data.getString(BUL_COL),
                    data.getLong(TIME_COL),
                    data.getString(TITLE_COL),
                    data.getString(BODY_COL),
                    Bulletin_Article.Type.getTypeFromNumCode(data.getInt(TYPE_COL)),
                    data.getInt(READ_COL) == 1
            ));
        }
        data.close();
        return articles;
    }*/

    /**
     * Deletes all articles that are not in the new articles
     * Updates old articles that are still there rather than destroying them
     * A little unnecessary, but it will be if there are any fields determined not by firebase
     * @param articles the articles to replace the old ones
     */
    public void updateArticles(Bulletin_Article... articles) {
        /*ArrayList<Bulletin_Article> oldArticles = getAllArticles();
        Log.d(TAG, "~~~~~~~~~~~~~~ oldArticles ~~~~~~~~~~~~~");
        for(Bulletin_Article article: oldArticles)
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
        deleteAll(); // note not necessary to update notified field because of the special interaction with getReadStatusByID()
        add(articles);
        /*ArrayList<Bulletin_Article> newArticles = getAllArticles();
        Log.d(TAG, "~~~~~~~~~~~~~~ newArticles ~~~~~~~~~~~~~");
        for(Bulletin_Article article: newArticles)
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
    }

    public void updateArticles(ArrayList<Bulletin_Article> articles) {
        updateArticles(articles.toArray(new Bulletin_Article[0]));
    }

    public void updateArticles(DataSnapshot snapshot, Resources resources) {
        final String[] categories = new String[]
                {
                        resources.getString(R.string.fb_bull_academics),
                        resources.getString(R.string.fb_bull_athletics),
                        resources.getString(R.string.fb_bull_clubs),
                        resources.getString(R.string.fb_bull_colleges),
                        resources.getString(R.string.fb_bull_reference),
                };
        final String articleTitle = resources.getString(R.string.fb_bull_artTitle),
                articleBody = resources.getString(R.string.fb_bull_artBody),
                articleTime = resources.getString(R.string.fb_bull_artTime);
        final Bulletin_Article.Type[] types = Bulletin_Article.Type.values();

        final ArrayList<Bulletin_Article> data = new ArrayList<>();
        for(int i = 0; i < categories.length; i++)
        {
            // Log.d(TAG, categories[i]);
            DataSnapshot snapshot1 = snapshot.child(categories[i]);


            for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                String ID = dataSnapshot.getKey();
                if(ID == null)
                    continue;
                String title = dataSnapshot.child(articleTitle).getValue(String.class);
                if(title == null)
                    title = "";
                String body = dataSnapshot.child(articleBody).getValue(String.class);
                if(body == null)
                    body = "";
                Long time_holder = dataSnapshot.child(articleTime).getValue(long.class);
                long time = (time_holder != null) ? time_holder : 0;
                Bulletin_Article info = new Bulletin_Article(ID, time, title, body, types[i],
                        getReadStatusByID(ID)
                );
                data.add(info);

            }
        }
        updateArticles(data);
    }
}
