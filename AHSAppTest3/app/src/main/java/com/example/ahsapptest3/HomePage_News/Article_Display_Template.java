package com.example.ahsapptest3.HomePage_News;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.R;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Article_Display_Template extends Fragment implements Parcelable {


    public Article_Display_Template() {
        // Required empty public constructor
    }

    private final static String TIME = "1", TITLE = "2", SUMMARY ="3", IMAGE_PATH = "4",BOOKMARKED = "5"; //keys
    
    private long time;
    private boolean is_bookmarked = false;
    private String title, summary, imagePath;
            
    View view;


    protected Article_Display_Template(Parcel in) {
        time = in.readLong();
        is_bookmarked = in.readByte() != 0;
        title = in.readString();
        summary = in.readString();
        imagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeByte((byte) (is_bookmarked ? 1 : 0));
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article_Display_Template> CREATOR = new Creator<Article_Display_Template>() {
        @Override
        public Article_Display_Template createFromParcel(Parcel in) {
            return new Article_Display_Template(in);
        }

        @Override
        public Article_Display_Template[] newArray(int size) {
            return new Article_Display_Template[size];
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.template__article_display, container, false);

        final ImageButton bookmarked_btn = view.findViewById(R.id.article_display__bookmarked_button);
        bookmarked_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swap_bookmark(bookmarked_btn);
            }
        });

        time = getArguments().getLong(TIME);
        setTimeUpdated(view);
        
        title = getArguments().getString(TITLE);
        setTitleText(view);

        summary = getArguments().getString(SUMMARY);
        setSummaryText(view);

        imagePath = getArguments().getString(IMAGE_PATH);

        return view;
    }

    public static Article_Display_Template newInstanceOf(Date time_updated, String title, String summary, String imagePath, Boolean is_bookmarked)
    {
        Article_Display_Template thisFrag = new Article_Display_Template();
        Bundle args = new Bundle();
        args.putLong(TIME, time_updated.getTime());
        args.putString(TITLE, title);
        args.putString(SUMMARY, summary);
        args.putString(IMAGE_PATH, imagePath);
        args.putBoolean(BOOKMARKED, is_bookmarked);

        thisFrag.setArguments(args);
        return thisFrag;
    }

    public void setTimeUpdated(View view)
    {
        TextView updated_text = view.findViewById(R.id.article_display__time_updated_Text);
        Date currentTime = new Date();
        long time_difference = currentTime.getTime()-time;
        int hours = (int) Math.round((time_difference/(3600000.0)));
        updated_text.setText(getString(R.string.time_hours_updated_placeholder, hours));
    }

    public void setTitleText(View view)
    {
        TextView titleText = view.findViewById(R.id.article_display__title_Text);
        titleText.setText(title);
    }

    public void setSummaryText(View view)
    {
        TextView summaryText = view.findViewById(R.id.article_display__summary_Text);
        summaryText.setText(summary);
    }

    public void setImage(View view)
    {
        //TODO: implement this
    }

    public void saveAsBookmark()
    {

    }

    public void removeBookmark()
    {

    }

    public void swap_bookmark(ImageButton btn)
    {
        if(!is_bookmarked)
        {
            btn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bookmarked_icon_inactive, null));
        }
        else
        {
            btn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bookmarked_icon_active, null));
        }
        is_bookmarked = !is_bookmarked;

    }

    public long getTime()
    {
        return time;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public boolean isBookmarked()
    {
        return is_bookmarked;
    }


}
