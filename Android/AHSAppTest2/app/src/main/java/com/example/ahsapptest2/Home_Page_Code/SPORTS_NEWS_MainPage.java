package com.example.ahsapptest2.Home_Page_Code;


import com.example.ahsapptest2.R;

public class SPORTS_NEWS_MainPage extends Main_Page_Scrolling_Template {

    public SPORTS_NEWS_MainPage() {
        super();
    }

    public String[][] getInfo()
    {
        // fully implement later
        return
                new String[][] {
                        {"Sports News Title1","text1"},
                        {"Title2","text2"},
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
        return 1020000; // Reserve 1020000-1030000 for SPORTS NEWS HomePage
    }

    public String getTitleText()
    {
        return "SPORTS NEWS";
    }
}
