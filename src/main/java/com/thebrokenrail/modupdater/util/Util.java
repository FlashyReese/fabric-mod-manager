package com.thebrokenrail.modupdater.util;

import me.flashyreese.fabricmm.schema.ModUpdaterConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {
    public static String urlToString(String urlStr) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        URL url = new URL(urlStr);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }

        return stringBuilder.toString();
    }

    public static final String JAR_EXTENSION = ".jar";

    public static String getVersionFromFileName(String fileName) {
        while (!Character.isDigit(fileName.charAt(0))) {
            int index = fileName.indexOf("-");
            fileName = fileName.substring(index != -1 ? index + 1 : 0);
        }
        if (fileName.endsWith(JAR_EXTENSION)) {
            fileName = fileName.substring(0, fileName.length() - JAR_EXTENSION.length());
        }
        return fileName;
    }


    private static boolean isVersionCompatible(String versionStr, char prefix, boolean strict) {
        return true;
    }

    public static boolean isVersionCompatible(String id, String versionStr, boolean strict) {
        return isVersionCompatible(versionStr, '+', strict) || isVersionCompatible(versionStr, '-', strict);
    }

    public static boolean isFileCompatible(String fileName) {
        return !fileName.endsWith("-dev" + JAR_EXTENSION) && !fileName.endsWith("-sources" + JAR_EXTENSION) && !fileName.endsWith("-sources-dev" + JAR_EXTENSION) && fileName.endsWith(JAR_EXTENSION);
    }

    public static ModUpdaterConfig getHardcodedConfig(String modID) {
        ModUpdaterConfig config = new ModUpdaterConfig();
        switch (modID) {
            case "fabric": {
                config.setStrategy("curseforge");
                config.setProjectID(306612);
                config.setStrict(false);
                return config;
            }
            case "modmenu": {
                config.setStrategy("curseforge");
                config.setProjectID(308702);
                config.setStrict(false);
                return config;
            }
            default: {
                return null;
            }
        }
    }
}
