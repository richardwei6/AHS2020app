package com.example.ahsapptest3.Helper_Code;

import android.content.Intent;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.ArticleActivity;
import com.example.ahsapptest3.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


// Alex Dang 2020
public class Helper {

    /*
    * Kind of unnecessary, but just for symmetry purposes
    * */
    public static void setText_toView(TextView view, String text)
    {
        view.setText(text);
    }

    /*
     *  Load image from the internet into an Image view (includes ImageButton, which extends ImageView)
     * */
    public static void setImage_toView_fromUrl(ImageView view, String url)
    {
        Picasso.with(view.getContext()).load(url).fit().centerInside().into(view, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                System.out.println("Picasso crashed");
            }
        });
        // possibly centerinside(), centercrop() options
        // you need fit() before centerinside(), centercrop() otherwise the app crashes with no logcat output
    }

    /*
     * Load image from internal storage into Image view
     * */
    public static void setImage_toView_fromStorage(ImageView view, String filePath)
    {
        Picasso.with(view.getContext()).load(new File(filePath)).into(view);
    }

    /*
     * Sets onClick listener to a view, opening a corresponding Article Activity
     * */
    public static void setArticleListener_toView(final View view, final Article article)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ArticleActivity.class);
                intent.putExtra("data", article);
                view.getContext().startActivity(intent);

            }
        });
    }

    /*
     *  Sets the proper image background to an imageView that displays the bookmark
     *  Ideally, this would be unnecessary; rather the bookmark view would have a selector as a background
     *  But that kinda requires a TODO: BookmarkButton Fragment
     * */
    public static void setBookmarked_toView(ImageView view, boolean is_bookmarked)
    {
        if(is_bookmarked)
        {
            view.setImageDrawable(ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.bookmarked_icon_active, null));
        }
        else
        {
            view.setImageDrawable(ResourcesCompat.getDrawable(view.getContext().getResources(), R.drawable.bookmarked_icon_inactive, null));
        }

    }

    /*
     *  Set an onClick listener to a bookmark view based on the current status of the article
     *  TODO: call a BookmarkHandler that updates the internal files
     * */
    public static void setBookMarkListener_toView(final ImageView view, final Article article)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.swapBookmark();
                setBookmarked_toView(view,article.isBookmarked());

            }
        });
    }

    /*
     *  Sets time in a given unit to a TextView, formatted
     * */
    public static void setTimeText_toView(TextView view, long time, TimeUnit unit)
    {
        long time_val = unit.convert(time, TimeUnit.MILLISECONDS);
        if(unit.equals(TimeUnit.MINUTES))
        {
            view.setText(view.getContext().getString(R.string.time_minutes_updated_placeholder, time_val));
        }
        if(unit.equals(TimeUnit.HOURS))
        {
            view.setText(view.getContext().getString(R.string.time_hours_updated_placeholder, time_val));
        }
    }

    /*
     *  Gets difference between time parameter passed and the time right now
     * */
    public static long TimeFromNow(long time)
    {
        Date currentTime = new Date();
        return currentTime.getTime()-time*1000L; // for interesting reasons, Date() uses seconds not milliseconds
    }

    /*
     *  Because I forget how to use SimpleDateFormat()
     * */
    public static String DateFromTime(String pattern, long time)
    {
        return new SimpleDateFormat(pattern, Locale.US).format(time*1000L);
    }


}
