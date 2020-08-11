package me.flashyreese.fabricmm.util;

import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.api.schema.repository.Project;
import me.flashyreese.fabricmm.core.ConfigurationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class UserInterfaceUtils {

    public static ImageIcon getImageIconFromCache(Project project) throws Exception {
        File file = new File(ConfigurationManager.getInstance().ICON_CACHE_DIR, String.format("%s.png", project.getId()));
        if(file.exists()){
            return getIconFromFile(file);
        }else{
            try {
                BufferedInputStream in = new BufferedInputStream(new URL(project.getIconUrl()).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                return getIconFromFile(file);
            } catch (Exception e) {
                return getIconFromImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
            }
        }
    }

    public static ImageIcon getIconFromResource(String resource, int scale){
        return getIconFromImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource(resource)), scale, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getGrayScaledIconFromResource(String resource, int scale){
        Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource(resource));
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
        Image mage = Toolkit.getDefaultToolkit().createImage(producer);
        return getIconFromImage(mage, scale, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        return new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getIconFromImage(Image image){
        return new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getIconFromImage(Image image, int scale){
        return new ImageIcon(image.getScaledInstance(scale, scale, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getIconFromImage(Image image, int scale, int hints){
        return new ImageIcon(image.getScaledInstance(scale, scale, hints));
    }

    public static ImageIcon getGrayScaledIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
        Image mage = Toolkit.getDefaultToolkit().createImage(producer);
        return new ImageIcon(mage.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    }

    public static String getEnglishStringList(ArrayList<Object> list){//Fixme: This will need a patch
        StringBuilder line = new StringBuilder();
        if(list == null){
            line.append("None listed");
        }else{
            for(int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof String){
                    String currElement = (String)list.get(i);
                    if(i == 0) {
                        line = new StringBuilder(currElement);
                    }else if(i == list.size() - 1) {
                        line.append(" and ").append(currElement);
                    }else{
                        line.append(", ").append(currElement);
                    }
                }
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

    public static String filterEnvironment(String environment){
        if(environment == null){
            return new I18nText("fmm.filter_environment.null").toString();
        }else if(environment.equals("*")){
            return new I18nText("fmm.filter_environment.both").toString();
        }else if(environment.equals("client")){
            return new I18nText("fmm.filter_environment.client").toString();
        }else{
            return new I18nText("fmm.filter_environment.server").toString();
        }
    }
}
