package com.example.ahsapptest2.Home_Page_Code;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahsapptest2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Page_Fragment extends Fragment {

    public Home_Page_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_page_layout, container, false);
    }
}
