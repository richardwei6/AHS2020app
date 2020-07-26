package com.example.ahsapptest3.Helper_Code;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.text.HtmlCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.ArticleActivity;
import com.example.ahsapptest3.ArticleDatabase;
import com.example.ahsapptest3.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


// Alex Dang 2020
public class Helper{

    /**
    * Kind of unnecessary, but just for symmetry purposes
    * */
    public static void setText_toView(TextView view, String text)
    {
        view.setText(text);
    }

    public static void setHtmlParsedText_toView(TextView view, String text)
    {

        view.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }
/*
    public static void setHtmlParsed_withRipple(TextView view, View parentView, String text)
    {
        view.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
        view.setMovementMethod(RippleLinkMovementMethod.getInstance(parentView));
    }*/

    /**
     *  Load image from the internet into an Image view (includes ImageButton, which extends ImageView)
     * */
    public static void setImage_toView_fromUrl(final ImageView view, String url)
    {
        /*Picasso.with(view.getContext()).load(url).fit().centerInside().into(view, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                System.out.println("Picasso crashed");
            }
        });
        // possibly centerinside(), centercrop() options
        // you need fit() before centerinside(), centercrop() otherwise the app crashes with no logcat output*/
        /*final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                vto.removeOnGlobalLayoutListener(this);
                int width = view.getWidth();
                int height = view.getHeight();
            }
        });*/

        Glide
                .with(view.getContext())
                .load(url)
                .error(R.drawable.image_bg)
                .centerInside()
                .transform(new RoundedCorners((int) view.getContext().getResources().getDimension(R.dimen.SmallRound_BG_Radius)))
                .into(view);

        Glide
                .with(view.getContext())
                .asBitmap()
                .apply(new RequestOptions().override(10,10))
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener()
                        {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(),R.color.NEW_CoffeeRed_473D3D__HOME_ARTICLE_SAVED));

                                Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.image_bg);
                                DrawableCompat.setTint(drawable,mutedColor);
                                view.setBackgroundResource(R.drawable.image_bg);

                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        /*Glide
                .with(view.getContext())
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        *//*Palette.from(resource).generate(new Palette.PaletteAsyncListener()
                        {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(),R.color.NEW_CoffeeRed_473D3D__HOME_ARTICLE_SAVED));

                                Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.image_bg);
                                DrawableCompat.setTint(drawable,mutedColor);
                                view.setBackgroundResource(R.drawable.image_bg);

                            }
                        });*//*
                        Palette palette = Palette.from(resource).generate();
                        int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(),R.color.NEW_CoffeeRed_473D3D__HOME_ARTICLE_SAVED));

                        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.image_bg);
                        DrawableCompat.setTint(drawable,mutedColor);
                        view.setBackgroundResource(R.drawable.image_bg);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/
    }

    /**
     * Load image from internal storage into Image view
     * Also sets Palette API
     * */
    public static void setImage_toView_fromStorage(final ImageView view, String filePath)
    {
        /*Picasso.with(view.getContext()).load(new File(filePath)).into(view);*/
        Glide
                .with(view.getContext())
                .load(new File(filePath))
                .error(R.drawable.image_bg)
                .centerInside()
                .into(view);

        Glide
                .with(view.getContext())
                .asBitmap()
                .load(filePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener()
                        {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(),R.color.NEW_CoffeeRed_473D3D__HOME_ARTICLE_SAVED));
                                view.setColorFilter(mutedColor);
                                view.setBackgroundResource(R.drawable.image_bg);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    /**
     * Sets the default background to an imageView
     * So that should the loaded image take up less space,
     * There wouldn't be an awkward blank space
     * @param view
     */
    public static void setDefaultBackground_toView(ImageView view)
    {

        view.setBackgroundResource(R.drawable.image_bg);
    }

    /**
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

    /**
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

    /**
     *  Set an onClick listener to a bookmark view based on the current status of the article
     *  TODO: call a BookmarkHandler that updates the internal files
     * */
    public static void setBookMarkListener_toView(final ImageView view, final Article article)
    {
        final ArticleDatabase articleDatabase = ArticleDatabase.getInstance(view.getContext(), ArticleDatabase.Option.BOOKMARK);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.swapBookmark();
                setBookmarked_toView(view,article.isBookmarked());
                if(!articleDatabase.alreadyAdded(article.getID()))
                        articleDatabase.add(article);
                else
                    articleDatabase.delete(article);
                ArticleDatabase.setBookmarkChanged();
            }
        });
    }


    /*
     *  Sets time in a given unit to a TextView, formatted
     * */
    public static void setTimeText_toView(TextView view, long time)
    {
        TimeUnit unit = getLogicalTimeUnit(time);
        long time_val = unit.convert(time, TimeUnit.MILLISECONDS);
        String after;
        if(time_val < 0)
        {
            time_val = -time_val;
            after = view.getContext().getString(R.string.from_now);
        }
        else
            after = view.getContext().getString(R.string.ago);
        if(unit.equals(TimeUnit.MINUTES))
        {
            view.setText(view.getContext().getString(R.string.time_minutes_updated_placeholder, time_val, after));
        }
        else if(unit.equals(TimeUnit.HOURS))
        {
            view.setText(view.getContext().getString(R.string.time_hours_updated_placeholder, time_val, after));
        }
        else if(unit.equals(TimeUnit.DAYS))
        {
            view.setText(view.getContext().getString(R.string.time_days_updated_placeholder, time_val, after));
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

    /**
     * Converts a long time to a time unit based on the reasonable unit for the length of time
     * to avoid overly large number of hours, minutes, etc.
     * @param time_difference The long time to be converted
     * @return the proper TimeUnit in Hours, minutes, etc.
     */
    public static TimeUnit getLogicalTimeUnit(long time_difference)
    {
        TimeUnit unit = TimeUnit.DAYS; // most reasonable to start with this
        int time_in_units = Math.abs((int) unit.convert(time_difference, TimeUnit.MILLISECONDS));
        if(time_in_units < 1) // less than one day, use hours
        {
            unit = TimeUnit.HOURS;
            time_in_units = (int) unit.convert(time_difference, TimeUnit.MILLISECONDS);
            if(time_in_units < 1) // fresh! less than one hour!
            {
                return TimeUnit.MINUTES; // stop here, unreasonable to go below this unit
            }
            return TimeUnit.HOURS;
        }
        return TimeUnit.DAYS;
    }

}
