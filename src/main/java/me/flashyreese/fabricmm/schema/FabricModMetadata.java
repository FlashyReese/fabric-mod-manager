package me.flashyreese.fabricmm.schema;

import java.util.List;
import java.util.Map;

public class FabricModMetadata {

    private int schemaVersion;
    private String id;
    private String name;
    private String version;
    private String description;
    private String environment;
    private String license;
    private String icon;
    private Map<String, String> contact;
    private List<String> authors;
    private List<String> contributors;
    private Map<String, String> depends;
    private Map<String, String> recommends;
    private Map<String, String> suggests;
    private Map<String, String> conflicts;
    private Map<String, String> breaks;
    private FabricCustom custom;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getLicense() {
        return license;
    }

    public String getIcon() {
        return icon;
    }

    public Map<String, String> getContact() {
        return contact;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public Map<String, String> getDepends() {
        return depends;
    }

    public Map<String, String> getRecommends() {
        return recommends;
    }

    public Map<String, String> getSuggests() {
        return suggests;
    }

    public Map<String, String> getConflicts() {
        return conflicts;
    }

    public Map<String, String> getBreaks() {
        return breaks;
    }

    public FabricCustom getCustom() {
        return custom;
    }
}
