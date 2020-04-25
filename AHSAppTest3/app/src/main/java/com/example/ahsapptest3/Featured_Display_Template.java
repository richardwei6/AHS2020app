package com.example.ahsapptest3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Featured_Display_Template extends Fragment {

    public Featured_Display_Template() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template__featured_display, container, false);


        return view;
    }


}
