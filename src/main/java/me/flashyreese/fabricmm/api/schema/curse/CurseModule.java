package me.flashyreese.fabricmm.api.schema.curse;

public class CurseModule {
    private String foldername;

    public boolean isFabricMod(){
        return foldername.equals("fabric.mod.json");
    }
}
