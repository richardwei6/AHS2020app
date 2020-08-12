package com.example.ahsapptest3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

// important note: if there is a weird error (column not found when it exists, for ex) , try clearing all storage on the device/ emulator
// especially when you change the columns somehow in the database
// By Alex Dang

public class ArticleDatabase extends SQLiteOpenHelper {

    /*private static final String TAG = "ArticleDatabase";*/

    private static ArticleDatabase mInstance;
    public static ArticleDatabase getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new ArticleDatabase(context.getApplicationContext());
        }
        return mInstance;
    }

    private static final String ART_ID = "ART_ID";
    static final int ID_COL = 1;
    private static final String TIME = "TIME";
    static final int TIME_COL = 2;
    private static final String TITLE = "TITLE";
    static final int TITLE_COL = 3;
    private static final String AUTHOR = "AUTHOR";
    static final int AUTHOR_COL = 4;
    private static final String STORY = "STORY";
    static final int STORY_COL = 5;
    private static final String IPATHS = "IPATHS";
    static final int IPATHS_COL = 6;
    private static final String V_IDS = "V_IDS";
    static final int V_IDS_COL = 7;
    private static final String TYPE = "TYPE";
    static final int TYPE_COL = 8;

    private static final String CURRENT_ART_TABLE = "current_table"; // current articles stored locally

    private final String current_Table;

    private ArticleDatabase(@Nullable Context context) {
        super(context, CURRENT_ART_TABLE, null,1);
        current_Table = CURRENT_ART_TABLE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + current_Table +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ART_ID + " TEXT," +
                        TIME + " INTEGER," + // "INTEGER" different from ints, no overflow despite time being long
                        TITLE + " TEXT," +
                        AUTHOR + " TEXT," +
                        STORY + " TEXT," +
                        IPATHS + " TEXT," +
                        V_IDS + " TEXT," +
                        TYPE + " INTEGER);"
        ;
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + current_Table);
        onCreate(db);
    }

    /**
     * Adds a new article to the database
     * @param articles Article
     * @return whether it added successfully to the database
     */
    public boolean add(Article... articles)
    {
        boolean succeeded = true;

        SQLiteDatabase db = this.getWritableDatabase();
        for(Article article: articles)
        {
            ContentValues values = new ContentValues();

            values.put(ART_ID, article.getID());
            values.put(TIME, article.getTimeUpdated());
            values.put(TITLE, article.getTitle());
            values.put(AUTHOR, article.getAuthor());
            values.put(STORY, article.getStory());
            values.put(IPATHS, convertArrayToString(article.getImagePaths()));
            values.put(V_IDS, convertArrayToString(article.getVideoIDS()));
            values.put(TYPE, article.getType().getNumCode());

            long result = db.insert(current_Table, null, values);
            succeeded = succeeded && (result != -1);
        }

        // if inserted incorrectly -1 is the return value
        return succeeded;
    }

    /**
     * Removes the specified article from the database based on id
     * @param article   The article to be deleted
     */
    /*public void delete(Article article)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + current_Table + " WHERE "
                + ART_ID + " = '" + article.getID() + "'";
        db.execSQL(query);
    }*/

    /**
     * Deletes all articles from the database while leaving the database intact
     */
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + current_Table;
        db.execSQL(query);
    }

    /**
     * Self Explanatory, but note if for whatever reason there are duplicate ids, it returns the first article only
     * @param ID the id of the article to search for
     * @return null if Article not found, Article if it is found
     */
    @Nullable
    public Article getArticleById(String ID)
    {
        String query = "SELECT * FROM " + current_Table
                + " WHERE  " + ART_ID + " = '" + ID + "'";
        Cursor data = this.getWritableDatabase().rawQuery(query, null);

        /*Log.d(TAG, DatabaseUtils.dumpCursorToString(data));*/
        if(data.getCount()<1)
            return null;
        data.moveToFirst();

        Article article = new Article(
                data.getString(ID_COL),
                data.getLong(TIME_COL),
                data.getString(TITLE_COL),
                data.getString(AUTHOR_COL),
                data.getString(STORY_COL),
                convertStringToArray(data.getString(IPATHS_COL)),
                convertStringToArray(data.getString(V_IDS_COL)),
                Article.Type.getTypeFromNumCode(data.getInt(TYPE_COL))
        );
        data.close();

        return article;
    }

    /**
     * Returns all the data in the database in the form of article objects
     * @return an arraylist of articles
     */
    /*public ArrayList<Article> getAllArticles()
    {
        String query = "SELECT * FROM " + current_Table;
        Cursor data = this.getWritableDatabase().rawQuery(query, null);
        ArrayList<Article> articles = new ArrayList<>();
        while(data.moveToNext())
        {
            articles.add(new Article(
                    data.getString(ID_COL),
                    data.getLong(TIME_COL),
                    data.getString(TITLE_COL),
                    data.getString(AUTHOR_COL),
                    data.getString(STORY_COL),
                    convertStringToArray(data.getString(IPATHS_COL)),
                    convertStringToArray(data.getString(V_IDS_COL)),
                    Article.Type.getTypeFromNumCode(data.getInt(TYPE_COL))
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
    public void updateArticles(Article... articles){
        /*ArrayList<Article> oldArticles = getAllArticles();
        Log.d(TAG, "oldArticles");
        for(Article article: oldArticles)
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
        deleteAll();
        // here, you would make a copy of any local instance fields
        // in this case, there are none
        add(articles);
        /*Log.d(TAG, "new Articles");
        for(Article article: getAllArticles())
            Log.d(TAG, article.getID() + "\t" + article.getTitle());*/
    }

    /**
     * searches bookmark database if an article is already bookmarked by ID
     * @param id: article ID
     * @return true if bookmark already exists
     */
    /*public boolean alreadyAdded(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "+ ART_ID +" FROM " + current_Table + " WHERE  " + ART_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        boolean alreadyAdded = cursor.getCount() > 0;
        cursor.close();
        return alreadyAdded;
    }*/

    /**
     * https://stackoverflow.com/questions/9053685/android-sqlite-saving-string-array
     * A temp soln better to muck about with JSON: (did that already)
     * https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases
     * @param str
     * @return
     */
    private static final String JSON_str = "iPaths";
    private static String convertArrayToString(String[] array){
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(new ArrayList<>(Arrays.asList(array)));
        try {
            json.put(JSON_str,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
    private static String[] convertStringToArray(String str){
        JSONObject json = null;
        try {
            json = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = json.optJSONArray(JSON_str);
        String[] strArr = new String[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++)
            strArr[i] = jsonArray.optString(i);

        return strArr;
    }
}
