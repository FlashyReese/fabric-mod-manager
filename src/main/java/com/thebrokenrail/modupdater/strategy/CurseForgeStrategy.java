package com.thebrokenrail.modupdater.strategy;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.thebrokenrail.modupdater.ModUpdater;
import com.thebrokenrail.modupdater.api.UpdateStrategy;
import com.thebrokenrail.modupdater.data.ModUpdate;
import com.thebrokenrail.modupdater.util.Util;
import com.vdurmont.semver4j.Semver;
import me.flashyreese.fabricmm.schema.ModUpdaterConfig;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;

public class CurseForgeStrategy implements UpdateStrategy {
    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    private static class CurseForgeFile {
        private String fileName;
        private String downloadUrl;
        private String[] gameVersion;
    }

    private final JsonAdapter<CurseForgeFile[]> jsonAdapter;

    public CurseForgeStrategy() {
        Moshi moshi = new Moshi.Builder().build();
        jsonAdapter = moshi.adapter(CurseForgeFile[].class);
    }

    @Override
    @Nullable
    public ModUpdate run(ModUpdaterConfig obj, String oldVersion, String name, String id) {
        int projectID = obj.getProjectID();

        String data;
        try {
            data = Util.urlToString("https://addons-ecs.forgesvc.net/api/v2/addon/" + projectID + "/files");
        } catch (IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        CurseForgeFile[] files;
        try {
            files = jsonAdapter.fromJson(data);
        } catch (JsonDataException | IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        if (files == null) {
            return null;
        }

        String versionStr = "1.16.1";//Fixme: Game Version detection;
        /*GameVersion version = Util.getMinecraftVersion();
        if (version.isStable()) {
            versionStr = version.getName();
        } else {
            versionStr = version.getReleaseTarget() + "-Snapshot";
        }*/

        boolean strict = isStrict(obj);

        CurseForgeFile newestFile = null;
        for (CurseForgeFile file : files) {
            if (Util.isFileCompatible(file.fileName)) {
                String fileVersion = Util.getVersionFromFileName(file.fileName);
                if ((Arrays.asList(file.gameVersion).contains(versionStr) && !strict) || Util.isVersionCompatible(id, fileVersion, strict)) {
                    if (newestFile != null) {
                        String newestFileVersion = Util.getVersionFromFileName(newestFile.fileName);
                        if (new Semver(fileVersion).compareTo(new Semver(newestFileVersion)) > 0){
                            newestFile = file;
                        }
                    } else {
                        newestFile = file;
                    }
                }
            }
        }

        if (newestFile != null) {
            String newestFileVersion = Util.getVersionFromFileName(newestFile.fileName);
            if (new Semver(newestFileVersion).compareTo(new Semver(oldVersion)) > 0){
                return new ModUpdate(oldVersion, newestFileVersion, newestFile.downloadUrl, name);
            }else{
                return null;
            }
        } else {
            return null;
        }
    }
}
