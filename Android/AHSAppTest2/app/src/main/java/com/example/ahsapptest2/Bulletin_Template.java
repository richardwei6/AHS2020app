package com.example.ahsapptest2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Bulletin_Template extends Fragment {

    public Bulletin_Template() {
        // Required empty public constructor
    }

    Bulletin_Page_Fragment.BulletinType type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bulletin__bulletin_template_layout, container, false);
        setTitleText(view);
        setBodyText(view);
        setDateText(view);
        setType();

        int imageID;
        switch(getType())
        {
            case SENIORS:
                imageID = R.drawable.bulletin_seniors;
                break;
            case EVENTS:
                imageID = R.drawable.bulletin_events;
                break;
            case COLLEGE:
                imageID = R.drawable.bulletin_college;
                break;
            case REFERENCE:
                imageID = R.drawable.bulletin_reference;
                break;
            case ATHLETICS:
                imageID = R.drawable.bulletin_athletics;
                break;
            default:
                imageID = R.drawable.bulletin_other;
                break;
        }
        setImage(view,imageID);

        return view;
    }

    public static Bulletin_Template newInstance(String title, String date, String bodyText, Bulletin_Page_Fragment.BulletinType type)
    {
        Bulletin_Template thisFrag = new Bulletin_Template();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("bodyText",bodyText);
        args.putString("date",date);
        args.putSerializable("BulletinType", type);

        thisFrag.setArguments(args);
        return thisFrag;
    }

    public void setTitleText(View view)
    {
        TextView titleText = view.findViewById(R.id.bulletin_template_TitleText);
        titleText.setText(this.getArguments().getString("title"));
    }

    public void setImage(View view, int imageID)
    {
        ImageView image = view.findViewById(R.id.bulletin_template_ImageView);
        image.setImageResource(imageID);
    }

    public void setBodyText(View view)
    {
        TextView titleText = view.findViewById(R.id.bulletin_template_BodyText);
        titleText.setText(this.getArguments().getString("bodyText"));
    }

    public void setDateText(View view)
    {
        TextView titleText = view.findViewById(R.id.bulletin_template_DateText);
        titleText.setText(this.getArguments().getString("date"));
    }

    public void setType()
    {
        type = (Bulletin_Page_Fragment.BulletinType) this.getArguments().getSerializable("BulletinType");
    }

    public Bulletin_Page_Fragment.BulletinType getType()
    {
        return type;
    }
}
