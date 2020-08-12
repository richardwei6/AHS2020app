package com.example.ahsapptest3.HomePage_News;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ahsapptest3.Article;
import com.example.ahsapptest3.ArticleDatabase;
import com.example.ahsapptest3.ArticleNavigation;
import com.example.ahsapptest3.Article_Slim;
import com.example.ahsapptest3.Misc.Helper;
import com.example.ahsapptest3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Featured_Display extends Fragment {

    private final static String ARTICLE_KEY = "1";
    private ArticleNavigation navigation;

    public Featured_Display() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigation = (ArticleNavigation) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_featured_template, container, false);
        if (getArguments() == null)
            return view;

        final Article_Slim article = getArguments().getParcelable(ARTICLE_KEY);
        ((TextView) view.findViewById(R.id.template_featured__title_Text)).setText( article.getTitle());

        Helper.setTimeText_toView((TextView) view.findViewById(R.id.template_featured__updated_Text),
                Helper.TimeFromNow(article.getTimeUpdated())
        );

        String imagePaths = article.getImagePath();
        ImageView imageView = view.findViewById(R.id.template_featured__ImageView);
        if(imagePaths != null && imagePaths.length() > 0)
            Helper.setImageFromUrl_CenterCrop_FullSize(
                    imageView,
                    imagePaths
            );
        else
            imageView.setImageResource(R.drawable.image_bg);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article1 = ArticleDatabase.getInstance(getContext()).getArticleById(article.getID());
                if(article1 != null)
                navigation.onItemClicked(article1);
            }
        });

        TextView typeText = view.findViewById(R.id.news_featured_typeText);
        typeText.setText(article.getType().getName());

        return view;
    }

    public static Featured_Display newInstanceOf(Article_Slim article)
    {
        Featured_Display thisFrag = new Featured_Display();

        Bundle args = new Bundle();
        args.putParcelable(ARTICLE_KEY,article);
        thisFrag.setArguments(args);

        return thisFrag;
    }

}
