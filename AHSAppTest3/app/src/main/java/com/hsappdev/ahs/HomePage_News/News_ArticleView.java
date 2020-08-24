package com.hsappdev.ahs.HomePage_News;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsappdev.ahs.Article_Slim;
import com.hsappdev.ahs.Misc.Helper;
import com.hsappdev.ahs.R;

public class News_ArticleView extends FrameLayout{

    public News_ArticleView(Context context, Article_Slim article) {
        super(context);
        init(context, article);
    }

    private void init(Context context, Article_Slim article) {

        inflate(context, R.layout.news_article_template, this);
        TextView summaryText = findViewById(R.id.article_display__summary_Text),
                titleText = findViewById(R.id.article_display__title_Text),
                timeText = findViewById(R.id.article_display__time_updated_Text);
        Helper.setHtmlParsedText_toView(summaryText, article.getStory());
        titleText.setText(article.getTitle());
        Helper.setTimeText_toView(timeText,
                Helper.TimeFromNow(article.getTimeUpdated()));

        ImageView imageView = findViewById(R.id.article_display__imageView);
        String imagePath = article.getImagePath();
        if(imagePath != null && imagePath.length() > 0)
            Helper.setImageFromUrl_CenterCrop_FullSize(
                    imageView,
                    imagePath
            );
        else
            imageView.setImageResource(R.drawable.image_bg);
    }
}
