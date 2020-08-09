package me.flashyreese.fabricmm.schema;

public class ModUpdater {
    private boolean strict = true;
    private String strategy;
    private int projectID;
    private String owner;
    private String repository;
    private String url;

    public boolean isStrict() {
        return strict;
    }

    public String getStrategy() {
        return strategy;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepository() {
        return repository;
    }

    public String getUrl() {
        return url;
    }
}
