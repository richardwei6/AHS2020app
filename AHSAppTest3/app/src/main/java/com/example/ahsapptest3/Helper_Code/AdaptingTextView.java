package com.example.ahsapptest3.Helper_Code;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;


/**
 * @link https://stackoverflow.com/questions/11243867/android-last-line-of-textview-cut-off
 * TextView that dynamically adjusts the number of lines
 */
public class AdaptingTextView extends AutoAdjustTextSize_TextView {
    private static final String TAG = "AdaptingTextView";
    public AdaptingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AdaptingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptingTextView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        // set fitting lines to prevent cut text
        final int fittingLines = h / this.getLineHeight();


        if (fittingLines > 0) {
            this.setMaxLines(fittingLines);
            // the below is needed to fix ellipses not appearing for some reason
            ViewTreeObserver vto = this.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver obs = getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                    if(getLineCount() > fittingLines){
                        int lineEndIndex = getLayout().getLineEnd(fittingLines -1) ;
                        if(lineEndIndex - 3 >= 0){String text = getText().subSequence(0, lineEndIndex-3)+"...";
                        setText(text);}
                        //Log.d(TAG, " NewText\t" + text);
                    }
                }
            });
            //Log.d(TAG, String.valueOf(this.getMaxLines()));
        }
        else
            setText(""); // don't display anything
    }

}