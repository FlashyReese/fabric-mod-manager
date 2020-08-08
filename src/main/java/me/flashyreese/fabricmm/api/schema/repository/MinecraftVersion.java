package me.flashyreese.fabricmm.api.schema.repository;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

public class MinecraftVersion implements Comparable<MinecraftVersion> {
    private String minecraftVersion;
    private List<ModVersion> modVersions;
    private String releasedDate;

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public List<ModVersion> getModVersions() {
        return modVersions;
    }

    public void setModVersions(List<ModVersion> modVersions) {
        this.modVersions = modVersions;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public boolean containsModVersion(String ver){
        for (ModVersion modVersion: getModVersions()){
            if (modVersion.getModVersion().equalsIgnoreCase(ver)){
                return true;
            }
        }
        return false;
    }

    public ModVersion getModVersion(String ver){
        for (ModVersion modVersion: getModVersions()){
            if (modVersion.getModVersion().equalsIgnoreCase(ver)){
                return modVersion;
            }
        }
        return null;
    }

    public LocalDate getReleasedLocalDate(){
        return ISODateTimeFormat.dateTimeParser().parseLocalDateTime(getReleasedDate()).toLocalDate();
    }

    @Override
    public int compareTo(MinecraftVersion o) {
        return getReleasedLocalDate().compareTo(o.getReleasedLocalDate());
    }
}
