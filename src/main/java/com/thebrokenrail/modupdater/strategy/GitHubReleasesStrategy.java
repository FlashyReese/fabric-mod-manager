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

public class GitHubReleasesStrategy implements UpdateStrategy {
    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    private static class GitHubRelease {
        private GitHubReleaseAsset[] assets;
    }

    @SuppressWarnings("unused")
    private static class GitHubReleaseAsset {
        private String name;
        private String browser_download_url;
    }

    private final JsonAdapter<GitHubRelease[]> jsonAdapter;

    public GitHubReleasesStrategy() {
        Moshi moshi = new Moshi.Builder().build();
        jsonAdapter = moshi.adapter(GitHubRelease[].class).nonNull();
    }

    @Override
    @Nullable
    public ModUpdate run(ModUpdaterConfig obj, String oldVersion, String name, String id) {
        String owner = obj.getOwner();
        String repo = obj.getRepository();

        String data;
        try {
            data = Util.urlToString(String.format("https://api.github.com/repos/%s/%s/releases", owner, repo));
        } catch (IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        GitHubRelease[] releases;
        try {
            releases = jsonAdapter.fromJson(data);
        } catch (JsonDataException | IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        if (releases == null) {
            return null;
        }

        boolean strict = isStrict(obj);

        GitHubReleaseAsset newestFile = null;
        for (GitHubRelease release : releases) {
            for (GitHubReleaseAsset asset : release.assets) {
                if (Util.isFileCompatible(asset.name)) {
                    String fileVersion = Util.getVersionFromFileName(asset.name);
                    if (Util.isVersionCompatible(id, fileVersion, strict)) {
                        if (newestFile != null) {
                            if (new Semver(fileVersion).compareTo(new Semver(fileVersion)) > 0){
                                newestFile = asset;
                            }
                        } else {
                            newestFile = asset;
                        }
                    }
                }
            }
        }

        if (newestFile != null) {
            String newestFileVersion = Util.getVersionFromFileName(newestFile.name);
            if (new Semver(newestFileVersion).compareTo(new Semver(oldVersion)) > 0){
                return new ModUpdate(oldVersion, newestFileVersion, newestFile.browser_download_url, name);
            }else{
                return null;
            }
        } else {
            return null;
        }
    }
}
