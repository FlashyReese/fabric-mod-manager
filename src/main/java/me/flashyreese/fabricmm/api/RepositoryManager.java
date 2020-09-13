package me.flashyreese.fabricmm.api;

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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RepositoryManager {
    private final List<User> users;
    private final File repositoryCache;
    private final String repositoryUrl;

    private final MinecraftVersionManifestHandler versionManifestHandler = new MinecraftVersionManifestHandler();

    public RepositoryManager(File repositoryCache, String repositoryUrl) throws Exception {
        if (!repositoryCache.exists()) {
            if (!repositoryCache.mkdirs()) {
                throw new Exception("Some went wrong creating directories");
            }
        }
        this.repositoryCache = repositoryCache;
        this.repositoryUrl = repositoryUrl;
        this.users = new ArrayList<>();
    }

    private void loadLocalRepositories() throws IOException {
        getUsers().clear();

        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, User.class);
        JsonAdapter<List<User>> adapter = moshi.adapter(type);
        URL url = new URL(repositoryUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String repository = in.lines().collect(Collectors.joining());

        List<User> usersRepository = adapter.fromJson(repository);

        final ExecutorService executor = Executors.newFixedThreadPool(4);
        final List<Future<?>> futures = new ArrayList<>();

        usersRepository.forEach(user -> user.getProjects().forEach(project -> {
            if (project.getCurseForgeProject() != -1) {
                File projectJsonFile = getProjectFile(project.getCurseForgeProject());
                if (projectJsonFile != null) {
                    Project localProject = FileUtil.readJson(projectJsonFile, Project.class);
                    project.setProject(localProject);
                } else {
                    Future<?> future = executor.submit(() -> {
                        try {
                            downloadProjectFile(project);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    futures.add(future);
                }
            } else {
                List<MinecraftVersion> minecraftVersions = new ArrayList<>();
                project.setMinecraftVersions(minecraftVersions);
            }
        }));
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        users.addAll(usersRepository);
    }

    private File getProjectFile(int curseForgeId) {
        return Arrays.stream(this.repositoryCache.listFiles())
                .filter(file -> file.isFile() && file.getName().endsWith(".json") && FileUtil.getFileName(file).equals(String.valueOf(curseForgeId)))
                .findFirst()
                .orElse(null);
    }

    private void downloadProjectFile(Project project) throws IOException {
        CurseAddon curseAddon = getCurseAddonFromProjectID(project.getCurseForgeProject(), false);
        if (curseAddon.getDefaultCurseAttachment() != null && curseAddon.getDefaultCurseAttachment().getThumbnailUrl() != null) {
            project.setIconUrl(curseAddon.getDefaultCurseAttachment().getThumbnailUrl());
        }
        project.setProjectUrl(curseAddon.getWebsiteUrl());
        project.setMinecraftVersions(new ArrayList<>());
        FileUtil.writeJson(new File(repositoryCache, String.format("%s.json", project.getCurseForgeProject())), project, Project.class);
    }

    public void downloadProjectFiles(Project project) {
        CurseAddon curseAddon = getCurseAddonFromProjectID(project.getCurseForgeProject(), true);
        List<MinecraftVersion> minecraftVersions = convertCurseAddonToMinecraftFiles(curseAddon);
        project.setMinecraftVersions(minecraftVersions);
        User user = project.getUser();
        project.setUser(null);
        FileUtil.writeJson(new File(repositoryCache, String.format("%s.json", project.getCurseForgeProject())), project, Project.class);
        project.setUser(user);
    }

    public List<MinecraftVersion> convertCurseAddonToMinecraftFiles(CurseAddon curseAddon) {
        return convertCurseFilesToMinecraftVersions(curseAddon.getFiles());
    }

    private CurseAddon getCurseAddon(String json, boolean includesFiles) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        CurseAddon curseAddon = moshi.adapter(CurseAddon.class).fromJson(json);
        assert curseAddon != null;
        if (includesFiles) {
            try {
                processCurseFiles(curseAddon);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        files.forEach(CurseFile::removeFabricFromGameVersion);
        curseAddon.setFiles(files);
        curseAddon.getLatestFiles().removeIf(curseFile -> !curseFile.isFabricModFile());
        curseAddon.getLatestFiles().forEach(latestFile -> {
            latestFile.removeFabricFromGameVersion();
            curseAddon.getFiles().add(latestFile);
        });
    }

    private CurseAddon getCurseAddonFromProjectID(long id, boolean includeFiles) {
        URL url;
        BufferedReader in;
        try {
            url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s", id));
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String addonJson = in.lines().collect(Collectors.joining());
            in.close();
            return getCurseAddon(addonJson, includeFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<MinecraftVersion> convertCurseFilesToMinecraftVersions(List<CurseFile> curseFiles) {
        Project project = new Project();
        project.setMinecraftVersions(new ArrayList<>());

        curseFiles.forEach(curseFile -> curseFile.getGameVersion().forEach(minecraftVersion -> {
            if (project.containsMinecraftVersion(minecraftVersion)) {
                MinecraftVersion currentMcVer = project.getMinecraftVersion(minecraftVersion);
                if (!currentMcVer.containsModVersion(curseFile.getDisplayName())) {
                    ModVersion modVersion = new ModVersion();
                    modVersion.setReleasedDate(curseFile.getFileDate());
                    modVersion.setModVersion(curseFile.getDisplayName());
                    modVersion.setModUrl(curseFile.getDownloadUrl());
                    currentMcVer.getModVersions().add(modVersion);
                    //Fixme: loop dependency here
                }
            } else {
                MinecraftVersion mcVer = new MinecraftVersion();
                if (versionManifestHandler.getDateTable().containsKey(minecraftVersion)) {
                    mcVer.setReleasedDate(versionManifestHandler.getDateTable().get(minecraftVersion));
                } else {
                    mcVer.setReleasedDate(curseFile.getFileDate());
                }
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
        }));
        project.getMinecraftVersions().sort(Collections.reverseOrder());
        project.getMinecraftVersions().forEach(minecraftVersion -> minecraftVersion.getModVersions().sort(Collections.reverseOrder()));
        return project.getMinecraftVersions();
    }

    public void clearLocalRepository() throws Exception {
        if (!FileUtil.removeFileDirectory(getRepositoryCache()))
            throw new Exception("No");
    }

    public void updateLocalRepository() throws Exception {
        loadLocalRepositories();
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
