package com.example.ahsapptest3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahsapptest3.Helper_Code.FullScreenActivity;
import com.example.ahsapptest3.Helper_Code.Helper;

public class Bulletin_Item_Activity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulletin_article_layout);

        Bulletin_Data data = getIntent().getParcelableExtra("data");
        TextView
                dateText = findViewById(R.id.bulletin_article_dateText),
                titleText = findViewById(R.id.bulletin_article_titleText),
                bodyText = findViewById(R.id.bulletin_article_bodyText),
                typeText = findViewById(R.id.bulletin_article_type_text);

        if(data != null) {
            dateText.setText(Helper.DateFromTime("MMMM dd, yyyy", data.getTime()));
            titleText.setText(data.getTitle());
            Helper.setHtmlParsedText_toView(bodyText, data.getBodyText());
            typeText.setText(data.getType().getName());

            // set listener for back button
            ImageView backButton = findViewById(R.id.bulletin_article_header_back);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(Bulletin.read_KEY, true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
        });}
    }
}