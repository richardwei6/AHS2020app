package com.example.ahsapptest3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.util.ArrayList;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>
{
    private static final String TAG = "ArticleRecyclerAdapter";

    public static ArrayList<Article> articles; // ArrayList instead of array in case we want to add or remove stuff (which will happen!)
    private android.content.Context context;
    private OnItemClick onItemClick;

    public ArticleRecyclerAdapter(android.content.Context context, ArrayList<Article> articles, OnItemClick onItemClick) {
        ArticleRecyclerAdapter.articles = new ArrayList<>(articles);
        this.context = context;
        this.onItemClick = onItemClick;
    }

    public void clearAll()
    {
        articles.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_article_template, parent, false);
        return new ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // change this later, just for sample

        final Article article = articles.get(position);
        Helper.setTimeText_toView(holder.time_updated,
                Helper.TimeFromNow(article.getTimeUpdated())
        );

        holder.titleText.setText(article.getTitle());

        Helper.setHtmlParsedText_toView(holder.summaryText, article.getStory());
        // setImage
        Helper.setImageFromUrl(holder.image,article.getImagePaths()[0], false);


/*
        // must use custom method rather than using Helper as we need to notifyDataSetChanged()
        final ArticleDatabase articleDatabase = new ArticleDatabase(this.context, ArticleDatabase.Option.BOOKMARK);

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.swapBookmark();
                Helper.setBookmarked_toView(bookmarkButton,article.isBookmarked());
                if(!articleDatabase.alreadyAdded(article.getID()))
                    articleDatabase.add(article);
                else
                    {
                        articleDatabase.delete(article);
                    articles.remove(position);
                    notifyItemRemoved(position);
                    ArticleDatabase.setBookmarkChanged();}
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView time_updated, titleText, summaryText;
        ImageView image;
        OnItemClick onItemClick;
        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            time_updated = itemView.findViewById(R.id.article_display__time_updated_Text);
            titleText = itemView.findViewById(R.id.article_display__title_Text);
            summaryText = itemView.findViewById(R.id.article_display__summary_Text);
            image = itemView.findViewById(R.id.article_display__imageView);

            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);
            summaryText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(articles.get(getAdapterPosition()));
        }
    }
    public interface OnItemClick
    {
        void onClick(Article data);
    }
}
