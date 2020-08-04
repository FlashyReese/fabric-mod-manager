package me.flashyreese.fabricmrf.schema.repository;

import java.util.HashMap;
import java.util.List;

public class Author {
    private String name;
    private HashMap<String, String> contacts;
    private List<Mod> mods;

    public String getName() {
        return name;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public List<Mod> getMods() {
        return mods;
    }
}
