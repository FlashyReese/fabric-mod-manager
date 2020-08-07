package me.flashyreese.fabricmm.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.fabricmm.api.schema.curse.CurseAddon;
import me.flashyreese.fabricmm.api.schema.curse.CurseFile;
import me.flashyreese.fabricmm.api.schema.repository.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RepositoryManager {
    private final ArrayList<User> users;
    private final File repositoryCache;
    private final String repositoryUrl;

    public RepositoryManager(File repositoryCache, String repositoryUrl) throws Exception {
        if(repositoryCache.exists()){
            repositoryCache.mkdirs();
        }
        this.repositoryCache = repositoryCache;
        this.repositoryUrl = repositoryUrl;
        this.users = new ArrayList<>();
        loadLocalRepositories();
    }

    private void loadLocalRepositories() throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, User.class);
        JsonAdapter<List<User>> adapter = moshi.adapter(type);
        URL url = new URL(repositoryUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String repository = in.lines().collect(Collectors.joining());
        List<User> usersRepository = adapter.fromJson(repository);
        for (User user: usersRepository){
            for (Project project: user.getProjects()){
                if(project.getCurseForgeProject() != -1){
                    File projectJsonFile = getProjectFile(project.getCurseForgeProject());
                    if (projectJsonFile != null){
                        Type newType = Types.newParameterizedType(List.class, MinecraftVersion.class);
                        JsonAdapter<List<MinecraftVersion>> jsonAdapter = moshi.adapter(newType);
                        FileReader fileReader = new FileReader(projectJsonFile);
                        BufferedReader buffered = new BufferedReader(fileReader);
                        String projectJson = buffered.lines().collect(Collectors.joining());
                        buffered.close();
                        fileReader.close();
                        List<MinecraftVersion> minecraftVersions = jsonAdapter.fromJson(projectJson);
                        project.setMinecraftVersions(minecraftVersions);
                    }else{
                        downloadProjectFile(project);
                    }
                }else{
                    List<MinecraftVersion> minecraftVersions = new ArrayList<>();
                    project.setMinecraftVersions(minecraftVersions);
                }
            }
        }
        users.addAll(usersRepository);
    }

    private File getProjectFile(int curseForgeId) throws Exception {
        for (File file: Objects.requireNonNull(this.repositoryCache.listFiles())){
            if (file.isFile() && file.getName().endsWith(".json") && FileUtil.getFileName(file).equals(String.valueOf(curseForgeId))){
                return file;
            }
        }
        return null;
    }

    private void downloadProjectFile(Project project) throws Exception {
        CurseAddon curseAddon = getCurseAddonFromProjectID(project.getCurseForgeProject());
        List<MinecraftVersion> minecraftVersions = convertCurseAddonToMinecraftFiles(curseAddon);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<MinecraftVersion>> jsonAdapter = moshi.adapter(Types.newParameterizedType(List.class, MinecraftVersion.class));
        String json = jsonAdapter.toJson(minecraftVersions);
        FileWriter fw = new FileWriter(new File(repositoryCache, String.format("%s.json", project.getCurseForgeProject())));
        fw.write(json);
        fw.flush();
        fw.close();
        project.setMinecraftVersions(minecraftVersions);
    }

    public List<MinecraftVersion> convertCurseAddonToMinecraftFiles(CurseAddon curseAddon) throws Exception {
        return convertCurseFilesToMinecraftVersions(curseAddon.getFiles());
    }

    private CurseAddon getCurseAddon(String json) throws Exception {
        CurseAddon curseAddon = new Gson().fromJson(json, CurseAddon.class);
        processCurseFiles(curseAddon);
        return curseAddon;
    }

    private void processCurseFiles(CurseAddon curseAddon) throws Exception {
        URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s/files", curseAddon.getId()));
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String filesJson = in.lines().collect(Collectors.joining());
        in.close();
        ArrayList<CurseFile> files = new Gson().fromJson(filesJson, new TypeToken<ArrayList<CurseFile>>(){}.getType());
        files.removeIf(curseFile -> !curseFile.isFabricModFile());
        for (CurseFile curseFile: files){
            curseFile.removeFabricFromGameVersion();
        }
        curseAddon.setFiles(files);
        curseAddon.getLatestFiles().removeIf(curseFile -> !curseFile.isFabricModFile());
        for (CurseFile latestFile: curseAddon.getLatestFiles()){
            latestFile.removeFabricFromGameVersion();
            curseAddon.getFiles().add(latestFile);
        }
    }

    private CurseAddon getCurseAddonFromProjectID(long id) throws Exception {
        URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s", id));
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String addonJson = in.lines().collect(Collectors.joining());
        in.close();
        return getCurseAddon(addonJson);
    }

    private List<MinecraftVersion> convertCurseFilesToMinecraftVersions(ArrayList<CurseFile> curseFiles) throws Exception {
        Project project = new Project();
        project.setMinecraftVersions(new ArrayList<>());
        for (CurseFile curseFile: curseFiles){
            for (String minecraftVersion: curseFile.getGameVersion()){
                if (project.containsMinecraftVersion(minecraftVersion)){
                    MinecraftVersion currentMcVer = project.getMinecraftVersion(minecraftVersion);
                    if (!currentMcVer.containsModVersion(curseFile.getDisplayName())){
                        ModVersion modVersion = new ModVersion();
                        modVersion.setModVersion(curseFile.getDisplayName());
                        modVersion.setModUrl(curseFile.getDownloadUrl());
                        currentMcVer.getModVersions().add(modVersion);
                        //Fixme: loop dependency here
                    }
                }else{
                    MinecraftVersion mcVer = new MinecraftVersion();
                    mcVer.setMinecraftVersion(minecraftVersion);
                    mcVer.setModVersions(new ArrayList<>());
                    ModVersion modVersion = new ModVersion();
                    modVersion.setModVersion(curseFile.getDisplayName());
                    modVersion.setModUrl(curseFile.getDownloadUrl());
                    //Fixme: loop dependency here
                    mcVer.getModVersions().add(modVersion);
                    project.getMinecraftVersions().add(mcVer);
                }
            }
        }
        return project.getMinecraftVersions();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public File getRepositoryCache() {
        return repositoryCache;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}
