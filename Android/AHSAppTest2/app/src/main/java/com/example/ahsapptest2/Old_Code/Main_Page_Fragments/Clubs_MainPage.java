package com.example.ahsapptest2.Old_Code.Main_Page_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahsapptest2.R;


public class Clubs_MainPage extends Fragment {

    public Clubs_MainPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.clubs__main_page_layout, container, false);

        return view;
    }

}
