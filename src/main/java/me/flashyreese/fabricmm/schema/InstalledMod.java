package me.flashyreese.fabricmm.schema;

import java.util.HashMap;

public class InstalledMod {
    private String id;
    private String name;
    private String version;
    private String description;
    private String iconPath;//Cached Locally
    private String[] authors;
    private HashMap<String, String> contact;
    private String environment;
    private String minecraftVersion = "";//Need Fabric modders to include this in fabric.mod.json
    private String installedPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public HashMap<String, String> getContact() {
        return contact;
    }

    public void setContact(HashMap<String, String> contact) {
        this.contact = contact;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public String getInstalledPath() {
        return installedPath;
    }

    public void setInstalledPath(String installedPath) {
        this.installedPath = installedPath;
    }

    public boolean isEnabled(){
        return this.getInstalledPath().endsWith(".jar");
    }
}
