package me.flashyreese.fabricmrf.schema.repository;

import java.util.HashMap;

public class ModVersion {

    private String modVersion;
    private String modUrl;
    private HashMap<String, String> dependencies;

    public String getModVersion ()
    {
        return modVersion;
    }

    public String getModUrl ()
    {
        return modUrl;
    }

    public HashMap<String, String> getDependencies ()
    {
        return dependencies;
    }
}
