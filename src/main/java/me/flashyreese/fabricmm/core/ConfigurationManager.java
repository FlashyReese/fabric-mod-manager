package me.flashyreese.fabricmm.core;

import com.squareup.moshi.Types;
import me.flashyreese.common.util.FileUtil;

import java.io.*;

public class ConfigurationManager {

    private static ConfigurationManager instance;

    public final File ICON_CACHE_DIR = new File(".cache/icons/");
    public final File MOD_CACHE_DIR = new File(".cache/mods/");
    public final File REPOSITORY_CACHE_DIR = new File(".cache/repository/");
    public final File CONFIG_DIR = new File("config/");
    public final File FMM_CONFIG = new File(CONFIG_DIR, "settings.json");

    private FMMSettings fmmSettings;

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
        if(!FMM_CONFIG.exists()){
            fmmSettings = ConfigurationBuilder.createFMMDefaultSettings(FMM_CONFIG);
        }else{
            fmmSettings = FileUtil.readJson(FMM_CONFIG, FMMSettings.class);
        }
    }

    public static ConfigurationManager getInstance(){
        if(instance == null) instance = new ConfigurationManager();
        return instance;
    }

    public FMMSettings getSettings() {
        return fmmSettings;
    }

    public void saveSettings(){
        FileUtil.writeJson(FMM_CONFIG, fmmSettings, FMMSettings.class);
    }
}
