package me.flashyreese.fabricmm.minecraft;

import com.squareup.moshi.Moshi;
import org.joda.time.format.ISODateTimeFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SighHandler {

    public List<MinecraftVersion> versions;
    Map<String, String> date;

    public SighHandler() throws IOException {
        load();
        purgeWSnapshots();
        purgePreFabric();
        loadTable();
    }

    private void load() throws IOException {
        URL url = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String addonJson = in.lines().collect(Collectors.joining());
        in.close();
        SighEntry entry = new Moshi.Builder().build().adapter(SighEntry.class).fromJson(addonJson);
        assert entry != null;
        this.versions = entry.versions;
    }

    private void purgeWSnapshots(){
        versions.removeIf(minecraftVersion -> minecraftVersion.getId().contains("w"));
    }

    private void purgePreFabric(){
        versions.removeIf(minecraftVersion -> !ISODateTimeFormat.dateTimeParser().parseLocalDateTime(minecraftVersion.getReleaseTime()).isAfter(ISODateTimeFormat.dateTimeParser().parseLocalDateTime("2018-10-22T11:41:07+00:00")));
    }

    private void loadTable(){
        this.date = new HashMap<>();
        for (MinecraftVersion version: this.versions){
            this.date.put(version.getId(), version.getReleaseTime());
        }
    }

    public Map<String, String> getDateTable(){
        return date;
    }
}
