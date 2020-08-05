package me.flashyreese.common.i18n;

public class ParsableTranslatableText {

    private String key;
    private String[] args;

    public ParsableTranslatableText(String key, String... args){
        this.key = key;
        this.args = args;
    }

    public String toString(){
        return new ParsableText(new TranslatableText(key), args).toString();
    }
}
