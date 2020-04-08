package com.example.ahsapptest2.Old_Code;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahsapptest2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Upcoming_Events extends Fragment {
    private View view;

    public Upcoming_Events() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.old_upcoming_events_template__layout, container, false);
        setDate(view);
        setTitle(view);
        setSummary(view);
        return view;
    }

    public static Upcoming_Events newInstance(String date, String title, String summary)
    {
        Upcoming_Events thisFrag = new Upcoming_Events();
        Bundle args = new Bundle();
        args.putString("date",date);
        args.putString("title",title);
        args.putString("summary",summary);

        thisFrag.setArguments(args);
        return thisFrag;
    }
    public void setDate(View view){
        TextView date = (TextView) view.findViewById(R.id.Date);
        date.setText(getArguments().getString("date"));
    }
    public void setTitle(View view){
        TextView title = (TextView) view.findViewById(R.id.Title);
        title.setText(getArguments().getString("title"));
    }
    public void setSummary(View view){
        TextView summary = (TextView) view.findViewById(R.id.Summary);
        summary.setText(getArguments().getString("summary"));
    }

}
