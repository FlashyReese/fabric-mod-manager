package me.flashyreese.fabricmm.api.schema.curse;

import java.util.ArrayList;

public class CurseFile {

    private String displayName;
    private String fileName;
    private String fileDate;
    private int fileLength;
    private String downloadUrl;
    private ArrayList<CurseDependency> dependencies;
    private ArrayList<String> gameVersion;
    private ArrayList<CurseModule> modules;

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public int getFileLength() {
        return fileLength;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public ArrayList<CurseDependency> getDependencies() {
        return dependencies;
    }

    public ArrayList<String> getGameVersion() {
        return gameVersion;
    }

    public ArrayList<CurseModule> getModules() {
        return modules;
    }

    public void removeFabricFromGameVersion(){
        getGameVersion().remove("Fabric");
        getGameVersion().remove("Forge");//Some people are just weird
    }

    public boolean isFabricModFile(){
        for (CurseModule curseModule: getModules()){
            if (curseModule.isFabricMod()){
                return true;
            }
        }
        return false;
    }
}
