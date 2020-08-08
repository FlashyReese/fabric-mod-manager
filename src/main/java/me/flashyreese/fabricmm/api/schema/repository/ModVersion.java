package me.flashyreese.fabricmm.api.schema.repository;

import org.joda.time.LocalDate;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Map;

public class ModVersion implements Comparable<ModVersion> {

    private String modVersion;
    private String modUrl;
    private String releasedDate;
    private Map<String, String> dependencies;

    public String getModVersion() {
        return modVersion;
    }

    public void setModVersion(String modVersion) {
        this.modVersion = modVersion;
    }

    public String getModUrl() {
        return modUrl;
    }

    public void setModUrl(String modUrl) {
        this.modUrl = modUrl;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public LocalDate getReleasedLocalDate(){
        return ISODateTimeFormat.dateTimeParser().parseLocalDateTime(getReleasedDate()).toLocalDate();
    }

    @Override
    public int compareTo(ModVersion o) {
        return getReleasedLocalDate().compareTo(o.getReleasedLocalDate());
    }
}
