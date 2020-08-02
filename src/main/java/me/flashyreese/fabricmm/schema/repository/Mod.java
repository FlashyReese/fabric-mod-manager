package me.flashyreese.fabricmm.schema.repository;

import java.util.List;

public class Mod {

    //Used for Mod Manager not actually in json
    private Author author;

    private List<MinecraftVersion> minecraftVersions;
    private String name;
    private String description;
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

    public List<MinecraftVersion> getMinecraftVersions(){
        return minecraftVersions;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl(){
        return iconUrl;
    }

    public String getId(){
        return id;
    }
}
