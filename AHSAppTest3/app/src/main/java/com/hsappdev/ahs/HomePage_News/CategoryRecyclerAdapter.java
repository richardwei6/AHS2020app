package com.hsappdev.ahs.HomePage_News;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.Article;
import com.hsappdev.ahs.ArticleDatabase;
import com.hsappdev.ahs.ArticleNavigation;
import com.hsappdev.ahs.Article_Slim;
import com.hsappdev.ahs.R;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /*private static final String TAG = "CategoryRecyclerAdapter";*/
    private final ArrayList<Article_Slim> articles = new ArrayList<>();
    private ArticleNavigation articleNavigation;
    public CategoryRecyclerAdapter(ArticleNavigation articleNavigation) {
        this.articleNavigation = articleNavigation;
    }
    public void addArticle(Article_Slim article) {
        int oldSize = articles.size();
        articles.add(article);
        Collections.sort(articles);
        if(oldSize == 0)
            notifyItemChanged(0);
        else
            notifyItemInserted(articles.size() -1);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_category_template, parent,false);
            return new ViewHolder(view, articleNavigation);
        }
        return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_category_template, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) > LOADING) {
            if(position < articles.size()/num_stacked) {
                Article_Slim[] articles_stacked = new Article_Slim[num_stacked];
                for(int i = 0; i < num_stacked; i++)
                {
                    int i1 = i + position*num_stacked;
                    articles_stacked[i] = this.articles.get(i1);
                }
                ((ViewHolder)holder).initialize(articles_stacked, false);
            } else {
                Article_Slim[] articles_stacked = new Article_Slim[articles.size() - position*num_stacked];
                if (articles.size() - num_stacked * position >= 0)
                    System.arraycopy(articles.toArray(new Article_Slim[0]), num_stacked * position, articles_stacked, 0, articles.size() - num_stacked * position);
                ((ViewHolder)holder).initialize(articles_stacked, position != 0);
            }
        }
        else
            ((LoadingViewHolder) holder).initialize();
    }

    private static final int num_stacked = 2;
    @Override
    public int getItemCount() {

        return (articles.size() != 0) ? articles.size()/num_stacked+((articles.size()%num_stacked == 0) ? 0 : 1) : 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private ArticleNavigation articleNavigation;
        public ViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.news_category_linearLayout);
            this.articleNavigation = articleNavigation;
        }

        public void initialize(final Article_Slim[] articles, boolean notFull) {
            /*Log.d(TAG, getAdapterPosition() + "" + articles.length);*/
            linearLayout.removeAllViews();
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            for(final Article_Slim article: articles) {
                News_ArticleView articleView = new News_ArticleView(itemView.getContext(), article,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Article article1 = ArticleDatabase.getInstance(itemView.getContext()).getArticleById(article.getID());
                                if(article1 != null)
                                    articleNavigation.onItemClicked(article1);
                            }
                        });
                linearLayout.addView(articleView, params);
                if(notFull) {
                    FrameLayout frameLayout = new FrameLayout(itemView.getContext());
                    View view = View.inflate(itemView.getContext(), R.layout.news_article_loading, frameLayout);
                    view.setVisibility(View.INVISIBLE);
                    linearLayout.addView(frameLayout, params);


                }
            }
        }

    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.news_category_linearLayout);
        }
        public void initialize() {
            for(int i = 0; i < num_stacked; i++)
                View.inflate(itemView.getContext(),R.layout.news_article_loading, linearLayout);
        }
    }
}
