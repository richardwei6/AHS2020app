package com.example.ahsapptest3.HomePage_News;

import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.R;

public class Asb_News extends News_Template {
    @Override
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

    @Override
    public String getTitleText() {
        return "ASB NEWS";
    }

    @Override
    public int getBarColor() {
        return ContextCompat.getColor(getContext(), R.color.Crimson_992938__HOME_BULLETIN_ARTICLE_NOTIF);
    }
}
