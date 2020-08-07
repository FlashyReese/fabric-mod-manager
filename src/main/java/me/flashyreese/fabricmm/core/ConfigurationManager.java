package me.flashyreese.fabricmm.core;

import java.io.*;

public class ConfigurationManager {

    private static ConfigurationManager instance;

    public final File ICON_CACHE_DIR = new File(".cache/icons/");
    public final File MOD_CACHE_DIR = new File(".cache/mods/");
    public final File REPOSITORY_CACHE_DIR = new File(".cache/repositories/");
    public final File CONFIG_DIR = new File("config/");

    public ConfigurationManager() {
        try {
            onInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onInit() throws IOException {
        if(!ICON_CACHE_DIR.exists()){
            ICON_CACHE_DIR.mkdirs();
        }
        if(!MOD_CACHE_DIR.exists()){
            MOD_CACHE_DIR.mkdirs();
        }
        if(!REPOSITORY_CACHE_DIR.exists()){
            REPOSITORY_CACHE_DIR.mkdirs();
        }
        if(!CONFIG_DIR.exists()){
            CONFIG_DIR.mkdirs();
        }
    }

    public static ConfigurationManager getInstance(){
        if(instance == null) instance = new ConfigurationManager();
        return instance;
    }
}
