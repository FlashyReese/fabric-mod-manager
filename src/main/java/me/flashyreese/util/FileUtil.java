package me.flashyreese.util;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;

public class FileUtil {
    public static <T> T readJson(File file, Type type) {
        Gson gson = new Gson();
        FileReader fileReader = null;
        BufferedReader buffered = null;
        try {
            fileReader = new FileReader(file);
            buffered = new BufferedReader(fileReader);
            return gson.fromJson(fileReader, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                buffered.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void writeJson(File file, Object object) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(object);
            FileWriter fw = new FileWriter(file);
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static File changeExtension(File file, String extension) {
        String filename = file.getName();

        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf('.'));
        }
        filename += "." + extension;

        File newFile = file;
        if(file.renameTo(new File(file.getParentFile(), filename))){
            newFile = new File(file.getParentFile(), filename);
        }
        return newFile;
    }
}
