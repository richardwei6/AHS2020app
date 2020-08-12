package com.example.ahsapptest3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ahsapptest3.Misc.FullScreenActivity;
import com.example.ahsapptest3.Misc.Helper;
import com.example.ahsapptest3.Misc.MediaYoutubeFragment;
import com.example.ahsapptest3.Misc.ValContainer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ArticleActivity extends FullScreenActivity implements ArticleImageFragment.OnImageClick{
    /*private static final String TAG = "ArticleActivity";*/

    public static final String read_KEY = "1";
    public static final String data_key = "0";
    private ValContainer<Boolean> saved;
    private ValContainer<Boolean> saved_copy;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_layout);

        article = getIntent().getParcelableExtra(data_key);
        final String[] imagePaths = article.getImagePaths();
        final String[] videoIDs = article.getVideoIDS();

        final ViewPager2 viewPager2 = findViewById(R.id.article_viewPager2);
        final MultipleVideoImagePagerAdapter adapter = new MultipleVideoImagePagerAdapter(this, videoIDs, imagePaths);
        viewPager2.setAdapter(adapter);
        // don't even bother handling it if there is 1 video or less
        if(videoIDs.length > 1)
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                int oldPosition = -1; // so on load it's "changed" and method is called
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    /*Log.d(TAG, "selected page\t"+position);*/
                    adapter.onPageChanged(oldPosition, position);
                    oldPosition = position;

                }
            });

        /*gestureDetector = new GestureDetector(this, new SwipeDetector(new SwipeDetector.onSwipeListener() {
            @Override
            public void onSwipeFromLeft() {
                *//*if(viewPager2.getScrollState() != ViewPager2.SCROLL_STATE_DRAGGING) {
                    finish();
                    overridePendingTransition(0, R.anim.to_right);
                }*//*

            }
        }));*/

        TabLayout tabLayout = findViewById(R.id.article_tabLayout);
        if(videoIDs.length + imagePaths.length <= 1)
            tabLayout.setVisibility(View.GONE);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        });
        tabLayoutMediator.attach();

        TextView
                dateText = findViewById(R.id.article_dateText),
                titleText = findViewById(R.id.article_titleText),
                authorText = findViewById(R.id.article_authorText),
                bodyText = findViewById(R.id.article_bodyText);

        dateText.setText(Helper.getDateFromTime(Helper.defaultDatePattern, article.getTimeUpdated()));
        titleText.setText(article.getTitle());
        authorText.setText(this.getString(R.string.author_placeholder, article.getAuthor()));
        Helper.setHtmlParsedText_toView(bodyText, article.getStory());

        TextView typeText = findViewById(R.id.article_type_text);
        typeText.setText(article.getType().getName());

        // set up bookmark button
        final ImageView bookmarkButton = findViewById(R.id.article_bookmarkButton);
        final SavedDatabase savedDatabase = SavedDatabase.getInstance(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                saved = new ValContainer<>(savedDatabase.alreadyAdded(article.getID()));
                saved_copy = new ValContainer<>(saved.getVal());

                Helper.setBookmarked_toView(bookmarkButton, saved.getVal());

            }
        });
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved.setVal(!saved.getVal());
                Helper.setBookmarked_toView(bookmarkButton, saved.getVal());
            }
        });

        // set listener for back button
        ImageView backButton = findViewById(R.id.article_header_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(saved.getVal() != saved_copy.getVal())
                {
                    intent.putExtra(Saved_Activity.saved_status_changed_KEY, true);
                    updateSavedStatus(savedDatabase);
                }
                else
                    intent.putExtra(Saved_Activity.saved_status_changed_KEY, false);

                intent.putExtra(read_KEY, true);

                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.to_right);
            }
        });
    }

    public void updateSavedStatus(final SavedDatabase savedDatabase){
        if(saved.getVal()) { // was false but now true
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    savedDatabase.add(article);
                }
            };
            runnable.run();
        } else { // was true but now false
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    savedDatabase.deleteByID(article.getID());
                }
            };
            runnable.run();
        }
    }

    @Override
    public void onClick(String imagePath) {
        Intent intent = new Intent(ArticleActivity.this, ArticleImageActivity.class);
        intent.putExtra(ArticleImageActivity.imagePath_key, imagePath);
        startActivity(intent);
    }

    /**
     * A FragmentStateAdapter that supports a mix of youtube videos and images
     */
    public static class MultipleVideoImagePagerAdapter extends FragmentStateAdapter implements  MediaYoutubeFragment.MediaFullScreenListener
    {
        final String[] videoIDs;
        final String[] imagePaths;
        final MediaYoutubeFragment.InitializeListener[] initializeListeners;
        public MultipleVideoImagePagerAdapter(@NonNull FragmentActivity fragmentActivity, String[] videoIDs, String[] imagePaths) {
            super(fragmentActivity);
            this.videoIDs = videoIDs;
            this.imagePaths = imagePaths;
            initializeListeners = new MediaYoutubeFragment.InitializeListener[videoIDs.length];
        }

        public boolean inVideosRange(int position)
        {
            return position >= 0 && position < videoIDs.length;
        }

        @NonNull
        @Override
        public Fragment createFragment(final int position) {
            if(inVideosRange(position)){
                MediaYoutubeFragment fragment = MediaYoutubeFragment.newInstance(videoIDs[position], this);
                initializeListeners[position] = fragment.getmInitializeListener();
                if(initializeListeners.length == 1)
                    initializeListeners[position].startVideo();
                return fragment;
            } else { // images
                return ArticleImageFragment.newInstance(imagePaths[position-videoIDs.length]);
            }
        }

        public void onPageChanged( int oldPosition, int newPosition)
        {
            if(!inVideosRange(oldPosition) && inVideosRange(newPosition)) // first time, or coming back from position without video
            {
                initializeListeners[newPosition].startVideo();
                return;
            }
            boolean pageHasChanged = oldPosition != newPosition;
            /*Log.d(TAG, "pageChanged\t" + pageHasChanged);*/
            if(pageHasChanged) // page changed
            {
                if(inVideosRange(oldPosition))
                    initializeListeners[oldPosition].stopVideo();
                if(inVideosRange(newPosition))
                    initializeListeners[newPosition].startVideo();
            }
        }

        @Override
        public int getItemCount() {
            return videoIDs.length + imagePaths.length;
        }

        @Override
        public void onFullScreen(boolean fullScreen) {
            /*if(!fullScreen) {
                int newOrientation = getResources().getConfiguration().orientation;
                if (newOrientation != oldOrientation) {
                    setRequestedOrientation(oldOrientation);
                }
            }*/
        }
    }

}
