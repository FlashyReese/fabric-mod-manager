package me.flashyreese.common.util;

import java.util.jar.JarEntry;

public class JarUtil {

    public static String getFileNameWithExtension(JarEntry entry) throws Exception {
        if(entry.isDirectory()){
            throw new Exception("Invalid JarEntry, it's not a file");
        }
        String path = entry.getName();
        if(path.contains("/")){
            return path.substring(path.lastIndexOf('/') + 1);
        }
        return path;
    }

    public static String getFileName(JarEntry entry) throws Exception {
        String fileNameWithExtension = getFileNameWithExtension(entry);
        if(fileNameWithExtension.contains(".")){
            return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
        }
        return fileNameWithExtension;
    }
}
