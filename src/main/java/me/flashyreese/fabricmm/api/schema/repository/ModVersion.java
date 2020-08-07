package me.flashyreese.fabricmm.api.schema.repository;

import java.util.Map;

public class ModVersion {

    private String modVersion;
    private String modUrl;
    private Map<String, String> dependencies;

    public String getModVersion ()
    {
        return modVersion;
    }

    public String getModUrl ()
    {
        return modUrl;
    }

    public Map<String, String>  getDependencies ()
    {
        return dependencies;
    }

    public void setModVersion(String modVersion) {
        this.modVersion = modVersion;
    }

    public void setModUrl(String modUrl) {
        this.modUrl = modUrl;
    }

    public void setDependencies(Map<String, String>  dependencies) {
        this.dependencies = dependencies;
    }
}
