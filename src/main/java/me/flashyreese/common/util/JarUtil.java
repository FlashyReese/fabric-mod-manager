package me.flashyreese.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

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

    public static String readTextFile(String resource) throws IOException {
        InputStream stream = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(resource)).openStream();
        final BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String string = br.lines().collect(Collectors.joining());
        br.close();
        return string;
    }
}
