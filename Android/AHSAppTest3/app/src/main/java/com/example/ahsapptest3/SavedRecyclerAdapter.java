package com.example.ahsapptest3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.util.ArrayList;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final String TAG = "ArticleRecyclerAdapter";
    private static final int ARTICLE = SavedHolder.Option.ARTICLE.getNum();
    private static final int BULLETIN_ARTICLE = SavedHolder.Option.BULLETIN_ARTICLE.getNum();

    public ArrayList<SavedHolder> articles; // ArrayList instead of array in case we want to add or remove stuff (which will happen!)
    private OnItemClick onItemClick;

    public SavedRecyclerAdapter(ArrayList<SavedHolder> articles, OnItemClick onItemClick) {
        this.articles = new ArrayList<>(articles);
        this.onItemClick = onItemClick;
    }

    public void addItem(SavedHolder article) {
        articles.add(article);
        notifyItemChanged(articles.size() -1);
    }

    public void updateItemRemoved(int position){
        articles.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(articles.get(position).getOption() == SavedHolder.Option.ARTICLE)
            return ARTICLE;
        if(articles.get(position).getOption() == SavedHolder.Option.BULLETIN_ARTICLE)
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
        return articles.size();
    }

    public static class Article_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time_updated, titleText, summaryText, typeText;
        ImageView image;
        OnItemClick onItemClick;
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
        TextView titleText, bodyText, dateText, typeText, readText;
        OnItemClick onItemClick;
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
    public interface OnItemClick
    {
        void onItemClick(Article data, int position);
        void onItemClick(Bulletin_Article data, int position);
    }
}
