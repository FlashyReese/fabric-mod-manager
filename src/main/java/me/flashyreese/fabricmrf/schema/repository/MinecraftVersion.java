package me.flashyreese.fabricmrf.schema.repository;

import java.util.List;

public class MinecraftVersion {

    private List<ModVersion> modVersions;
    private String minecraftVersion;

    public List<ModVersion> getModVersions(){
        return modVersions;
    }

    public String getMinecraftVersion(){
        return minecraftVersion;
    }

    public void setModVersions(List<ModVersion> modVersions) {
        this.modVersions = modVersions;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }
}
