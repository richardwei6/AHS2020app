package com.hsappdev.ahs;

import androidx.annotation.Nullable;

public class Article_or_BulletinHolder {
    public static final int ARTICLE_KEY = 0;
    public static final int BULLETIN_ARTICLE_KEY = 1;
    private Article article;
    private Bulletin_Article bulletin_article;
    private final Option option;

    public Article_or_BulletinHolder(Article article){
        this.article = article;
        option = Option.ARTICLE;
    }
    public Article_or_BulletinHolder(Bulletin_Article bulletin_article){
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
        private final int num;

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
