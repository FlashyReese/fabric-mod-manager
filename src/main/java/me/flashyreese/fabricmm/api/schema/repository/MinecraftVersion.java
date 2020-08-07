package me.flashyreese.fabricmm.api.schema.repository;

import java.util.List;

public class MinecraftVersion {
    private String minecraftVersion;
    private List<ModVersion> modVersions;

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public void setModVersions(List<ModVersion> modVersions) {
        this.modVersions = modVersions;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public List<ModVersion> getModVersions() {
        return modVersions;
    }

    public boolean containsModVersion(String ver){
        for (ModVersion modVersion: getModVersions()){
            if (modVersion.getModVersion().equalsIgnoreCase(ver)){
                return true;
            }
        }
        return false;
    }

    public ModVersion getModVersion(String ver){
        for (ModVersion modVersion: getModVersions()){
            if (modVersion.getModVersion().equalsIgnoreCase(ver)){
                return modVersion;
            }
        }
        return null;
    }
}
