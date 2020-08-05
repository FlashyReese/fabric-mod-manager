package me.flashyreese.common.i18n;

public class TranslatableText {
    private String key;

    public TranslatableText(String key){
        this.key = key;
    }

    public String toString(){
        return I18nManager.translate(key);
    }
}
