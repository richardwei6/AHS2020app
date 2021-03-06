package com.example.ahsapptest3;

import androidx.annotation.Nullable;

public class Notif_Data {
    enum SpecialType {
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
    private String ID, title, body;
    private int category;
    private String article_ID;
    private Article article;
    private boolean notified;

    public Notif_Data(String ID, long time, String title, String body, int category, String article_ID, boolean notified) {
        this.ID = ID;
        this.time = time;
        this.title = title;
        this.body = body;
        this.category = category;
        this.article_ID = article_ID;
        this.notified = notified;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
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
    @Nullable public Article getArticle() {
        return article;
    }
    public void setArticle(@Nullable Article article) { this.article = article; }
    public String getArticle_ID() { return article_ID; }
    public void setArticle_ID(String article_ID) { this.article_ID = article_ID; }
    public int getCategory() {
        return category;
    }
    public void setCategory(int category) {
        this.category = category;
    }
    public boolean isNotified() {
        return notified;
        /*if(article == null)
            return notified;
        return article.isNotified();*/
    }
    public void setNotified(boolean notified) {
        this.notified = notified;
        /*if(article == null)
            this.notified = notified;
        else
            article.setNotified(notified);*/
    }

    public String getType()
    {
        if(article!= null)
            return article.getType().getName();
        return SpecialType.ALERTS.getName();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Notif_Data)
            return ID.equals(((Notif_Data) obj).getID());
        return super.equals(obj);
    }
}
