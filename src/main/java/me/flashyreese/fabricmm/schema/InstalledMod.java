package me.flashyreese.fabricmm.schema;

import me.flashyreese.common.i18n.I18nText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    public boolean isEnabled() {
        return this.getInstalledPath().endsWith(".jar");
    }

    public void assignMinecraftVersion() {
        if (modMetadata.getDepends() != null && modMetadata.getDepends().containsKey("minecraft")) {
            Object mcVer = modMetadata.getDepends().get("minecraft");
            if (mcVer instanceof String){
                setMinecraftVersion((String) mcVer);
            }else if(mcVer instanceof ArrayList){
                List<String> mcVers = (ArrayList<String>) modMetadata.getDepends().get("minecraft");
                String line = String.join(", ", mcVers);
                setMinecraftVersion(line);
            }
        } else {
            setMinecraftVersion(new I18nText("fmm.installed_mod.minecraft_version.not_available").toString());
        }
    }
}
