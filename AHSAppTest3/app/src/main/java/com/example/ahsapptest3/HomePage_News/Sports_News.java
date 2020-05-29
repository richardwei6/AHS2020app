package com.example.ahsapptest3.HomePage_News;

import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.R;

public class Sports_News extends News_Template {
    @Override
    String[][] getData() {
        return new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"Sports News Title2", "summaryText2"},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
    }

    @Override
    String getTitleText() {
        return "SPORTS NEWS";
    }

    @Override
    int getBarColor() {
        return ContextCompat.getColor(getContext(), R.color.SeaBlue_364D9E__HOME);
    }
}
