package com.hsappdev.ahs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.HomePage_News.Article_Stacked_PagerAdapter;
import com.hsappdev.ahs.HomePage_News.FeaturedArticle_PagerAdapter;
import com.hsappdev.ahs.Misc.EnhancedWrapContentViewPager;
import com.hsappdev.ahs.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /*private static final String TAG = "NewsRecyclerAdapter";*/

    final ArrayList<Article_Slim> featuredArticles = new ArrayList<>();
    final ArrayList<ArrayList<Article_Slim>> articles = new ArrayList<>();
    private final String[] titles;
    private final FragmentManager fragmentManager;
    public NewsRecyclerAdapter(FragmentManager fragmentManager, String[] titles){
        this.fragmentManager = fragmentManager;
        this.titles = titles;
    }

    @Override
    public int getItemViewType(int position) {
        if(featuredArticles.size() == 0 && articles.size() == 0)
            return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parent.setClipChildren(false);
        parent.setClipToPadding(false);
        if(viewType != 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_template, parent, false);
            return new NewsRecyclerAdapter.ViewHolder(view);
        }
        return new LoadingFeaturedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_featured_loading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) != 0) {
            if(position == 0)
                ((NewsRecyclerAdapter.ViewHolder)holder).setDetails(fragmentManager, featuredArticles.toArray(new Article_Slim[0]), titles[position], true);
            else
                ((NewsRecyclerAdapter.ViewHolder)holder).setDetails(fragmentManager, articles.get(position-1).toArray(new Article_Slim[0]), (position < titles.length) ? titles[position] : "", false);
        }

    }

    @Override
    public int getItemCount() {
        return articles.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private static int idgen = 1000000;
        private static final int numStacked = 2;
        final TextView titleText;
        final EnhancedWrapContentViewPager viewPager;
        final TabLayout tabLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
        }

        public void setDetails(FragmentManager fragmentManager, Article_Slim[] articles, String title, boolean featured) {
            titleText.setText(title);
            viewPager.setId(idgen++);
            viewPager.setAdapter(
                    (featured)
                            ? new FeaturedArticle_PagerAdapter(fragmentManager, articles)
                            : new Article_Stacked_PagerAdapter(fragmentManager, articles,numStacked)
            );
            tabLayout.setupWithViewPager(viewPager, true);

        }

    }

    public static class LoadingFeaturedViewHolder extends RecyclerView.ViewHolder {
        public LoadingFeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
