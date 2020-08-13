package me.flashyreese.fabricmm.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.fabricmm.api.schema.curse.CurseAddon;
import me.flashyreese.fabricmm.api.schema.curse.CurseFile;
import me.flashyreese.fabricmm.api.schema.repository.*;
import me.flashyreese.fabricmm.minecraft.SighHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RepositoryManager {
    private final List<User> users;
    private final File repositoryCache;
    private final String repositoryUrl;

    private final SighHandler sighHandler = new SighHandler();

    public RepositoryManager(File repositoryCache, String repositoryUrl) throws Exception {
        if(!repositoryCache.exists()){
            if(!repositoryCache.mkdirs()){
                throw new Exception("Some went wrong creating directories");
            }
        }
        this.repositoryCache = repositoryCache;
        this.repositoryUrl = repositoryUrl;
        this.users = new ArrayList<>();
    }

    private void loadLocalRepositories(boolean force) throws IOException {
        users.clear();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, User.class);
        JsonAdapter<List<User>> adapter = moshi.adapter(type);
        URL url = new URL(repositoryUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String repository = in.lines().collect(Collectors.joining());
        List<User> usersRepository = adapter.fromJson(repository);
        final ExecutorService executor = Executors.newFixedThreadPool(4);
        final List<Future<?>> futures = new ArrayList<>();
        assert usersRepository != null;
        for (User user: usersRepository){
            for (Project project: user.getProjects()){
                if(project.getCurseForgeProject() != -1){
                    File projectJsonFile = getProjectFile(project.getCurseForgeProject());
                    if (projectJsonFile != null && !force){
                        Type newType = Types.newParameterizedType(List.class, MinecraftVersion.class);
                        JsonAdapter<List<MinecraftVersion>> jsonAdapter = moshi.adapter(newType);
                        FileReader fileReader = new FileReader(projectJsonFile);
                        BufferedReader buffered = new BufferedReader(fileReader);
                        String projectJson = buffered.lines().collect(Collectors.joining());
                        buffered.close();
                        fileReader.close();
                        List<MinecraftVersion> minecraftVersions = jsonAdapter.fromJson(projectJson);
                        project.setMinecraftVersions(minecraftVersions);
                        loadProjectInfo(project);//Fixme: Saved Locally CurseForge doesn't like many request
                    }else{
                        Future<?> future = executor.submit(() -> {
                            try {
                                downloadProjectFile(project);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        futures.add(future);
                    }
                }else{
                    List<MinecraftVersion> minecraftVersions = new ArrayList<>();
                    project.setMinecraftVersions(minecraftVersions);
                }
            }
        }
        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        users.addAll(usersRepository);
    }

    private File getProjectFile(int curseForgeId) {
        for (File file: Objects.requireNonNull(this.repositoryCache.listFiles())){
            if (file.isFile() && file.getName().endsWith(".json") && FileUtil.getFileName(file).equals(String.valueOf(curseForgeId))){
                return file;
            }
        }
        return null;
    }

    private void downloadProjectFile(Project project) throws IOException {
        CurseAddon curseAddon = getCurseAddonFromProjectID(project.getCurseForgeProject(), true);
        if (curseAddon.getDefaultCurseAttachment() != null && curseAddon.getDefaultCurseAttachment().getThumbnailUrl() != null){
            project.setIconUrl(curseAddon.getDefaultCurseAttachment().getThumbnailUrl());
        }
        project.setProjectUrl(curseAddon.getWebsiteUrl());
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

    private void loadProjectInfo(Project project) throws IOException {
        CurseAddon curseAddon = getCurseAddonFromProjectID(project.getCurseForgeProject(), false);
        project.setProjectUrl(curseAddon.getWebsiteUrl());
    }

    public List<MinecraftVersion> convertCurseAddonToMinecraftFiles(CurseAddon curseAddon) {
        return convertCurseFilesToMinecraftVersions(curseAddon.getFiles());
    }

    private CurseAddon getCurseAddon(String json, boolean includesFiles) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        CurseAddon curseAddon = moshi.adapter(CurseAddon.class).fromJson(json);
        assert curseAddon != null;
        if(includesFiles){
            processCurseFiles(curseAddon);
        }
        return curseAddon;
    }

    private void processCurseFiles(CurseAddon curseAddon) throws IOException {
        URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s/files", curseAddon.getId()));
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String filesJson = in.lines().collect(Collectors.joining());
        in.close();
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, CurseFile.class);
        JsonAdapter<List<CurseFile>> jsonAdapter = moshi.adapter(type);
        List<CurseFile> files = jsonAdapter.fromJson(filesJson);
        assert files != null;
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

    private CurseAddon getCurseAddonFromProjectID(long id, boolean includeFiles) throws IOException {
        URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s", id));
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String addonJson = in.lines().collect(Collectors.joining());
        in.close();
        return getCurseAddon(addonJson, includeFiles);
    }

    private List<MinecraftVersion> convertCurseFilesToMinecraftVersions(List<CurseFile> curseFiles) {
        Project project = new Project();
        project.setMinecraftVersions(new ArrayList<>());
        for (CurseFile curseFile: curseFiles){
            for (String minecraftVersion: curseFile.getGameVersion()){
                if (project.containsMinecraftVersion(minecraftVersion)){
                    MinecraftVersion currentMcVer = project.getMinecraftVersion(minecraftVersion);
                    if (!currentMcVer.containsModVersion(curseFile.getDisplayName())){
                        ModVersion modVersion = new ModVersion();
                        modVersion.setReleasedDate(curseFile.getFileDate());
                        modVersion.setModVersion(curseFile.getDisplayName());
                        modVersion.setModUrl(curseFile.getDownloadUrl());
                        currentMcVer.getModVersions().add(modVersion);
                        //Fixme: loop dependency here
                    }
                }else{
                    MinecraftVersion mcVer = new MinecraftVersion();
                    if (sighHandler.getDateTable().containsKey(minecraftVersion)){
                        mcVer.setReleasedDate(sighHandler.getDateTable().get(minecraftVersion));
                    }else{
                        mcVer.setReleasedDate(curseFile.getFileDate());
                    }
                    //mcVer.setReleasedDate(curseFile.getGameVersionDateReleased()); CurseForge is broken
                    mcVer.setMinecraftVersion(minecraftVersion);
                    mcVer.setModVersions(new ArrayList<>());
                    ModVersion modVersion = new ModVersion();
                    modVersion.setReleasedDate(curseFile.getFileDate());
                    modVersion.setModVersion(curseFile.getDisplayName());
                    modVersion.setModUrl(curseFile.getDownloadUrl());
                    //Fixme: loop dependency here
                    mcVer.getModVersions().add(modVersion);
                    project.getMinecraftVersions().add(mcVer);
                }
            }
        }
        project.getMinecraftVersions().sort(Collections.reverseOrder());
        for (MinecraftVersion minecraftVersion: project.getMinecraftVersions()){
            minecraftVersion.getModVersions().sort(Collections.reverseOrder());
        }
        return project.getMinecraftVersions();
    }


    public void updateLocalRepository(boolean force) throws Exception {
        loadLocalRepositories(force);
    }

    public List<User> getUsers() {
        return users;
    }

    public File getRepositoryCache() {
        return repositoryCache;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }
}
