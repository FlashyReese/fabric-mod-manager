package me.flashyreese.common.i18n;

public class ParsableI18nText {

    private final String key;
    private final String[] args;

    public ParsableI18nText(String key, String... args){
        this.key = key;
        this.args = args;
    }

    public String toString(){
        return new ParsableText(new I18nText(key), args).toString();
    }
}
