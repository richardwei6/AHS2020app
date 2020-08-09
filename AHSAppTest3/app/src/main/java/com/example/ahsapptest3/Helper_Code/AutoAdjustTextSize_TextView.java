package com.example.ahsapptest3.Helper_Code;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.ahsapptest3.Settings;

public class AutoAdjustTextSize_TextView extends androidx.appcompat.widget.AppCompatTextView {
    public AutoAdjustTextSize_TextView(Context context) {
        super(context);
        initTextSize(context);
    }

    public AutoAdjustTextSize_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTextSize(context);
    }

    public AutoAdjustTextSize_TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTextSize(context);
    }

    private float originalTextSize;
    public void initTextSize(Context context) {
        Settings settings = new Settings(context.getApplicationContext());
        originalTextSize = settings.convertPXtoSP(getTextSize());
        this.setTextSize(Settings.convertTextSize(originalTextSize));
    }

    public void updateTextSize() {
        this.setTextSize(Settings.convertTextSize(originalTextSize));
    }


}
