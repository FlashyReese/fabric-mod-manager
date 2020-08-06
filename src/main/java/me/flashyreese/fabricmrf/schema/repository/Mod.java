package me.flashyreese.fabricmrf.schema.repository;

import java.util.ArrayList;
import java.util.List;

public class Mod {

    //Used for Mod Manager not actually in json
    private Author author;

    private ArrayList<MinecraftVersion> minecraftVersions;
    private String name;
    private String description;
    private String sources;
    private String iconUrl;
    private String id;

    //Used for Mod Manager not actually in json
    public void setAuthor(Author author){
        this.author = author;
    }

    //Used for Mod Manager not actually in json
    public Author getAuthor(){
        return author;
    }

    public ArrayList<MinecraftVersion> getMinecraftVersions(){
        return minecraftVersions;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSources() {
        return sources;
    }

    public String getIconUrl(){
        return iconUrl;
    }

    public String getId(){
        return id;
    }

    public void setMinecraftVersions(ArrayList<MinecraftVersion> minecraftVersions) {
        this.minecraftVersions = minecraftVersions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean containsMinecraftVersion(String ver){
        for (MinecraftVersion minecraftVersion: getMinecraftVersions()){
            if (minecraftVersion.getMinecraftVersion().equalsIgnoreCase(ver)){
                return true;
            }
        }
        return false;
    }

    public MinecraftVersion getMinecraftVersion(String ver){
        for (MinecraftVersion version: getMinecraftVersions()){
            if (version.getMinecraftVersion().equalsIgnoreCase(ver)){
                return version;
            }
        }
        return null;
    }
}
