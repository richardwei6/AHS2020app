package com.example.ahsapptest2.Home_Page_Code;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahsapptest2.R;


/**
 * A simple blurb template for the main page, with only a title and presumably an image that will be added later.
 * TODO: Add image adding capabilities
 */
public class Article_Blurb_Template extends Fragment {

    public Article_Blurb_Template() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page__article_blurb_template_layout, container, false); // Inflate the layout for this fragment
        setImage(view);
        setTitle(view);
        return view;
    }

    public static Article_Blurb_Template newInstance(String title, String filepath/*, /*int width*/) //filepath of image
    {
        Article_Blurb_Template thisFrag = new Article_Blurb_Template();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("image_filepath",filepath);
        /*args.putInt("width", width);*/

        thisFrag.setArguments(args);
        return thisFrag;

    }

    public void setImage(View view){
        //TODO: implement this somehow
    }
    public void setTitle(View view){
        TextView title = (TextView) view.findViewById(R.id.Title);
        title.setText(getArguments().getString("title"));
    }

}
