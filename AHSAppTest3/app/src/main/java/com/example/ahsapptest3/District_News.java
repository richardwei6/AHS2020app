package com.example.ahsapptest3;

import androidx.core.content.ContextCompat;

public class District_News extends News_Template {
    @Override
    String[][] getData() {
        return new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"Title2", "summaryText2"},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
    }

    @Override
    String getTitleText() {
        return "DISTRICT NEWS";
    }

    @Override
    int getBarColor() {
        return ContextCompat.getColor(getContext(),R.color.VomitYellow_DDCD3E__HOME);
    }
}
