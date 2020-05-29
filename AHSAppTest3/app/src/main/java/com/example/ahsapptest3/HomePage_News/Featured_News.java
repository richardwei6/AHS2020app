package com.example.ahsapptest3.HomePage_News;

import androidx.core.content.ContextCompat;

import com.example.ahsapptest3.R;

public class Featured_News extends Featured_Template {
    @Override
    String[][] getData() {
        return new String[][]
                {
                        {"Lorem Ipsum a Very Long Title", "hello world what a nice day!"},
                        {"ASB NEWS Title2", "summaryText2. This is a long sample summary. This should cut off at two lines, with an ellipsis."},
                        {"Title3", "summaryText3"},
                        {"Title4", "summaryText4"},
                        {"Title5", "summaryText5"},
                        {"Title6", "summaryText6"}
                };
    }

    @Override
    String getTitleText() {
        return "FEATURED";
    }

    @Override
    int getBarColor() {
        return ContextCompat.getColor(getContext(), R.color.VomitYellow_DDCD3E__HOME);
    }
}
