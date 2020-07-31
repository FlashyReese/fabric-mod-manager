package me.flashyreese.fabricmm.schema;

import java.util.HashMap;

public class Version {
    private String minecraftVersion;
    private String modVersion;
    private String modUrl;
    private HashMap<String, String> dependencies = new HashMap<String, String>();

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public String getModVersion() {
        return modVersion;
    }

    public String getModUrl() {
        return modUrl;
    }

    public HashMap<String, String> getDependencies() {
        return dependencies;
    }
}
