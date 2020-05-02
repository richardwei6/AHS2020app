package com.example.ahsapptest3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Bulletin_Template extends Fragment {

    private final static String BULLETIN_INFO_KEY = "1";
    public Bulletin_Template()
    {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template__bulletin_display, container, false);
        if(getArguments()==null)
            return view;
        Bulletin_Info info = getArguments().getParcelable(BULLETIN_INFO_KEY);
        info.setTitle_toView((TextView) view.findViewById(R.id.bulletin_template_TitleText));
        info.setBodyText_toView((TextView) view.findViewById(R.id.bulletin_template_BodyText));
        info.setDate_toView((TextView) view.findViewById(R.id.bulletin_template_DateText));
        info.setImageType_toView((ImageView) view.findViewById(R.id.bulletin_template_ImageView));

        return view;
    }

    public static Bulletin_Template newInstanceOf(Bulletin_Info bulletin_info)
    {
        Bulletin_Template thisFrag = new Bulletin_Template();
        Bundle args = new Bundle();
        args.putParcelable(BULLETIN_INFO_KEY,bulletin_info);
        thisFrag.setArguments(args);

        return thisFrag;
    }
}
