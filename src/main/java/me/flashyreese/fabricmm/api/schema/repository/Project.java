package me.flashyreese.fabricmm.api.schema.repository;

import java.util.List;

public class Project {
    private String id;
    private String name;
    private String description;
    private String iconUrl;
    private String sourcesUrl;
    private int curseForgeProject;

    private String projectUrl;
    private List<MinecraftVersion> minecraftVersions;//Curse will parse to this
    private User user;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getSourcesUrl() {
        return sourcesUrl;
    }

    public int getCurseForgeProject() {
        return curseForgeProject;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public List<MinecraftVersion> getMinecraftVersions() {
        return minecraftVersions;
    }

    public void setMinecraftVersions(List<MinecraftVersion> minecraftVersions) {
        this.minecraftVersions = minecraftVersions;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProject(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.iconUrl = project.getIconUrl();
        this.sourcesUrl = project.getSourcesUrl();
        this.curseForgeProject = project.getCurseForgeProject();
        this.projectUrl = project.getProjectUrl();
        this.minecraftVersions = project.getMinecraftVersions();
    }

    public boolean containsMinecraftVersion(String ver) {
        for (MinecraftVersion minecraftVersion : getMinecraftVersions()) {
            if (minecraftVersion.getMinecraftVersion().equalsIgnoreCase(ver)) {
                return true;
            }
        }
        return false;
    }

    public MinecraftVersion getMinecraftVersion(String ver) {
        for (MinecraftVersion version : getMinecraftVersions()) {
            if (version.getMinecraftVersion().equalsIgnoreCase(ver)) {
                return version;
            }
        }
        return null;
    }
}
