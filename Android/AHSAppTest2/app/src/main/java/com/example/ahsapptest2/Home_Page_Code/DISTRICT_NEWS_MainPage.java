package com.example.ahsapptest2.Home_Page_Code;


import com.example.ahsapptest2.R;

public class DISTRICT_NEWS_MainPage extends Main_Page_Scrolling_Template {

    public DISTRICT_NEWS_MainPage() {
        super();
    }

    public String[][] getInfo()
    {
        // fully implement later
        return
                new String[][] {
                        {"District News Title1","text1"},
                        {"A very long title. How are you doing today? Once upon a time, there was a little old lady","text2"},
                        {"Title3","text3"},
                        {"Title4","text4"},
                };
    }

    public int getLayoutId()
    {
        return R.layout.home_page__scrolling_template_layout;
    }

    public int getConstraintLayoutId()
    {
        return R.id.Scrolling_Template_ScrollView_ConstraintLayout;
    }

    public int getIdRange()
    {
        return 1010000; // Reserve 1010000-1020000 for District HomePage
    }

    public String getTitleText()
    {
        return "DISTRICT NEWS";
    }
}
