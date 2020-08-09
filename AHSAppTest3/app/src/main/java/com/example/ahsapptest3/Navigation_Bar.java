package com.example.ahsapptest3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


public class Navigation_Bar extends Fragment {

    private boolean is_nav_bar_up = true; // assume nav bar is up when fragment created, which should be the case anyway
    private View view;
    private final static int scrollAnimBuffer = 4; // so the animation doesn't repeat on overly slight changes

    public Navigation_Bar() {
        // Required empty public constructor
    }

    private Navigation navigation;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigation = (Navigation) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nav_bar, container, false);
        ImageView
                homeBtn = view.findViewById(R.id.nav_bar_home_button),
                inner_bulletin_btn = view.findViewById(R.id.nav_bar_inner_bulletin_button),
                savedBtn = view.findViewById(R.id.nav_bar_bookmarks_button),
                settingsBtn = view.findViewById(R.id.nav_bar_settings_button);
        View bulletinBtnLayout = view.findViewById(R.id.nav_bar_bulletin_button);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.goToHome();
            }
        });
        bulletinBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.goToBulletin();
            }
        });
        savedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.goToSaved();
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.goToSettings();
            }
        });
        switch (navigation.getHighlightOption()){
            case HOME:
                homeBtn.setColorFilter(ContextCompat.getColor(this.getContext(),R.color.DarkCrimson_1A0303));
                break;
            case BULLETIN:
                inner_bulletin_btn.setColorFilter(ContextCompat.getColor(this.getContext(),R.color.DarkCrimson_1A0303));
                break;
            case SAVED:
                savedBtn.setColorFilter(ContextCompat.getColor(this.getContext(),R.color.DarkCrimson_1A0303));
                break;
            case SETTINGS:
                settingsBtn.setColorFilter(ContextCompat.getColor(this.getContext(),R.color.DarkCrimson_1A0303));
                break;
            case NONE:
            default:
        }
        return view;
    }

    /**
     * You can only access the view after activity created; else view is null
     * @param savedInstanceState uh doesn't really matter for this app
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        final View scrollingView = getActivity().findViewById(navigation.getScrollingViewId());

        final ViewGroup navBar= view.findViewById(R.id.nav_bar_ConstraintLayout);

        if (scrollingView instanceof ScrollView || scrollingView instanceof NestedScrollView)
        {

            scrollingView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                float y = 0;
                @Override
                public void onScrollChanged() {
                    if(scrollingView.getScrollY() > y + scrollAnimBuffer) // scroll down, 2 is the buffer
                    {
                        slideDown(navBar);
                        is_nav_bar_up = false;
                    }
                    else if (scrollingView.getScrollY() < y - scrollAnimBuffer)
                    {
                        slideUp(navBar);
                        is_nav_bar_up = true;
                    }
                    y= scrollingView.getScrollY();
                }
            });
        }
        else if (scrollingView instanceof RecyclerView)
        {

            ((RecyclerView) scrollingView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                float y = 0;
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int new_y = ((RecyclerView) scrollingView).computeVerticalScrollOffset();
                    if(new_y > y + scrollAnimBuffer) // scroll down, 2 is the buffer
                    {
                        slideDown(navBar);
                        is_nav_bar_up = false;
                    }
                    else if (new_y < y - scrollAnimBuffer)
                    {
                        slideUp(navBar);
                        is_nav_bar_up = true;
                    }
                    y= new_y;
                }
            });
        }

    }

    public void slideUp(View view){
        if(!is_nav_bar_up)
        {
            view.animate().translationY(0).setDuration(500);
            /*view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);*/
        }

    }


    // slide the view from its current position to below itself
    public void slideDown(View view){
        if(is_nav_bar_up)
        {
            view.animate().translationY(view.getHeight()).setDuration(500);
            /*TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            view.startAnimation(animate);*/
        }

    }
}
