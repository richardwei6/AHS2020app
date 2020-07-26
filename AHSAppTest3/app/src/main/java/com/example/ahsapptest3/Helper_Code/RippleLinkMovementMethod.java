package com.example.ahsapptest3.Helper_Code;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class RippleLinkMovementMethod extends LinkMovementMethod {
    private static final String TAG = "RippleLinkMM";
    private View forceRippleView;
    /**
     *
     * @param forceRippleView view to force the ripple
     */
    public RippleLinkMovementMethod(View forceRippleView) {
        super();
        this.forceRippleView = forceRippleView;

    }
    private static RippleLinkMovementMethod sInstance;

    public static RippleLinkMovementMethod getInstance(View parentView)
    {
        if(sInstance == null)
            sInstance = new RippleLinkMovementMethod(parentView);
        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        float x = event.getX(),
                y = event.getY();
        forceRippleAnimation(forceRippleView, x, y);
        return super.onTouchEvent(widget, buffer, event);
    }

    private void forceRippleAnimation(View view, float x, float y)
    {
        Drawable background = view.getBackground();
        Log.d(TAG, Float.toString(x + y));
        Log.d(TAG, background.getClass().toString());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && background instanceof RippleDrawable)
        {
            Log.d(TAG, "is ripple drawable");
            final RippleDrawable rippleDrawable = (RippleDrawable) background;
            rippleDrawable.setHotspot(x,y);
            rippleDrawable.setState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled });
            rippleDrawable.setState(new int[] {  });
        }
    }


}
