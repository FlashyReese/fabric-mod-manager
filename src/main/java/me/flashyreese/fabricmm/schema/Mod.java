package me.flashyreese.fabricmm.schema;

import java.util.List;

public class Mod {
    private String name;
    private String description;
    private String iconUrl;
    private List<Version> versions;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public List<Version> getVersions() {
        return versions;
    }
}
