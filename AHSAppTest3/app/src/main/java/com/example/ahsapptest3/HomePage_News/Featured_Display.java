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
import com.example.ahsapptest3.Helper_Code.Helper;
import com.example.ahsapptest3.R;

import java.util.concurrent.TimeUnit;

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
        if (getArguments() == null)
            return view;

        Article article = (Article) getArguments().getParcelable(ARTICLE_KEY);
        Helper.setText_toView( (TextView) view.findViewById(R.id.template_featured__title_Text), article.getTitle());

        ImageView bookmarkButton = view.findViewById(R.id.template_featured__bookmarked_button);
        Helper.setBookmarked_toView(bookmarkButton,article.isBookmarked());
        Helper.setBookMarkListener_toView(bookmarkButton, article);

        Helper.setTimeText_toView((TextView) view.findViewById(R.id.template_featured__updated_Text),
                Helper.TimeFromNow(article.getTimeUpdated()),
                TimeUnit.HOURS);

        Helper.setImage_toView_fromUrl((ImageView) view.findViewById(R.id.template_featured__ImageView),article.getImagePaths()[0]);

        Helper.setArticleListener_toView(view, article);

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
