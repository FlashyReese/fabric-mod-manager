package me.flashyreese.common.i18n;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.common.util.JarUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class I18nManager {

    private static final Logger log = LogManager.getLogger(I18nManager.class);
    private final String languageDirectory;
    private final ArrayList<Locale> availableLocales;
    private static HashMap<String, String> currentLocale;

    public I18nManager(String languageDirectory) throws Exception {
        log.info("Building I18nManager...");
        this.languageDirectory = languageDirectory;
        this.availableLocales = findAvailableLocales();
        log.info("I18nManager built!");
    }

    public void setLocale(Locale l) throws Exception {
        for (Locale locale: getAvailableLocales()){
            if (locale == l){
                currentLocale = getTranslationsForLocale(locale);
                break;
            }
        }
        if (currentLocale == null){
            currentLocale = getTranslationsForLocale(Locale.US);
        }
    }

    private ArrayList<Locale> findAvailableLocales() throws Exception {
        ArrayList<Locale> availableLocales = new ArrayList<Locale>();
        final File jarFile = new File(I18nManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if(jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (name.startsWith(languageDirectory + "/") && name.endsWith(".json")) {
                    Locale locale = getLocale(JarUtil.getFileName(entry));
                    if(locale != null){
                        availableLocales.add(locale);
                    }
                }
            }
            jar.close();
            return availableLocales;
        } else {
            final URL url = ClassLoader.getSystemClassLoader().getResource(languageDirectory);
            if (url != null) {
                try {
                    final File languages = new File(url.toURI());
                    for (File language : Objects.requireNonNull(languages.listFiles())) {
                        Locale locale = getLocale(FileUtil.getFileName(language));
                        if(locale != null){
                            availableLocales.add(locale);
                        }
                    }
                    return availableLocales;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private HashMap<String, String> getTranslationsForLocale(Locale locale) throws Exception {
        HashMap<String, String> translations = new HashMap<String, String>();
        final File jarFile = new File(I18nManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if(jarFile.isFile()) {
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();
                if (name.startsWith(languageDirectory + "/") && name.endsWith(".json")) {
                    if (JarUtil.getFileName(entry).equalsIgnoreCase(locale.toString())){
                        translations.putAll(new Gson().fromJson(new BufferedReader(new InputStreamReader(jar.getInputStream(entry), StandardCharsets.UTF_8)), new TypeToken<HashMap<String, String>>(){}.getType()));
                        break;
                    }
                }
            }
            jar.close();
            return translations;
        } else {
            final URL url = ClassLoader.getSystemClassLoader().getResource(languageDirectory);
            if (url != null) {
                try {
                    final File languages = new File(url.toURI());
                    for (File language : Objects.requireNonNull(languages.listFiles())) {
                        if (FileUtil.getFileName(language).equalsIgnoreCase(locale.toString())){
                            translations.putAll(new Gson().fromJson(new BufferedReader(new InputStreamReader(new FileInputStream(language), StandardCharsets.UTF_8)), new TypeToken<HashMap<String, String>>(){}.getType()));
                            break;
                        }
                    }
                    return translations;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public ArrayList<Locale> getAvailableLocales() {
        return availableLocales;
    }

    public String getLocaleDisplayName(String tag) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if(locale.toString().equalsIgnoreCase(tag)){
                return locale.getDisplayName();
            }
        }
        return tag;
    }

    public Locale getLocale(String tag) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if(locale.toString().equalsIgnoreCase(tag)){
                return locale;
            }
        }
        return null;
    }

    protected static String translate(String key) {
        if (currentLocale != null && currentLocale.containsKey(key)){
            return currentLocale.get(key);
        }
        return key;
    }
}