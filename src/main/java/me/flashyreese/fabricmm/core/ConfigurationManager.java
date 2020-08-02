package me.flashyreese.fabricmm.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
            writeJson(REPOSITORIES, repositories);
        }
    }

    public ArrayList<String> getRepositories(){
        return readJson(REPOSITORIES, new TypeToken<ArrayList<String>>(){}.getType());
    }

    private <T> T readJson(File file, Type type) {
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

    private void writeJson(File file, Object object) {
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

    public static ConfigurationManager getInstance(){
        if(instance == null) instance = new ConfigurationManager();
        return instance;
    }
}
