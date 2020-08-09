package me.flashyreese.common.i18n;

public class ParsableText{

    private final String parsableText;
    private final String[] args;

    public ParsableText(String parsableText, String... args){
        this.parsableText = parsableText;
        this.args = args;
    }

    public ParsableText(I18nText i18nText, String... args){
        this.parsableText = i18nText.toString();
        this.args = args;
    }

    @Override
    public String toString() {
        return format(parsableText, args);
    }

    private String format(String text, String... args) {
        for(int i = 0; i < args.length; i++) {
            if(!text.contains("{" + ( i + 1) + "}")) {
                continue;
            }
            text = text.replaceAll("\\{" + (i+1) + "}", args[i]);
        }
        return text;
    }

}
