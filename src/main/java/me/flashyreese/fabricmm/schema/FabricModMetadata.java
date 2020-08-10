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
    private Map<String, Object> contact;
    private List<Object> authors;
    private List<Object> contributors;
    private Map<String, Object> depends;
    private Map<String, Object> recommends;
    private Map<String, Object> suggests;
    private Map<String, Object> conflicts;
    private Map<String, Object> breaks;
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

    public Map<String, Object> getContact() {
        return contact;
    }

    public List<Object> getAuthors() {
        return authors;
    }

    public List<Object> getContributors() {
        return contributors;
    }

    public Map<String, Object> getDepends() {
        return depends;
    }

    public Map<String, Object> getRecommends() {
        return recommends;
    }

    public Map<String, Object> getSuggests() {
        return suggests;
    }

    public Map<String, Object> getConflicts() {
        return conflicts;
    }

    public Map<String, Object> getBreaks() {
        return breaks;
    }

    public FabricCustom getCustom() {
        return custom;
    }
}
