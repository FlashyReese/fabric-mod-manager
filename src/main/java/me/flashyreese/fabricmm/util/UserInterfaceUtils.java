package me.flashyreese.fabricmm.util;

import me.flashyreese.common.i18n.TranslatableText;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmrf.schema.repository.Mod;

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

public class UserInterfaceUtils {

    public static ImageIcon getImageIconFromCache(Mod mod) throws IOException {//Fixme: baddddddddd
        File file = new File(ConfigurationManager.getInstance().ICON_CACHE_DIR, String.format("%s.png", mod.getId()));
        if(file.exists()){
            return getIconFromFile(file);
        }else{
            try (BufferedInputStream in = new BufferedInputStream(new URL(mod.getIconUrl()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                return getIconFromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getIconFromImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
    }

    public static ImageIcon getIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        return new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    }

    public static ImageIcon getIconFromImage(Image image){
        return new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    }

    public static ImageIcon getGrayScaledIconFromFile(File file) throws IOException {
        Image image = ImageIO.read(file);
        ImageFilter filter = new GrayFilter(true, 50);
        ImageProducer producer = new FilteredImageSource(image.getSource(), filter);
        Image mage = Toolkit.getDefaultToolkit().createImage(producer);
        return new ImageIcon(mage.getScaledInstance(64, 64, Image.SCALE_DEFAULT));
    }

    public static String getEnglishStringList(String[] list){//Fixme: This will need a patch
        StringBuilder line = new StringBuilder();
        if(list == null){
            line.append("None listed");
        }else{
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
            return new TranslatableText("fmm.filter_environment.null").toString();
        }else if(environment.equals("*")){
            return new TranslatableText("fmm.filter_environment.both").toString();
        }else if(environment.equals("client")){
            return new TranslatableText("fmm.filter_environment.client").toString();
        }else{
            return new TranslatableText("fmm.filter_environment.server").toString();
        }
    }
}
