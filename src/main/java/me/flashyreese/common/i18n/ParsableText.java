package me.flashyreese.common.i18n;

public class ParsableText{

    private String parsableText;
    private String[] args;

    public ParsableText(String parsableText, String... args){
        this.parsableText = parsableText;
        this.args = args;
    }

    public ParsableText(TranslatableText translatableText, String... args){
        this.parsableText = translatableText.toString();
        this.args = args;
    }

    @Override
    public String toString() {
        return format(parsableText, args);
    }

    private String format(String text, String... args) {
        for(int i = 0; i < args.length; i++) {
            if(!text.contains("{" + ( i + 1) + "}")) {
                //System.out.println("Missing notation to parse: " + args[i]);
                continue;
            }
            if(text.contains("{" + (i+2) + "}") && i + 1 == args.length){
                //System.out.println("Missing object to parse: {" + (i+2) + "}");
            }
            text = text.replaceAll("\\{" + (i+1) + "}", args[i]);
        }
        return text;
    }

}
