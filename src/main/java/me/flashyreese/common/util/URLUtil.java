package me.flashyreese.common.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLUtil {

    public static String getFileName(String url) throws MalformedURLException {
        URL validUrl = new URL(url);
        String validPath = validUrl.getPath();
        return validPath.substring(validPath.lastIndexOf('/') + 1, validPath.lastIndexOf('.'));
    }

    public static String getFileNameWithExtension(String url) throws MalformedURLException {
        URL validUrl = new URL(url);
        String validPath = validUrl.getPath();
        return validPath.substring(validPath.lastIndexOf('/') + 1);
    }

    public static int getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }
}
