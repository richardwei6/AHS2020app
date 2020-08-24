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
import com.hsappdev.ahs.HomePage_News.CategoryRecyclerAdapter;
import com.hsappdev.ahs.HomePage_News.FeaturedRecyclerAdapter;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /*private static final String TAG = "NewsRecyclerAdapter";*/
/*
    final ArrayList<Article_Slim> featuredArticles = new ArrayList<>();
    final ArrayList<ArrayList<Article_Slim>> articles = new ArrayList<>();*/
    private final String[] titles;
    private ArticleNavigation articleNavigation;

    public NewsRecyclerAdapter(String[] titles, ArticleNavigation articleNavigation){
        this.titles = titles;
        this.articleNavigation = articleNavigation;
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
            ((FeaturedViewHolder)holder).setDetails(titles[position]);
        else
            ((CategoryViewHolder)holder).setDetails(titles[position]);

    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder{
        final TextView titleText;
        final ViewPager2 viewPager;
        final TabLayout tabLayout;
        private ArticleNavigation articleNavigation;
        private FeaturedRecyclerAdapter adapter;

        public FeaturedViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager2);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
            this.articleNavigation = articleNavigation;
        }

        public void setDetails(String title) {
            /*Log.d("NewsFeatured", "Setting details");*/
            titleText.setText(title);
            if(adapter == null) {
                adapter = new FeaturedRecyclerAdapter(articleNavigation);

                viewPager.setAdapter(
                        adapter
                );
                /*Log.d("NewsFeatured", "adapter null");*/
            }

            new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            }).attach();
            /*
            final Resources r = itemView.getContext().getResources();
            final String
                    articleTitle = r.getString(R.string.fb_art_title),
                    articleBody =  r.getString(R.string.fb_art_body),
                    articleImages = r.getString(R.string.fb_art_images),
                    articleTime = r.getString(R.string.fb_art_time),
                    articleFeatured = r.getString(R.string.fb_art_featured);
            final Article.Type [] types = Article.Type.values();
            final String [] cat_ref = new String[] {
                    r.getString(R.string.fb_news_district),
                    r.getString(R.string.fb_news_sports),
                    r.getString(R.string.fb_news_asb),
            };

            FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adapter.clearAll();
                    for(int i = 0; i < types.length; i++) {
                        DataSnapshot cat_sn = snapshot.child(cat_ref[i]);
                        for(DataSnapshot art_sn: cat_sn.getChildren()) {
                            boolean is_featured = (boolean) art_sn.child(articleFeatured).getValue();
                            if(is_featured) {
                                String ID = art_sn.getKey();
                                if(ID == null)
                                    continue;

                                String title = art_sn.child(articleTitle).getValue(String.class);
                                if(title == null)
                                    title = "";

                                String body = art_sn.child(articleBody).getValue(String.class);
                                if(body == null)
                                    body = "";

                                String imagePath = null;
                                for (DataSnapshot images_sn : art_sn.child(articleImages).getChildren()) {
                                    imagePath = (images_sn.getValue(String.class));
                                    break;
                                }
                                if(imagePath == null)
                                    imagePath = "";

                                long article_time = (long) art_sn.child(articleTime).getValue();

                                adapter.addArticle(new Article_Slim(ID, article_time, title, body, imagePath, Article.Type.values()[i]));

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/
        }
        public void addArticle(Article_Slim article) {
            if(adapter != null) {
                adapter.addArticle(article);
            }
        }
        public void clearAll() {
            if(adapter != null)
                adapter.clearAll();
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        final TextView titleText;
        final ViewPager2 viewPager;
        final TabLayout tabLayout;
        private final ArticleNavigation articleNavigation;
        private CategoryRecyclerAdapter adapter;
        public CategoryViewHolder(@NonNull View itemView, ArticleNavigation articleNavigation) {
            super(itemView);
            titleText = itemView.findViewById(R.id.template_news__TitleText);
            viewPager = itemView.findViewById(R.id.template_news__ViewPager2);
            tabLayout = itemView.findViewById(R.id.template_news__TabLayout);
            this.articleNavigation = articleNavigation;

        }

        public void setDetails(String title) {
            titleText.setText(title);
            adapter = new CategoryRecyclerAdapter(articleNavigation);
            viewPager.setAdapter(adapter);
            new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                }
            }).attach();
            /*final Resources r = itemView.getContext().getResources();
            final String
                    articleTitle = r.getString(R.string.fb_art_title),
                    articleBody =  r.getString(R.string.fb_art_body),
                    articleImages = r.getString(R.string.fb_art_images),
                    articleTime = r.getString(R.string.fb_art_time),
                    articleFeatured = r.getString(R.string.fb_art_featured);
            FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_news_key)).child(childPath).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adapter.clearAll();
                    for(DataSnapshot art_sn: snapshot.getChildren()) {
                        boolean is_featured = (boolean) art_sn.child(articleFeatured).getValue();
                        if(is_featured) {
                            String ID = art_sn.getKey();
                            if(ID == null)
                                continue;

                            String title = art_sn.child(articleTitle).getValue(String.class);
                            if(title == null)
                                title = "";

                            String body = art_sn.child(articleBody).getValue(String.class);
                            if(body == null)
                                body = "";

                            String imagePath = null;
                            for (DataSnapshot images_sn : art_sn.child(articleImages).getChildren()) {
                                imagePath = (images_sn.getValue(String.class));
                                break;
                            }
                            if(imagePath == null)
                                imagePath = "";

                            long article_time = (long) art_sn.child(articleTime).getValue();

                            adapter.addArticle(new Article_Slim(ID, article_time, title, body, imagePath, type));
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

        }
        public void addArticle(Article_Slim article) {
            if(adapter != null)
                adapter.addArticle(article);
        }
        public void clearAll() {
            if(adapter != null) adapter.clearAll();
        }

    }
}
