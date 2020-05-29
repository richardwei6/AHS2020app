package com.example.ahsapptest3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class ArticleActivity extends AppCompatActivity {

    private Article data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        data = getIntent().getParcelableExtra("data");
        TextView
                dateText = findViewById(R.id.article_dateText),
                titleText = findViewById(R.id.article_titleText),
                authorText = findViewById(R.id.article_authorText),
                bodyText = findViewById(R.id.article_bodyText);
        data.setDateText_toView(dateText);
        data.setTitleText_toView(titleText);
        data.setAuthorText_toView(authorText);
        data.setStoryText_toView(bodyText);

        //TODO: delete the below, it's just filler example
        bodyText.setText(data.getStory() + "\n\n" + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Magna fermentum iaculis eu non. Accumsan lacus vel facilisis volutpat. Sit amet porttitor eget dolor morbi non arcu risus quis. Iaculis urna id volutpat lacus laoreet non curabitur. Ut placerat orci nulla pellentesque dignissim enim sit. Praesent elementum facilisis leo vel. Malesuada nunc vel risus commodo viverra maecenas. Pharetra massa massa ultricies mi quis hendrerit dolor magna. In pellentesque massa placerat duis ultricies lacus sed turpis tincidunt. Sed risus ultricies tristique nulla aliquet. Dictum fusce ut placerat orci nulla pellentesque dignissim. Dictumst vestibulum rhoncus est pellentesque elit ullamcorper dignissim cras tincidunt. Aliquet nec ullamcorper sit amet risus nullam eget felis. At augue eget arcu dictum. Sed arcu non odio euismod.\n" +
                "\n" +
                "Nunc mi ipsum faucibus vitae. Velit scelerisque in dictum non consectetur a erat nam. Eget mi proin sed libero. Eu lobortis elementum nibh tellus molestie nunc. Ut tellus elementum sagittis vitae et leo. A iaculis at erat pellentesque adipiscing commodo elit. Risus nec feugiat in fermentum posuere urna nec tincidunt praesent. Feugiat scelerisque varius morbi enim nunc faucibus. Ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at. Libero enim sed faucibus turpis in. Quis auctor elit sed vulputate mi. Donec ac odio tempor orci dapibus ultrices in. At erat pellentesque adipiscing commodo elit. Semper risus in hendrerit gravida rutrum quisque. Et malesuada fames ac turpis.");

        // set up viewpager and associated dots
        ViewPager viewPager = findViewById(R.id.article_viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                // TODO: fix this, put image[] field in Article? Oh boy
                return new ArticleImage();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        TabLayout tabLayout = findViewById(R.id.article_tabLayout);
        tabLayout.setupWithViewPager(viewPager, true);

        // set up bookmark button
        ImageButton bookmarkButton = findViewById(R.id.article_bookmarkButton);
        data.setBookmarked_toView(bookmarkButton);
        data.setBookMarkListener_toView(bookmarkButton);

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
