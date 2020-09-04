package com.hsappdev.ahs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hsappdev.ahs.misc.Helper;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class News_Header extends Fragment{

    public News_Header() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.header_layout, container, false);
        TextView timeText = view.findViewById(R.id.header__timeText);

        // set the current month and date
        String date = Helper.getDateFromTime("MMM d", Calendar.getInstance().getTimeInMillis()/1000L);
        timeText.setText(date);

        return view;
    }

}
