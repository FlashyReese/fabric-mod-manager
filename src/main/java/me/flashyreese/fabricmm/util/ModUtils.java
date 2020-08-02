package me.flashyreese.fabricmm.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.schema.repository.Author;
import me.flashyreese.fabricmm.schema.repository.Mod;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static File getModsDirectory(){
        return new File(findDefaultInstallDir().getAbsolutePath() + File.separator + "mods");
    }

    public static InstalledMod changeInstalledModState(InstalledMod installedMod){
        File newFile;
        if(installedMod.isEnabled()){
            newFile = changeExtension(new File(installedMod.getInstalledPath()), "fabricmod");
        }else{
            newFile = changeExtension(new File(installedMod.getInstalledPath()), "jar");
        }
        installedMod.setInstalledPath(newFile.getAbsolutePath());
        return installedMod;
    }

    public static File changeExtension(File file, String extension) {
        String filename = file.getName();

        if (filename.contains(".")) {
            filename = filename.substring(0, filename.lastIndexOf('.'));
        }
        filename += "." + extension;

        File newFile = file;
        if(file.renameTo(new File(file.getParentFile(), filename))){
            newFile = new File(file.getParentFile(), filename);
        }
        return newFile;
    }


    public static List<Author> getAuthors() throws FileNotFoundException {
        return new Gson().fromJson(new FileReader("repo.json"), new TypeToken<List<Author>>(){}.getType());
    }

    public static List<Mod> getModList() throws IOException {
        List<Mod> modList = new ArrayList<Mod>();
        for(Author author:  getAuthorsFromRepositories()){
            for (Mod mod: author.getMods()){
                mod.setAuthor(author);
                modList.add(mod);
            }
        }
        return modList;
    }

    public static List<Author> getAuthorsFromRepository(String repositoryUrl) throws IOException {
        List<Author> authors = new ArrayList<Author>();
        URL url = new URL(repositoryUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String json = in.lines().collect(Collectors.joining());
        in.close();
        List<String> listOfAuthors = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
        for(String authorUrl: listOfAuthors){
            URL url2 = new URL(authorUrl);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream()));
            String json2 = in2.lines().collect(Collectors.joining());
            Author author = new Gson().fromJson(json2, Author.class);
            authors.add(author);
        }
        return authors;
    }

    public static List<Author> getAuthorsFromRepositories() throws IOException {
        List<Author> authors = new ArrayList<Author>();
        ArrayList<String> repositories = ConfigurationManager.getInstance().getRepositories();
        for(String url: repositories){
            authors.addAll(getAuthorsFromRepository(url));
        }
        return authors;
    }
}
