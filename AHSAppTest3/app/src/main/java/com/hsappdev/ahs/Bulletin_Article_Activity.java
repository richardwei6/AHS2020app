package com.hsappdev.ahs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsappdev.ahs.Misc.FullScreenActivity;
import com.hsappdev.ahs.Misc.Helper;
import com.hsappdev.ahs.Misc.ValContainer;

public class Bulletin_Article_Activity extends FullScreenActivity {

    /*public static final String read_KEY = "1";*/
    public static final String data_KEY = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_article_layout);

        /*PhotoView photoView = findViewById(R.id.photoview);
        photoView.setImageResource(R.drawable.downsize_feather);*/
        final Bulletin_Article data = getIntent().getParcelableExtra(data_KEY);
        TextView
                dateText = findViewById(R.id.bulletin_article_dateText),
                titleText = findViewById(R.id.bulletin_article_titleText),
                bodyText = findViewById(R.id.bulletin_article_bodyText),
                typeText = findViewById(R.id.bulletin_article_type_text);

        if(data != null) {
            dateText.setText(Helper.getDateFromTime(Helper.defaultDatePattern, data.getTime()));
            titleText.setText(data.getTitle());
            Helper.setHtmlParsedText_toView(bodyText, data.getBodyText());
            typeText.setText(data.getType().getName());


            final ImageView bookmarkButton = findViewById(R.id.bulletin_article_bookmarkButton);

            final SavedDatabase savedDatabase = SavedDatabase.getInstance(this);
            final ValContainer<Boolean> saved = new ValContainer<>(savedDatabase.alreadyAdded(data.getID()));
            final ValContainer<Boolean> saved_copy = new ValContainer<>(saved.getVal());

            Helper.setBookmarked_toView(bookmarkButton,saved.getVal());
            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saved.setVal(!saved.getVal());
                    Helper.setBookmarked_toView(bookmarkButton,saved.getVal());

                }
            });

            // set listener for back button
            ImageView backButton = findViewById(R.id.bulletin_article_header_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if(saved.getVal() != saved_copy.getVal())
                    {
                        intent.putExtra(Saved_Activity.saved_status_changed_KEY, true);
                        if(saved.getVal()) { // was false but now true
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    savedDatabase.add(data);
                                }
                            };
                            runnable.run();
                        } else { // was true but now false
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    savedDatabase.deleteByID(data.getID());
                                }
                            };
                            runnable.run();
                        }
                    }
                    else
                        intent.putExtra(Saved_Activity.saved_status_changed_KEY, false);
                    endWithAnimation(intent);
                }
            });
        } else {
            ImageView backButton = findViewById(R.id.bulletin_article_header_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    endWithAnimation(new Intent());
                }
            });
        }

    }

    public void endWithAnimation(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
        overridePendingTransition(0, R.anim.to_right);
    }

}