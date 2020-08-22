package com.hsappdev.ahs.Misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.hsappdev.ahs.R;

public class Bulletin_SelectorView extends FrameLayout{


    public Bulletin_SelectorView(Context context) {
        super(context);
        init();
    }

    public Bulletin_SelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }



    public Bulletin_SelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init() {
        // honestly no clue what to do here lol
    }

    private TextView categoryText;
    private ImageView decoration;
   /* public OnClick action;
*/
    private boolean selected = false;

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bulletin_SelectorView);

        inflate(context, R.layout.bulletin_selection_view_layout, this);
        categoryText = findViewById(R.id.bulletin_selection_text);
        decoration = findViewById(R.id.bulletin_selection_underline);


        categoryText.setText(typedArray.getString(R.styleable.Bulletin_SelectorView_text));

        /*final LinearLayout outerLayout = findViewById(R.id.bulletin_selection_outerLayout);*/

        /*outerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        categoryText.setOnTouchListener(new OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Convert to card view coordinates. Assumes the host view is
                // a direct child and the card view is not scrollable.
                float x = event.getX() + v.getLeft();
                float y = event.getY() + v.getTop();

                // Simulate motion on the card view.
                outerLayout.drawableHotspotChanged(x, y);

                // Simulate pressed state on the card view.
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        {
                            outerLayout.setPressed(true);
                            selected = !selected;
                            initDecoration(selected);
                            if(action != null)
                                action.onClick();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        outerLayout.setPressed(false);
                        break;
                }

                // Pass all events through to the host view.
                return false;
            }
        });*/
        initDecoration();
        typedArray.recycle();
    }

    private void initDecoration()
    {
        if(selected){
            decoration.setVisibility(View.VISIBLE);
            categoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.DarkCrimson_1A0303));
        } else {
            decoration.setVisibility(View.INVISIBLE);
            categoryText.setTextColor(ContextCompat.getColor(getContext(), R.color.GraniteRed_7E6E6A));
        }
    }

    public void initDecoration(boolean selected)
    {
        this.selected = selected;
        initDecoration();
    }
/*
    public boolean getSelected()
    {
        return selected;
    }*/
/*

    public interface OnClick
    {
        void onClick();
    }
*/

}
