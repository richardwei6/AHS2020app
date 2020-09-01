package com.hsappdev.ahs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hsappdev.ahs.homePage_News.CategoryRecyclerAdapter;
import com.hsappdev.ahs.homePage_News.FeaturedRecyclerAdapter;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /*private static final String TAG = "NewsRecyclerAdapter";*/

    private final ArrayList<Article_Slim> featuredArticles = new ArrayList<>();
    private final ArrayList<ArrayList<Article_Slim>> articles = new ArrayList<>();
    private final String[] titles;
    private final ArticleNavigation articleNavigation;

    public NewsRecyclerAdapter(String[] titles, ArticleNavigation articleNavigation){
        this.titles = titles;
        this.articleNavigation = articleNavigation;
        for(int i = 0; i < titles.length - 1; i++)
            articles.add(new ArrayList<Article_Slim>());
    }

    public void setFeaturedArticles(ArrayList<Article_Slim> articles) {
        featuredArticles.clear();
        featuredArticles.addAll(articles);
        notifyItemChanged(0);
    }

    public void setCategoryArticlesAtPosition(ArrayList<Article_Slim> articles, int position) {
        if(position < this.articles.size()) {
            this.articles.get(position).clear();
            this.articles.get(position).addAll(articles);
            notifyItemChanged(position + 1);
        }
    }

    private static final int FEATURED = 0;
    private static final int CATEGORY = 1;
    @Override
    public int getItemViewType(int position) {
        /*if(featuredArticles.size() == 0 && articles.size() == 0)
            return LOADING;*/
        if(position == 0)
            return FEATURED;
        return CATEGORY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_template, parent, false);
        if(viewType == FEATURED) {
            return new FeaturedViewHolder(view, articleNavigation);
        }
        else {
            return new CategoryViewHolder(view, articleNavigation);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0)
            ((FeaturedViewHolder)holder).setDetails(titles[position], featuredArticles);
        else
            ((CategoryViewHolder)holder).setDetails(titles[position], articles.get(position - 1));

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{
        final TextView titleText;
        final ViewPager2 viewPager;
        final TabLayout tabLayout;
        private final ArticleNavigation articleNavigation;

        public FeaturedViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager2);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
            this.articleNavigation = articleNavigation;
        }

        public void setDetails(String title, ArrayList<Article_Slim> articles) {
            /*Log.d("NewsFeatured", "Setting details");*/
            titleText.setText(title);
            FeaturedRecyclerAdapter adapter = new FeaturedRecyclerAdapter(articleNavigation);

            viewPager.setAdapter(
                    adapter
            );
            adapter.clearAll();
            adapter.addArticles(articles);
            new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            }).attach();

        }
        /*public void addArticles(ArrayList<Article_Slim> articles) {
            if(adapter != null) {
                adapter.addArticles(articles);
            }
        }
        public void clearAll() {
            if(adapter != null)
                adapter.clearAll();
        }*/
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        final TextView titleText;
        final ViewPager2 viewPager;
        final TabLayout tabLayout;
        private final ArticleNavigation articleNavigation;

        public CategoryViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager2);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
            this.articleNavigation = articleNavigation;

        }

        public void setDetails(String title, ArrayList<Article_Slim> articles) {
            titleText.setText(title);
            CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(articleNavigation);
            viewPager.setAdapter(adapter);
            adapter.clearAll();
            adapter.addArticles(articles);
            new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            }).attach();

        }
        /*public void addArticles(ArrayList<Article_Slim> articles) {
            if(adapter != null)
                adapter.addArticles(articles);
        }
        public void clearAll() {
            if(adapter != null) adapter.clearAll();
        }*/

    }
}
