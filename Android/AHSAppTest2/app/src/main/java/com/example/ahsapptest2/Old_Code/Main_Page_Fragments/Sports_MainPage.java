package com.example.ahsapptest2.Old_Code.Main_Page_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest2.R;


public class Sports_MainPage extends Fragment {

    public Sports_MainPage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.old_sports__main_page_layout, container, false);
    }

}
