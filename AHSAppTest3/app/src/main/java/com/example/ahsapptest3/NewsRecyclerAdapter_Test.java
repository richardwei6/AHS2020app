package com.example.ahsapptest3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ahsapptest3.Helper_Code.EnhancedWrapContentViewPager;
import com.example.ahsapptest3.HomePage_News.Article_Display_Stacked;
import com.example.ahsapptest3.HomePage_News.Article_Stacked_PagerAdapter;
import com.example.ahsapptest3.HomePage_News.FeaturedArticle_PagerAdapter;
import com.example.ahsapptest3.HomePage_News.Featured_Display;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class NewsRecyclerAdapter_Test extends RecyclerView.Adapter<NewsRecyclerAdapter_Test.ViewHolder>{
    private static final String TAG = "NewsRecyclerAdapter";

    ArrayList<Article> featuredArticles;
    ArrayList<ArrayList<Article>> articles;
    private FragmentManager fragmentManager;
    public NewsRecyclerAdapter_Test(FragmentManager fragmentManager,
                                    ArrayList<Article> featuredArticles,
                                    ArrayList<ArrayList<Article>> articles){
        this.fragmentManager = fragmentManager;
        this.featuredArticles = featuredArticles;
        this.articles = articles;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public NewsRecyclerAdapter_Test.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_test_template, parent, false);

        return new NewsRecyclerAdapter_Test.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       /* if(position == 0)
            holder.setDetails(fragmentManager, featuredArticles.toArray(new Article[0]), "Featured", true);
        else
            holder.setDetails(fragmentManager, articles.get(position-1).toArray(new Article[0]), "Regular", false);*/
    }

    @Override
    public int getItemCount() {
        return articles.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private static final int num_stacked = 2;
        TextView titleText;
        ViewPager2 viewPager;
        TabLayout tabLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
        }
/*

        public void setDetails(FragmentManager fragmentManager, final Article[] articles, String title, boolean featured) {
            titleText.setText(title);
            if(featured) {
                viewPager.setAdapter(
                        new FragmentStateAdapter((FragmentActivity) itemView.getContext()) {
                            @NonNull
                            @Override
                            public Fragment createFragment(int position) {
                                return Featured_Display.newInstanceOf(articles[position]);
                            }

                            @Override
                            public int getItemCount() {
                                return articles.length;
                            }
                        }

                );
            }
            else
                viewPager.setAdapter(
                    new FragmentStateAdapter((FragmentActivity) itemView.getContext()) {
                        @NonNull
                        @Override
                        public Fragment createFragment(int position) {
                            if(position < articles.length/num_stacked) {
                                Article[] stacked_info = new Article[num_stacked];
                                for(int i = 0; i < num_stacked; i++)
                                {
                                    int i1 = i + position*num_stacked;
                */
/*if(i1 >= this.articles.length)
                    stacked_info[i] = new Article();
                else*//*

                                    stacked_info[i] = articles[i1];
                                }
                                return Article_Display_Stacked.newInstanceOf(stacked_info);
                            }
                            Article[] stacked_info = new Article[articles.length - position*num_stacked];
                            for(int i = 0; i < articles.length-num_stacked*position; i++) {
                                stacked_info[i] = articles[i + num_stacked*position];
                            }
                            return Article_Display_Stacked.newInstanceOf(stacked_info);
                        }

                        @Override
                        public int getItemCount() {
                            return articles.length/num_stacked+((articles.length%num_stacked == 0) ? 0 : 1);
                        }
                    }

                );
            TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            });
            tabLayoutMediator.attach();
        }
*/

    }

}
