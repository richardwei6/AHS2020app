package com.example.ahsapptest3;

import androidx.annotation.Nullable;

public class Notif_Data
{
    enum SpecialType{
        ALERTS("General Alerts");
        private String name;
        SpecialType(String name)
        {
            this.name = name;
        }
        String getName(){
            return name;
        }
    }
    private long time;
    private String title, body;
    private int category;
    private Article article;

    private boolean notified;
    public Notif_Data(long time, String title, String body, int category, @Nullable Article article)
    {
        this.time = time;
        this.title = title;
        this.body = body;
        this.category = category;
        this.article = article;
    }

    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    @Nullable
    public Article getArticle() {
        return article;
    }
    public void setArticle(Article article) {
        this.article = article;
    }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public boolean isNotified() {
        if(article == null)
            return notified;
        return article.isNotified();
    }
    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getType()
    {
        if(article!= null)
            return article.getType().toString();
        return SpecialType.ALERTS.toString();
    }
}
