package com.example.ahsapptest3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notif_Activity extends AppCompatActivity {

    private static Article[] articles;
    private FrameLayout[] frameLayouts;
    private LinearLayout listLayout;
    private boolean[] justNotified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif_layout);

        listLayout = findViewById(R.id.notif_linearLayout);
        String[][] data = getData();
        if(articles == null)
        {
            articles = new Article[data.length];
            for(int i = 0; i < articles.length; i++)
            {
                articles[i] =

                new Article(2342,238472394,data[i][0],"author",data[i][1],new String [] {"hello"},false,i < 3);
                // change this later
            }
        }
        justNotified = new boolean[articles.length];
        for(int i = 0; i < articles.length; i++)
        {
            justNotified[i] = articles[i].alreadyNotified();
        }

        frameLayouts = new FrameLayout[data.length];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0,0,0,0);

        for(int i = 0; i < frameLayouts.length; i++) {
            frameLayouts[i] = new FrameLayout(this);
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(getIdRange()+i);
            listLayout.addView(frameLayouts[i],params);
            Helper.setArticleListener_toView(frameLayouts[i], articles[i]);
        }

        Notif_Template[] items = new Notif_Template[data.length];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = Notif_Template.newInstanceOf(articles[i]);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),items[i])
                    .commit();

        }

        // set listener for back button
        ImageButton backButton = findViewById(R.id.notif_header_back);
        final Context context = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*@Override
    public void onResume()
    {
        super.onResume();

        for(FrameLayout fl: frameLayouts) // slightly janky solution to the problem that when article back button pressed, android doesn't redraw the notif page,
        {
            fl.setVisibility(View.GONE); //thus changing notified boolean seems to have no effect, until you click back on notif then go back to notif again
            fl.setVisibility(View.VISIBLE);
        }
    }*/
    @Override
    public void onRestart(){
        super.onRestart();
        this.recreate(); // see comment above, only this actually works; maybe fix the listener to change flag variable to recreate? maybe sharedpreferences?
        /*for(FrameLayout fl: frameLayouts)
        {
            /*fl.requestLayout(); // doesn't work
            fl.invalidate();
            fl.forceLayout();*7/


        }*/
        /*int position = -1;
        for(int i = 0; i < articles.length; i++)
        {
            if(justNotified[i] ^ articles[i].alreadyNotified()) //xor
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayouts[i].getId(),Notif_Template.newInstanceOf(articles[i]));
                System.out.println("::: replace" + i);
                frameLayouts[i].invalidate();
                //position = i+2; // there are two elements in the linearlayout before the frameLayouts. actually prob better to make a whole separate layout now
            }
        }*/



    }

    private int getIdRange()
    {
        return 2000000;
    }

    public String[][] getData() {
        return new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day! This is the content inside the article."},
                        {"ASB NEWS Title2", "summaryText2. This is a long sample summary. This should cut off at two lines, with an ellipsis."},
                        {"Title3", "summaryText3. Content inside article, but it will be truncated."},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"},
                        {"Title7", "summaryText7"}
                };
    }

    public Date getDate(String key)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,3,22,9,7);

        Date date = calendar.getTime();
        return date;
    }

    public Boolean isAlreadyBookmarked(String key)
    {
        return false;
    }

    public String getImageFilePath(String key)
    {
        return "";
    }
}
