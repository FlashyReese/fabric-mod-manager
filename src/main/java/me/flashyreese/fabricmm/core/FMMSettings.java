package me.flashyreese.fabricmm.core;

public class FMMSettings {
    private String lastSelectedInstance;
    private String mmcPath;
    private String locale;

    public String getLastSelectedInstance() {
        return lastSelectedInstance;
    }

    public void setLastSelectedInstance(String lastSelectedInstance) {
        this.lastSelectedInstance = lastSelectedInstance;
    }

    public String getMmcPath() {
        return mmcPath;
    }

    public void setMmcPath(String mmcPath) {
        this.mmcPath = mmcPath;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
