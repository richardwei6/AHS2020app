package com.example.ahsapptest2.Old_Code;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahsapptest2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Club_Template extends Fragment {


    public Club_Template() {
        // Required empty public constructor
    }
    private View view;
    private TextView title;
    private ImageView image;
    private TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.club_template__layout, container, false);
        title = (TextView) view.findViewById(R.id.title_text);
        image = (ImageView) view.findViewById(R.id.Image);
        description = (TextView) view.findViewById(R.id.Description);

        title.setText("hello");
        //image.setImageResource(R.drawable.rounded_yellow);
        description.setText("description");

        // Inflate the layout for this fragment
        return view;
    }

    public void setTitleText(String str)
    {
        title.setText(str);
    }

}
