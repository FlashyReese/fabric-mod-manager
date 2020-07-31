package me.flashyreese.fabricmm.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserInterfaceUtils {

    public static ImageIcon getIconFromURL(String s) throws MalformedURLException {
        URL url = new URL(s);
        return new ImageIcon(url);
    }

    public static ImageIcon getIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        return new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    }

    public static ImageIcon getGrayScaledIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
        Image mage = Toolkit.getDefaultToolkit().createImage(producer);
        return new ImageIcon(mage.getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    }

    public static String getEnglishStringList(String[] list){
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < list.length; i++) {
            String currElement = list[i];
            if(i == 0) {
                line = new StringBuilder(currElement);
            }else if(i == list.length - 1) {
                line.append(" and ").append(currElement);
            }else{
                line.append(", ").append(currElement);
            }
        }
        return line.toString();
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String hashMapToString(HashMap<String, String> map){
        StringBuilder builder = new StringBuilder();
        //<a href=\"https://github.com/FlashyReese/\">GitHub</a>
        for (Map.Entry<String, String> entry: map.entrySet()){
            builder.append("<a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a>");
        }
        return builder.toString();
    }

    public static String filterEnvironment(String environment){
        if(environment.equals("*")){
            return "Client & Server";
        }else if(environment.equals("client")){
            return "Client";
        }else{
            return "Server";
        }
    }
}
