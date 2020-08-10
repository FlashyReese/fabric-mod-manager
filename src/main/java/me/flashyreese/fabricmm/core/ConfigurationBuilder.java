package me.flashyreese.fabricmm.core;

import me.flashyreese.common.util.FileUtil;

import java.io.File;

public class ConfigurationBuilder {

    public static FMMSettings createFMMDefaultSettings(File config){
        FMMSettings fmmSettings = new FMMSettings();
        fmmSettings.setLocale("en_us");
        fmmSettings.setMmcPath("");
        FileUtil.writeJson(config, fmmSettings, FMMSettings.class);
        return fmmSettings;
    }
}
