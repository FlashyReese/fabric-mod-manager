package me.flashyreese.fabricmm.core;

import java.io.File;

public class CacheManager {

    private static CacheManager instance;

    public final File ICON_CACHE_DIR = new File(".cache/icons/");

    public CacheManager(){
        onInit();
    }

    public void onInit(){
        if(!ICON_CACHE_DIR.exists()){
            ICON_CACHE_DIR.mkdirs();
        }
    }

    public static CacheManager getInstance(){
        if(instance == null) instance = new CacheManager();
        return instance;
    }
}
