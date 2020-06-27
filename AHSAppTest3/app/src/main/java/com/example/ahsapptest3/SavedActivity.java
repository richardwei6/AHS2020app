package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahsapptest3.Helper_Code.Helper;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SavedActivity extends AppCompatActivity {

    private static final String TAG = "SavedActivity";
    BookmarkHandler bookmarkHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_layout);

        RecyclerView recyclerView = findViewById(R.id.saved_recyclerView);
        bookmarkHandler = new BookmarkHandler(this);

        ArticleRecyclerAdapter adapter= new ArticleRecyclerAdapter(this, getSavedArticles());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public ArrayList<Article> getSavedArticles()
    {
        /*Cursor data = bookmarkHandler.getAllData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext())
        {
            listData.add(data.getString(1));
        }

        ArrayList<Article> articles = new ArrayList<>();
        for(int i = 0; i < listData.size(); i++)
            articles.add(
                new Article(
                    1000,
                        202347384,
                        listData.get(i),
                        "Alex Dang",
                        "story story story story",
                        new String[] {"https://i1.sndcdn.com/artworks-UEyYbq12Fo3y4yoR-1rMSdQ-t500x500.jpg"},
                        true,
                        true

                )
            );
*/
        return bookmarkHandler.getAllArticles();
    }

    public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>
    {
        private static final String TAG = "ArticleRecyclerAdapter";

        private ArrayList<Article> articles; // ArrayList instead of array in case we want to add or remove stuff (which will happen!)
        private android.content.Context context;

        public ArticleRecyclerAdapter(android.content.Context context, ArrayList<Article> articles) {
            this.articles = articles;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template__stacked_article_display, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // change this later, just for sample

            LinearLayout parentLayout = holder.parentLayout;
            ViewStub stub = new ViewStub(context);
            stub.setLayoutResource(R.layout.template__article_display);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            parentLayout.addView(stub, params);
            
            View inflated = stub.inflate();

            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
            inflated.setBackgroundResource(outValue.resourceId);

            Helper.setArticleListener_toView(inflated, articles.get(position));

            Helper.setTimeText_toView((TextView) inflated.findViewById(R.id.article_display__time_updated_Text),
                    Helper.TimeFromNow(articles.get(position).getTimeUpdated()),
                    TimeUnit.HOURS);

            // set title
            Helper.setText_toView((TextView) inflated.findViewById(R.id.article_display__title_Text),articles.get(position).getTitle());

            // set summary/description
            Helper.setText_toView((TextView) inflated.findViewById(R.id.article_display__summary_Text), articles.get(position).getStory());

            // setImage
            Helper.setImage_toView_fromUrl((ImageView) inflated.findViewById(R.id.article_display__imageView),articles.get(position).getImagePaths()[0]);

            // set bookmarked button state
            final ImageButton bookmarkButton = inflated.findViewById(R.id.article_display__bookmarked_button);

            Helper.setBookmarked_toView(bookmarkButton,articles.get(position).isBookmarked());
            Helper.setBookMarkListener_toView(bookmarkButton, articles.get(position));
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public LinearLayout parentLayout;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                parentLayout = itemView.findViewById(R.id.article_stacked_LinearLayout);
            }
        }
    }
}
