package me.flashyreese.fabricmm.core;

import com.google.gson.reflect.TypeToken;
import me.flashyreese.util.FileUtil;

import java.io.*;
import java.util.ArrayList;

public class ConfigurationManager {

    private static ConfigurationManager instance;

    public final File ICON_CACHE_DIR = new File(".cache/icons/");
    public final File MOD_CACHE_DIR = new File(".cache/mods/");
    public final File REPOSITORY_CACHE_DIR = new File(".cache/repositories/");
    public final File CONFIG_DIR = new File("config/");

    private final File REPOSITORIES = new File(CONFIG_DIR, "repositories.json");

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
        if(!REPOSITORIES.exists()){
            ArrayList<String> repositories = new ArrayList<String>();
            repositories.add("https://raw.githubusercontent.com/FlashyReese/fabric-mod-repository/master/central.json");
            FileUtil.writeJson(REPOSITORIES, repositories);
        }
    }

    public ArrayList<String> getRepositories(){
        return FileUtil.readJson(REPOSITORIES, new TypeToken<ArrayList<String>>(){}.getType());
    }

    public static ConfigurationManager getInstance(){
        if(instance == null) instance = new ConfigurationManager();
        return instance;
    }
}
