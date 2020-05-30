package com.example.ahsapptest3.HomePage_News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Featured_Display extends Fragment {


    private final static String ARTICLE_KEY = "1";
    public Featured_Display() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.template__featured_display, container, false);
        Article article = (Article) getArguments().getParcelable(ARTICLE_KEY);
        article.setTitleText_toView( (TextView) view.findViewById(R.id.template_featured__title_Text));
        article.setBookmarked_toView((ImageButton) view.findViewById(R.id.template_featured__bookmarked_button));
        article.setBookMarkListener_toView((ImageButton) view.findViewById(R.id.template_featured__bookmarked_button));
        article.setTime_Hours_UpdatedText_toView((TextView) view.findViewById(R.id.template_featured__updated_Text));
        article.setImage_toView((ImageView) view.findViewById(R.id.template_featured__ImageView));
    article.setArticleListener_toView(view, getContext());
        return view;
    }

    public static Featured_Display newInstanceOf(Article article)
    {
        Featured_Display thisFrag = new Featured_Display();

        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_KEY,article);
        thisFrag.setArguments(args);

        return thisFrag;
    }


}
