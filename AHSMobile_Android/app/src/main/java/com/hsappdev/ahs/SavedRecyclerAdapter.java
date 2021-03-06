package com.hsappdev.ahs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsappdev.ahs.misc.Helper;

import java.util.ArrayList;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    /*private static final String TAG = "ArticleRecyclerAdapter";*/
    private static final int ARTICLE = Article_or_BulletinHolder.Option.ARTICLE.getNum();
    private static final int BULLETIN_ARTICLE = Article_or_BulletinHolder.Option.BULLETIN_ARTICLE.getNum();
    private static final int EMPTY_PLACEHOLDER = 10;

    public final ArrayList<Article_or_BulletinHolder> articles; // ArrayList instead of array in case we want to add or remove stuff (which will happen!)
    private final OnItemClick onItemClick;

    public SavedRecyclerAdapter(ArrayList<Article_or_BulletinHolder> articles, OnItemClick onItemClick) {
        this.articles = new ArrayList<>(articles);
        this.onItemClick = onItemClick;
    }

    public void addItem(Article_or_BulletinHolder article) {
        articles.add(article);
        notifyItemInserted(articles.size() -1);
    }

    public void updateItemRemoved(int position){
        if(position <articles.size()) {
            articles.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(articles.size()  == 0)
            return EMPTY_PLACEHOLDER;
        if(articles.get(position).getOption() == Article_or_BulletinHolder.Option.ARTICLE)
            return ARTICLE;
        if(articles.get(position).getOption() == Article_or_BulletinHolder.Option.BULLETIN_ARTICLE)
            return BULLETIN_ARTICLE;
        throw new IllegalStateException("Unexpected SavedHolder.Option in SavedRecyclerAdapter");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == ARTICLE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_article_template, parent, false);
            return new Article_ViewHolder(view, onItemClick);
        }
        if(viewType == BULLETIN_ARTICLE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bulletin_item_template, parent, false);
            return new Bulletin_Article_ViewHolder(view, onItemClick);
        }
        if(viewType == EMPTY_PLACEHOLDER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_placeholder, parent, false);
            return new NoArticlesViewHolder(view);
        }
        throw new IllegalStateException("Unexpected viewType value");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == ARTICLE) {
            ((Article_ViewHolder) holder).setDetails(articles.get(position).getArticle());
        } else if (getItemViewType(position) == BULLETIN_ARTICLE) {
            ((Bulletin_Article_ViewHolder) holder).setDetails(articles.get(position).getBulletin_article());
        }
    }

    @Override
    public int getItemCount() {
        return (articles.size() > 0) ? articles.size() : 1;
    }

    public static class Article_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView time_updated;
        final TextView titleText;
        final TextView summaryText;
        final TextView typeText;
        final ImageView image;
        final OnItemClick onItemClick;
        Article article;
        public Article_ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            time_updated = itemView.findViewById(R.id.saved_template__time_updated_Text);
            titleText = itemView.findViewById(R.id.saved_template__title_Text);
            summaryText = itemView.findViewById(R.id.saved_template__summary_Text);
            image = itemView.findViewById(R.id.saved_template__imageView);
            typeText = itemView.findViewById(R.id.saved_template_typeText);
            this.onItemClick = onItemClick;
        }

        public void setDetails(Article article) {
            this.article = article;
            itemView.setOnClickListener(this);
            summaryText.setOnClickListener(this);
            time_updated.setText(Helper.getDateFromTime(Helper.shortDatePattern, article.getTimeUpdated()));
            typeText.setText(article.getType().getName());
            titleText.setText(article.getTitle());

            Helper.setHtmlParsedText_toView(summaryText, article.getStory());
            // setImage
            Helper.setImageFromUrl_CenterCrop(image,article.getImagePaths()[0]);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(article, getAdapterPosition());
        }
    }

    public static class Bulletin_Article_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView titleText;
        final TextView bodyText;
        final TextView dateText;
        final TextView typeText;
        final TextView readText;
        final OnItemClick onItemClick;
        Bulletin_Article bulletin_article;
        public Bulletin_Article_ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            titleText = itemView.findViewById(R.id.bulletin_template_TitleText);
            bodyText = itemView.findViewById(R.id.bulletin_template_BodyText);
            dateText = itemView.findViewById(R.id.bulletin_template_DateText);
            typeText = itemView.findViewById(R.id.bulletin_template_typeText);
            readText = itemView.findViewById(R.id.bulletin_template_newText);

            this.onItemClick = onItemClick;
        }

        public void setDetails(Bulletin_Article bulletin_article) {
            this.bulletin_article = bulletin_article;
            itemView.setOnClickListener(this);
            bodyText.setOnClickListener(this);
            ViewTreeObserver observer = bodyText.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(bodyText.getLineCount() >= bodyText.getMaxLines()) {
                        int lineIndex = bodyText.getLayout().getLineEnd(bodyText.getMaxLines() - 1);
                        String text_with_ellipses = bodyText.getText().toString().substring(0, lineIndex - 3) + "...";
                        bodyText.setText(text_with_ellipses);
                    }
                    bodyText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            titleText.setText(bulletin_article.getTitle());
            Helper.setHtmlParsedText_toView(bodyText, bulletin_article.getBodyText());
            dateText.setText(Helper.getDateFromTime(Helper.shortDatePattern, bulletin_article.getTime()));
            readText.setVisibility(View.GONE);
            typeText.setText(bulletin_article.getType().getName());
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClick(bulletin_article, getAdapterPosition());
        }
    }

    public static class NoArticlesViewHolder extends RecyclerView.ViewHolder {
        public NoArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClick
    {
        void onItemClick(Article data, int position);
        void onItemClick(Bulletin_Article data, int position);
    }
}
