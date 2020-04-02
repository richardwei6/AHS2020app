package com.example.ahsapptest2;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
        View view = inflater.inflate(R.layout.article_blurb_template_layout, container, false); // Inflate the layout for this fragment
        setImage(view);
        setTitle(view);
        return view;
    }

    public static Article_Blurb_Template newInstance(String title, String filepath) //filepath of image
    {
        Article_Blurb_Template thisFrag = new Article_Blurb_Template();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("image_filepath",filepath);

        thisFrag.setArguments(args);
        return thisFrag;

    }

    public void setImage(View view){
        //TODO: implement this somehow
    }
    public void setTitle(View view){
        TextView title = (TextView) view.findViewById(R.id.Title);
        assert getArguments() != null;
        title.setText(getArguments().getString("title"));
    }
}
