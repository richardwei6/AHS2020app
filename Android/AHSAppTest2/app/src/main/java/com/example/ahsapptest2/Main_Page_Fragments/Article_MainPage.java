package com.example.ahsapptest2.Main_Page_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest2.R;
import androidx.fragment.app.Fragment;

public class Article_MainPage extends Fragment {

    public Article_MainPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.article_layout, container, false);
    }

}
