package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahsapptest3.Helper_Code.Helper;
import com.google.android.material.tabs.TabLayout;

public class ArticleActivity extends AppCompatActivity {

    private Article article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        article = getIntent().getParcelableExtra("data");
        TextView
                dateText = findViewById(R.id.article_dateText),
                titleText = findViewById(R.id.article_titleText),
                authorText = findViewById(R.id.article_authorText),
                bodyText = findViewById(R.id.article_bodyText);

        Helper.setText_toView(dateText,Helper.DateFromTime("MMMM dd, yyyy", article.getTimeUpdated()));
        Helper.setText_toView(titleText,article.getTitle());
        Helper.setText_toView(authorText,article.getAuthor());
        Helper.setText_toView(bodyText,article.getStory());

        // set up viewpager and associated dots
        ViewPager viewPager = findViewById(R.id.article_viewPager);

        // TODO: ViewPager crashes for unknown reason when image is updated from database (this does not happen on other pages)
        final String[] imagePaths = article.getImagePaths();
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position)
            {
                ImageView imageView = new ImageView(container.getContext());
                Helper.setImage_toView_fromUrl(imageView, article.getImagePaths()[position]);
                container.addView(imageView);
                return imageView;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object view)
            {
                container.removeView((View) view);
            }

            @Override
            public int getCount() {
                return imagePaths.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return object == view;
            }
        });

        TabLayout tabLayout = findViewById(R.id.article_tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.bringToFront(); // necessary, otherwise viewpager will cover it
        // using android:elevation attribute would be preferable but it is only available for API level 21+

        // set up bookmark button
        ImageButton bookmarkButton = findViewById(R.id.article_bookmarkButton);
        bookmarkButton.bringToFront(); // necessary, otherwise viewpager will cover it

        Helper.setBookmarked_toView(bookmarkButton,article.isBookmarked());
        Helper.setBookMarkListener_toView(bookmarkButton, article);

        // set listener for back button
        ImageButton backButton = findViewById(R.id.article_header_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
