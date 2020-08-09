package me.flashyreese.fabricmm.schema;

import me.flashyreese.common.i18n.I18nText;

import java.io.File;

public class InstalledMod {
    private String iconPath;
    private String minecraftVersion;//Need Fabric modders to include this in fabric.mod.json depends section
    private String installedPath;
    private FabricModMetadata modMetadata;

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
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

    public FabricModMetadata getModMetadata() {
        return modMetadata;
    }

    public void setModMetadata(FabricModMetadata modMetadata) {
        this.modMetadata = modMetadata;
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
        if (modMetadata.getDepends() != null && modMetadata.getDepends().containsKey("minecraft")){
            setMinecraftVersion(modMetadata.getDepends().get("minecraft"));
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
