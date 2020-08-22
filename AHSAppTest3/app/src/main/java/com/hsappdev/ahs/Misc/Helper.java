package com.hsappdev.ahs.Misc;

import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hsappdev.ahs.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


// Alex Dang 2020
public class Helper{
    /*private static final String TAG = "Helper";*/

    public static void setHtmlParsedText_toView(TextView view, String text)
    {
        view.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

/*
    public static void setHtmlParsed_withRipple(TextView view, View parentView, String text)
    {
        view.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS));
        view.setMovementMethod(RippleLinkMovementMethod.getInstance(parentView));
    }*/

   /* *//**
     *  Load image from the internet or internal storage into an ImageView
     * *//*
    public interface OnPaletteLoad {
        void onLoad(Palette palette);
    }
    public static void setImageFromUrl(final ImageView view, String url, final OnPaletteLoad onPaletteLoad)
    {
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
                            if (palette != null) {
                                onPaletteLoad.onLoad((palette));
                                int mutedColor = palette.getMutedColor(ContextCompat.getColor(view.getContext(), R.color.CoffeeRed_473D3D));

                                Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.image_bg);
                                DrawableCompat.setTint(drawable, mutedColor);
                                view.setBackgroundResource(R.drawable.image_bg);
                            }
                        }
                    });
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
    }*/

    /**
     * same as above but with centercrop() option
     * @param view
     * @param url
     */
    public static void setImageFromUrl_CenterCrop(final ImageView view, String url)
    {
        Glide
                .with(view.getContext())
                .load(url)
                .override(400,300)
                .error(R.drawable.image_bg)
                .transform(new CenterCrop(), new RoundedCorners((int) view.getContext().getResources().getDimension(R.dimen.SmallRound_BG_Radius)))
                .into(view);

    }

    public static void setImageFromUrl_CenterCrop_FullSize(final ImageView view, String url) {
        Glide
                .with(view.getContext())
                .load(url)
                .error(R.drawable.image_bg)
                .transform(new CenterCrop(), new RoundedCorners((int) view.getContext().getResources().getDimension(R.dimen.SmallRound_BG_Radius)))
                .into(view);
    }


    /**
     * Sets the default background to an imageView
     * So that should the loaded image take up less space,
     * There wouldn't be an awkward blank space
     * @param view imageview
     */
   /* public static void setDefaultBackground_toView(ImageView view)
    {
        view.setBackgroundResource(R.drawable.image_bg);
    }*/

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
     *  Sets time in a given unit to a TextView, formatted
     */
    public static void setTimeText_toView(TextView view, long time)
    {
        TimeUnit unit = getLogicalTimeUnit(time);
        //Log.d(TAG, unit.toString());

        long time_val = unit.convert(time, TimeUnit.MILLISECONDS);
        String text = "";
        String after;
        if(time_val < 0)
        {
            time_val = -time_val;
            after = view.getContext().getString(R.string.from_now);
        }
        else
            after = view.getContext().getString(R.string.ago);
        if (unit.equals(TimeUnit.MINUTES)) {
            text = view.getContext().getResources().getQuantityString(R.plurals.time_minutes_updated_placeholder, (int) time_val, time_val, after);
        } else if (unit.equals(TimeUnit.HOURS)) {
            text = view.getContext().getResources().getQuantityString(R.plurals.time_hours_updated_placeholder, (int) time_val, time_val, after);
        } else if (unit.equals(TimeUnit.DAYS)) {
            if(time_val < 7) {
                text = view.getContext().getResources().getQuantityString(R.plurals.time_days_updated_placeholder, (int) time_val, time_val, after);
            } else if(time_val < 30) {
                time_val = Math.round(time_val/7.0);
                text = view.getContext().getResources().getQuantityString(R.plurals.time_weeks_updated_placeholder, (int) time_val, time_val, after);
            } else if(time_val < 365) {
                time_val = Math.round(time_val/30.0);
                text = view.getContext().getResources().getQuantityString(R.plurals.time_months_updated_placeholder, (int) time_val, time_val, after);
            } else {
                time_val = Math.round(time_val/365.24);
                text = view.getContext().getResources().getQuantityString(R.plurals.time_years_updated_placeholder, (int) time_val, time_val, after);
            }
        }

        view.setText(text);


    }

    /*
     *  Gets difference between time parameter passed and the time right now
     * */
    public static long TimeFromNow(long time)
    {
        Date currentTime = new Date();
        return currentTime.getTime()-time*1000L; // for interesting reasons, Date() uses seconds not milliseconds
    }

    /**
     *  Because I forget how to use SimpleDateFormat()
     *  See @link {https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html} for guidance on pattern
     */
    public static String getDateFromTime(String pattern, long time)
    {
        return new SimpleDateFormat(pattern, Locale.US).format(time*1000L);
    }
    public static final String defaultDatePattern = "MMMM d, yyyy";
    public static final String shortDatePattern = "MMM d, ''yy"; // represents single quote

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
            time_in_units = Math.abs((int) unit.convert(time_difference, TimeUnit.MILLISECONDS));
            //Log.d(TAG, String.valueOf(time_in_units));
            if(time_in_units < 1) // fresh! less than one hour!
            {
                return TimeUnit.MINUTES; // stop here, unreasonable to go below this unit
            }
            return TimeUnit.HOURS;
        }
        return TimeUnit.DAYS;
    }

}
