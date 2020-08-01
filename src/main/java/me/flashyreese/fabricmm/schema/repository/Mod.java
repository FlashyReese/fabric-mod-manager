package me.flashyreese.fabricmm.schema.repository;

import java.util.List;

public class Mod {

    private List<MinecraftVersion> minecraftVersions;
    private String name;
    private String description;
    private String iconUrl;
    private String id;

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
