package me.flashyreese.fabricmrf.schema.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Author {
    private String name;
    private HashMap<String, String> contacts;
    private ArrayList<Mod> mods;

    public String getName() {
        return name;
    }

    public HashMap<String, String> getContacts() {
        return contacts;
    }

    public ArrayList<Mod> getMods() {
        return mods;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }

    public void setMods(ArrayList<Mod> mods) {
        this.mods = mods;
    }
}
