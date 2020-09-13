package me.flashyreese.common.i18n;

public class I18nText {
    private final String key;

    public I18nText(String key) {
        this.key = key;
    }

    public String toString() {
        return I18nManager.translate(key);
    }
}
