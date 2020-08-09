package com.thebrokenrail.modupdater.data;

public class ModUpdate {
    public final String text;
    public final String downloadURL;

    private String toFriendlyString(String version) {
        return version;
    }

    public ModUpdate(String oldVersion, String newVersion, String downloadURL, String name) {
        this.text = name + ' ' + toFriendlyString(oldVersion) + " -> " + toFriendlyString(newVersion);
        this.downloadURL = downloadURL;
    }
}
