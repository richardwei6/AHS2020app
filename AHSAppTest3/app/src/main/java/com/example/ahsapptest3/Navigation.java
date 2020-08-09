package com.example.ahsapptest3;

public interface Navigation {
    void goToHome();
    void goToBulletin();
    void goToSaved();
    void goToSettings();
    int getScrollingViewId();
    HighlightOption getHighlightOption();
    enum HighlightOption{
        HOME, BULLETIN, SAVED, SETTINGS, NONE
    }
}
