package com.example.ahsapptest3;

import androidx.annotation.Nullable;

public class SavedHolder {
    public static final int ARTICLE_KEY = 0;
    public static final int BULLETIN_ARTICLE_KEY = 1;
    private Article article;
    private Bulletin_Article bulletin_article;
    private Option option;

    public SavedHolder(Article article){
        this.article = article;
        option = Option.ARTICLE;
    }
    public SavedHolder(Bulletin_Article bulletin_article){
        this.bulletin_article = bulletin_article;
        option = Option.BULLETIN_ARTICLE;
    }

    @Nullable
    public Article getArticle() {
        return article;
    }
    @Nullable
    public Bulletin_Article getBulletin_article() {
        return bulletin_article;
    }

    /**
     * whole point is to avoid null checks
     * @return
     */
    public Option getOption() {
        return option;
    }
    public enum Option{
        ARTICLE(ARTICLE_KEY), BULLETIN_ARTICLE(BULLETIN_ARTICLE_KEY);
        private int num;

        Option(int i){
            this.num = i;
        }

        public int getNum() {
            return num;
        }

        @Nullable
        public static Option getOptionFromInteger(int i){
            for(Option option: values()) {
                if(option.getNum() == i)
                    return option;
            }
            return null;
        }
    }
}
