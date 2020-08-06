package me.flashyreese.fabricmrf.schema.repository;

import java.util.ArrayList;
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

    public HashMap<String, String>  getDependencies ()
    {
        return dependencies;
    }

    public void setModVersion(String modVersion) {
        this.modVersion = modVersion;
    }

    public void setModUrl(String modUrl) {
        this.modUrl = modUrl;
    }

    public void setDependencies(HashMap<String, String>  dependencies) {
        this.dependencies = dependencies;
    }
}
