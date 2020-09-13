package me.flashyreese.fabricmm.api.schema.repository;

import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private Map<String, String> contacts;
    private List<Project> projects;

    public String getName() {
        return name;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
