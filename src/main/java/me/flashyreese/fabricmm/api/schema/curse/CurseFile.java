package me.flashyreese.fabricmm.api.schema.curse;

import java.util.List;

public class CurseFile {

    private String displayName;
    private String fileName;
    private String fileDate;
    private int fileLength;
    private String downloadUrl;
    private List<CurseDependency> dependencies;
    private List<String> gameVersion;
    private List<CurseModule> modules;
    private String gameVersionDateReleased;

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

    public List<CurseDependency> getDependencies() {
        return dependencies;
    }

    public List<String> getGameVersion() {
        return gameVersion;
    }

    public List<CurseModule> getModules() {
        return modules;
    }

    public String getGameVersionDateReleased() {
        return gameVersionDateReleased;
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
