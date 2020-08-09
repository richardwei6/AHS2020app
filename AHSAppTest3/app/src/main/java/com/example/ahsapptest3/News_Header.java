package com.example.ahsapptest3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class News_Header extends Fragment implements NotifBtn.Navigation{

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
        String month = (String) android.text.format.DateFormat.format("MMMM", new Date());
        String date = (String) android.text.format.DateFormat.format("dd", new Date());
        timeText.setText(month+" "+date);


        return view;
    }

    @Override
    public void goToNotif() {
        final Context context = this.getContext();
        Intent intent = new Intent(context, Notif_Activity.class);
        context.startActivity(intent);
    }
}
