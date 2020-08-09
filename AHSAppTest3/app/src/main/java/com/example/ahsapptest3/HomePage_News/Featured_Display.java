package com.example.ahsapptest3.HomePage_News;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.Helper_Code.Helper;
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
        View view = inflater.inflate(R.layout.news_featured_template, container, false);
        if (getArguments() == null)
            return view;

        Article article = getArguments().getParcelable(ARTICLE_KEY);
        ((TextView) view.findViewById(R.id.template_featured__title_Text)).setText( article.getTitle());

        Helper.setTimeText_toView((TextView) view.findViewById(R.id.template_featured__updated_Text),
                Helper.TimeFromNow(article.getTimeUpdated())
        );

        String[] imagePaths = article.getImagePaths();
        ImageView imageView = view.findViewById(R.id.template_featured__ImageView);
        if(imagePaths.length > 0)
            Helper.setImageFromUrl_CenterCrop(
                    imageView,
                    imagePaths[0],
                    false);
        else
            imageView.setImageResource(R.drawable.image_bg);

        Helper.setArticleListener_toView(view, article);

        TextView typeText = view.findViewById(R.id.news_featured_typeText);
        typeText.setText(article.getType().getName());

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
