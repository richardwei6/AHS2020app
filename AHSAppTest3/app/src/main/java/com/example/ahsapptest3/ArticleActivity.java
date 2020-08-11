package com.example.ahsapptest3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Helper_Code.Helper;
import com.example.ahsapptest3.Helper_Code.MediaYoutubeFragment;
import com.example.ahsapptest3.Helper_Code.ValContainer;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ArticleActivity extends FullScreenActivity {
    private static final String TAG = "ArticleActivity";

    public static final String read_KEY = "1";
    public static final String data_key = "0";
    private ValContainer<Boolean> saved;
    private ValContainer<Boolean> saved_copy;
    private Article article;
    /*private int oldOrientation;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        /*oldOrientation = getResources().getConfiguration().orientation;*/

        article = getIntent().getParcelableExtra(data_key);
        TextView
                dateText = findViewById(R.id.article_dateText),
                titleText = findViewById(R.id.article_titleText),
                authorText = findViewById(R.id.article_authorText),
                bodyText = findViewById(R.id.article_bodyText);

        dateText.setText(Helper.getDateFromTime(Helper.defaultDatePattern, article.getTimeUpdated()));
        titleText.setText(article.getTitle());
        authorText.setText(this.getString(R.string.author_placeholder, article.getAuthor()));
        Helper.setHtmlParsedText_toView(bodyText, article.getStory());

        final String[] imagePaths = article.getImagePaths();
        final String[] videoIDs = article.getVideoIDS();

        TabLayout tabLayout = findViewById(R.id.article_tabLayout);

        final ViewPager2 viewPager2 =findViewById(R.id.article_viewPager2);
        final MultipleVideoImagePagerAdapter adapter = new MultipleVideoImagePagerAdapter(this, videoIDs, imagePaths);
        viewPager2.setAdapter(adapter);
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
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        });
        tabLayoutMediator.attach();
        TextView typeText = findViewById(R.id.article_type_text);
        typeText.setText(article.getType().getName());

        // set up bookmark button
        final ImageView bookmarkButton = findViewById(R.id.article_bookmarkButton);
        final SavedDatabase savedDatabase = SavedDatabase.getInstance(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                saved = new ValContainer<>();
                saved.setVal(savedDatabase.alreadyAdded(article.getID()));
                saved_copy = new ValContainer<>();
                saved_copy.setVal(saved.getVal());

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
/*
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }*/

    /**
     * A FragmentStateAdapter that supports a mix of youtube videos and images
     */
    public static class MultipleVideoImagePagerAdapter extends FragmentStateAdapter implements  MediaYoutubeFragment.MediaFullScreenListener
    {
        String[] videoIDs, imagePaths;
        MediaYoutubeFragment.InitializeListener[] initializeListeners;
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
                return fragment;
            } else { // images
                return ImageFragment.newInstance(imagePaths[position-videoIDs.length]);
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

    /**
     * A simple Image holder compatible with FragmentPagerAdapters solely for use in viewPagers in articles
     */
    public static class ImageFragment extends Fragment {

        private static final String imagePath_KEY = "1";
        private String imagePath;
        public static ImageFragment newInstance(String imagePath) {
            ImageFragment thisFrag = new ImageFragment();
            Bundle args = new Bundle();
            args.putString(imagePath_KEY, imagePath);
            thisFrag.setArguments(args);
            return thisFrag;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(getArguments() != null)
                imagePath = getArguments().getString(imagePath_KEY);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.article_image, container, false);
            /*if(container == null) // apparently, it always is in the viewpager
                Log.d(TAG, "help, the container is null");*/
            ImageView imageView = view.findViewById(R.id.article_image);
            /*container.addView(imageView);*/
            Helper.setImageFromUrl(imageView, imagePath);
            return imageView;
        }
    }
}
