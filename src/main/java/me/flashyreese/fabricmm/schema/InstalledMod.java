package me.flashyreese.fabricmm.schema;

import me.flashyreese.common.i18n.I18nText;

import java.io.File;
import java.util.Map;

public class InstalledMod {
    private String id;
    private String name;
    private String version;
    private String description;
    private String iconPath;
    private String[] authors;
    private Map<String, String> contact;
    private String environment;
    private Map<String, String> depends;
    private String minecraftVersion;//Need Fabric modders to include this in fabric.mod.json depends section
    private String installedPath;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String[] getAuthors() {
        return authors;
    }

    public Map<String, String> getContact() {
        return contact;
    }

    public String getEnvironment() {
        return environment;
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

    private boolean isInstalledViaFMM(){
        File installedMod = new File(this.getInstalledPath());
        String fileName = installedMod.getName().substring(0, installedMod.getName().lastIndexOf('.'));
        return fileName.contains("__") && fileName.split("__").length == 3;//Fixme: Jank
    }

    public void assignMinecraftVersion() {
        if (depends != null && depends.containsKey("minecraft")){
            setMinecraftVersion(depends.get("minecraft"));
        }else if(isInstalledViaFMM()){
            File installedMod = new File(this.getInstalledPath());
            String fileName = installedMod.getName().substring(0, installedMod.getName().lastIndexOf('.'));
            String[] splitString = fileName.split("__");
            setMinecraftVersion(splitString[1]);
        }else{
            setMinecraftVersion(new I18nText("fmm.installed_mod.minecraft_version.not_available").toString());
        }
    }
}
