package com.example.ahsapptest2;


public class ASB_NEWS_MainPage extends Main_Page_Scrolling_Template {

    public ASB_NEWS_MainPage() {
        super();
    }

    public String[][] getInfo()
    {
        // fully implement later
        return
                new String[][] {
                        {"Asb News Title1","text1"},
                        {"Title2","text2"},
                        {"Title3","text3"},
                        {"Title4","text4"},
                };
    }

    public int getLayoutId()
    {
        return R.layout.scrolling_template__main_page_layout;
    }

    public int getConstraintLayoutId()
    {
        return R.id.Scrolling_Template_ScrollView_ConstraintLayout;
    }

    public int getIdRange()
    {
        return 1030000; // Reserve 1030000-1040000 for ASB HomePage
    }

    public String getTitleText()
    {
        return "ASB NEWS";
    }
}
