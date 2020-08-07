package me.flashyreese.fabricmm.schema;

public class CurseModule {
    private String foldername;

    public boolean isFabricMod(){
        return foldername.equals("fabric.mod.json");
    }
}
