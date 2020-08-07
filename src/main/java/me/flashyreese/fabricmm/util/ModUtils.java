package me.flashyreese.fabricmm.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.CurseAddon;
import me.flashyreese.fabricmm.schema.CurseFile;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.fabricmrf.schema.repository.Author;
import me.flashyreese.fabricmrf.schema.repository.MinecraftVersion;
import me.flashyreese.fabricmrf.schema.repository.Mod;
import me.flashyreese.fabricmrf.schema.repository.ModVersion;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ModUtils {

    public static InstalledMod getInstalledModFromJar(File file) throws IOException {
        InstalledMod installedMod = null;
        final JarFile jarFile = new JarFile(file);
        final JarEntry fabricSchema = jarFile.getJarEntry("fabric.mod.json");
        if(fabricSchema != null) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(fabricSchema), StandardCharsets.UTF_8));
            String fabricSchemaJson = bufferedReader.lines().collect(Collectors.joining());
            bufferedReader.close();
            installedMod = new Gson().fromJson(fabricSchemaJson, InstalledMod.class);
            JSONObject fabricSchemaJsonObject = new JSONObject(fabricSchemaJson);
            String icon = fabricSchemaJsonObject.getString("icon");
            final JarEntry iconJarEntry = jarFile.getJarEntry(icon);
            if (iconJarEntry != null){
                File iconFile = new File(ConfigurationManager.getInstance().ICON_CACHE_DIR + File.separator + String.format("%s.png", installedMod.getId()));
                if(!iconFile.exists()){
                    InputStream inputStream = jarFile.getInputStream(iconJarEntry);
                    FileOutputStream fileOutputStream = new FileOutputStream(iconFile);
                    while (inputStream.available() > 0) {
                        fileOutputStream.write(inputStream.read());
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }
                installedMod.setIconPath(iconFile.getAbsolutePath());
                installedMod.setInstalledPath(file.getAbsolutePath());
                installedMod.assignMinecraftVersion();
            }
        }
        jarFile.close();
        return installedMod;
    }

    public static List<InstalledMod> getInstalledModsFromDir(File dir) throws Exception {
        if(!dir.exists())dir.mkdirs();
        List<InstalledMod> installedMods = new ArrayList<InstalledMod>();
        if(!dir.isDirectory()){
            throw new Exception("This is not a directory???");
        }
        for(File file: Objects.requireNonNull(dir.listFiles((directory, fileName) -> fileName.endsWith(".jar") || fileName.endsWith(".fabricmod")))){
            InstalledMod installedMod = getInstalledModFromJar(file);
            if(installedMod != null){
                installedMods.add(installedMod);
            }
        }
        return installedMods;
    }

    public static File findDefaultInstallDir() {
        File dir;
        String home = System.getProperty("user.home", ".");
        String os = System.getProperty("os.name").toLowerCase();
        File homeDir = new File(home);
        if (os.contains("win") && System.getenv("APPDATA") != null) {
            dir = new File(System.getenv("APPDATA"), ".minecraft");
        } else if (os.contains("mac")) {
            dir = new File(homeDir, "Library" + File.separator + "Application Support" + File.separator + "minecraft");
        } else {
            dir = new File(homeDir, ".minecraft");
        }
        return dir;
    }

    public static File findDefaultLauncherPath(){
        File file;
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win") && System.getenv("ProgramFiles(X86)") != null){
            file = new File(System.getenv("ProgramFiles(X86)"), "/Minecraft Launcher/MinecraftLauncher.exe");
        }else if(os.contains("mac")){
            file = new File(File.separator + "Applications" + File.separator + "Minecraft.app");
        }else{
            file = new File("/opt/minecraft-launcher/minecraft-launcher");//Fixme: currently broken
        }
        return file;
    }

    public static File getModsDirectory(){
        return new File(findDefaultInstallDir().getAbsolutePath() + File.separator + "mods");
    }

    public static InstalledMod changeInstalledModState(InstalledMod installedMod){
        File newFile;
        if(installedMod.isEnabled()){
            newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "fabricmod");
        }else{
            newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "jar");
        }
        installedMod.setInstalledPath(newFile.getAbsolutePath());
        return installedMod;
    }

    public static ArrayList<CurseAddon> getFabricCurseAddons(boolean includeFiles) throws IOException, InterruptedException {
        URL url = new URL("https://addons-ecs.forgesvc.net/api/v2/addon/search?gameId=432&pagesize=5000&categoryId=4780");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String filesJson = in.lines().collect(Collectors.joining());
        in.close();
        ArrayList<CurseAddon> curseAddons = new Gson().fromJson(filesJson, new TypeToken<ArrayList<CurseAddon>>(){}.getType());//Fixme: and doesn't check latestFile;
        if (includeFiles){
            for (CurseAddon curseAddon: curseAddons){
                System.out.println(curseAddon.getName());
                URL url2 = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s/files", curseAddon.getId()));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream()));
                String filesJson2 = in2.lines().collect(Collectors.joining());
                in2.close();
                ArrayList<CurseFile> files = new Gson().fromJson(filesJson2, new TypeToken<ArrayList<CurseFile>>(){}.getType());
                files.removeIf(curseFile -> !curseFile.getGameVersion().contains("Fabric") && !curseFile.isFabricModFile());
                for (CurseFile curseFile: files){
                    curseFile.removeFabricFromGameVersion();
                }
                curseAddon.setFiles(files);
                for (CurseFile latestFile: curseAddon.getLatestFiles()){
                    curseAddon.getFiles().add(latestFile);
                }
            }
        }
        return curseAddons;
    }

    public static CurseAddon getCurseAddon(String json, boolean includeFiles) throws IOException {
        CurseAddon curseAddon = new Gson().fromJson(json, CurseAddon.class);
        if (includeFiles){
            URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s/files", curseAddon.getId()));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String filesJson = in.lines().collect(Collectors.joining());
            in.close();
            ArrayList<CurseFile> files = new Gson().fromJson(filesJson, new TypeToken<ArrayList<CurseFile>>(){}.getType());
            files.removeIf(curseFile -> !curseFile.getGameVersion().contains("Fabric") && !curseFile.isFabricModFile());
            for (CurseFile curseFile: files){
                curseFile.removeFabricFromGameVersion();
            }
            curseAddon.setFiles(files);
            for (CurseFile latestFile: curseAddon.getLatestFiles()){
                curseAddon.getFiles().add(latestFile);
            }
        }
        return curseAddon;
    }

    public static CurseAddon getCurseAddonFromProjectID(long id, boolean includeFiles) throws IOException {
        URL url = new URL(String.format("https://addons-ecs.forgesvc.net/api/v2/addon/%s", id));
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String addonJson = in.lines().collect(Collectors.joining());
        in.close();
        return getCurseAddon(addonJson, includeFiles);
    }

    public static Author convertCurseAddonToAuthor(CurseAddon curseAddon, boolean includeFiles){
        Author author = new Author();
        author.setName(curseAddon.getAuthors().get(0).getName());
        HashMap<String, String> contacts = new HashMap<String, String>();
        contacts.put("curseForgeUrl", curseAddon.getAuthors().get(0).getUrl());
        author.setContacts(contacts);
        author.setMods(new ArrayList<Mod>());
        Mod mod = new Mod();
        //mod.setAuthor(author);StackOverflow
        mod.setDescription(curseAddon.getSummary());
        if (curseAddon.getDefaultCurseAttachment() != null && curseAddon.getDefaultCurseAttachment().getThumbnailUrl() != null){
            mod.setIconUrl(curseAddon.getDefaultCurseAttachment().getThumbnailUrl());
        }
        mod.setId(curseAddon.getSlug());
        if (includeFiles){
            mod.setMinecraftVersions(convertCurseFilesToMinecraftVersions(curseAddon.getFiles()));
        }
        mod.setName(curseAddon.getName());
        author.getMods().add(mod);
        return author;
    }

    public static ArrayList<MinecraftVersion> convertCurseFilesToMinecraftVersions(ArrayList<CurseFile> curseFiles){
        Mod mod = new Mod();
        mod.setMinecraftVersions(new ArrayList<MinecraftVersion>());
        for (CurseFile curseFile: curseFiles){
            for (String minecraftVersion: curseFile.getGameVersion()){
                if (mod.containsMinecraftVersion(minecraftVersion)){
                    MinecraftVersion currentMcVer = mod.getMinecraftVersion(minecraftVersion);
                    ModVersion modVersion = new ModVersion();
                    modVersion.setModVersion(curseFile.getDisplayName());//Fixme: trim jar extension;
                    modVersion.setModUrl(curseFile.getDownloadUrl());
                    currentMcVer.getModVersions().add(modVersion);
                    //Fixme: loop dependency here
                }else{
                    MinecraftVersion mcVer = new MinecraftVersion();
                    mcVer.setMinecraftVersion(minecraftVersion);
                    mcVer.setModVersions(new ArrayList<ModVersion>());
                    ModVersion modVersion = new ModVersion();
                    modVersion.setModVersion(curseFile.getDisplayName());//Fixme: trim jar extension;
                    modVersion.setModUrl(curseFile.getDownloadUrl());
                    //Fixme: loop dependency here
                    mcVer.getModVersions().add(modVersion);
                    mod.getMinecraftVersions().add(mcVer);
                }
            }
        }
        return mod.getMinecraftVersions();
    }

    public static Author mergeAuthors(Author author, Author author2){
        if (author.getName().equalsIgnoreCase(author2.getName())){
            Author authorNew = author;
            for (Mod mod: author2.getMods()){
                authorNew.getMods().add(mod);
            }
            return authorNew;
        }
        return author;
    }

}
