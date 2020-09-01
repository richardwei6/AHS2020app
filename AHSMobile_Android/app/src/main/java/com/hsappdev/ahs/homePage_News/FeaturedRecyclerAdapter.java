package com.hsappdev.ahs.homePage_News;

import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.Article;
import com.hsappdev.ahs.ArticleDatabase;
import com.hsappdev.ahs.ArticleNavigation;
import com.hsappdev.ahs.Article_Slim;
import com.hsappdev.ahs.misc.Helper;
import com.hsappdev.ahs.R;

import java.util.ArrayList;
import java.util.Collections;

public class FeaturedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // --Commented out by Inspection (8/31/2020 11:32 AM):private static final String TAG = "FeaturedRecyclerAdapter";
    private final ArrayList<Article_Slim> articles = new ArrayList<>();
    private final ArticleNavigation articleNavigation;
    public FeaturedRecyclerAdapter(ArticleNavigation articleNavigation) {
        this.articleNavigation = articleNavigation;
    }
    public void addArticles(ArrayList<Article_Slim> new_articles) {
        int oldSize = articles.size();
        articles.addAll(new_articles);
        Collections.sort(articles);
        if(oldSize == 0)
            notifyItemChanged(0);
        notifyItemRangeInserted(articles.size() -1, new_articles.size());
    }
    public void clearAll() {
        articles.clear();
        notifyDataSetChanged();
    }

    private static final int LOADING = 0;
    @Override
    public int getItemViewType(int position) {
        if(articles.size() == 0)
            return LOADING;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType > LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_featured_template, parent, false);
            return new FeaturedViewHolder(view, articleNavigation);
        }
        return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_featured_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) > LOADING) {
            ((FeaturedViewHolder) holder).init(articles.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return (articles.size() > 0) ? articles.size() : 1;
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titleText;
        private final TextView typeText;
        private final TextView timeText;
        private final ImageView imageView;
        private final ArticleNavigation articleNavigation;
        final View outerLayout;
        public FeaturedViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_featured__title_Text);
            typeText = itemView.findViewById(R.id.news_featured_typeText);
            timeText = itemView.findViewById(R.id.template_featured__updated_Text);
            imageView = itemView.findViewById(R.id.template_featured__ImageView);
            outerLayout = itemView.findViewById(R.id.featured_outerLayout);
            this.articleNavigation = articleNavigation;
        }
        private Article_Slim article_slim;
        public void init(final Article_Slim article) {
            this.article_slim = article;


            Helper.setTimeText_toView(timeText,
                    Helper.TimeFromNow(article.getTimeUpdated())
            );
            String imagePaths = article.getImagePath();
            if(imagePaths != null && imagePaths.length() > 0)
                Helper.setImageFromUrl_CenterCrop_FullSize(
                        imageView,
                        imagePaths
                );
            else
                imageView.setImageResource(R.drawable.image_bg);
            titleText.setText(article.getTitle());
            typeText.setText(article.getType().getName());

            outerLayout.setOnClickListener(this);
            /*Log.d("Featured", article_slim.getID());*/
            new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                    initArticle(article_slim.getID());
                    /*Log.d(TAG, "initializing article");*/
                }
            }.run();

        }
        private void initArticle(String articleID) {
            article = ArticleDatabase.getInstance(itemView.getContext()).getArticleById(articleID);
        }
        Article article;
        @Override
        public void onClick(View v) {
            if(article != null)
                articleNavigation.onItemClicked(article);
            else {
                initArticle(article_slim.getID());
                if(article != null)
                    articleNavigation.onItemClicked(article);
            }
        }
    }
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
