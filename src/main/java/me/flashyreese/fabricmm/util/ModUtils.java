package me.flashyreese.fabricmm.util;

import com.squareup.moshi.Moshi;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.FabricModMetadata;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.common.util.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ModUtils {

    public static InstalledMod getInstalledModFromJar(File file) throws IOException {
        InstalledMod installedMod = new InstalledMod();
        final JarFile jarFile = new JarFile(file);
        final JarEntry fabricSchema = jarFile.getJarEntry("fabric.mod.json");
        if (fabricSchema != null) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(fabricSchema), StandardCharsets.UTF_8));
            String fabricSchemaJson = bufferedReader.lines().collect(Collectors.joining());
            bufferedReader.close();
            FabricModMetadata fabricModMetadata = new Moshi.Builder().build().adapter(FabricModMetadata.class).fromJson(fabricSchemaJson);
            installedMod.setModMetadata(fabricModMetadata);
            assert fabricModMetadata != null;
            final JarEntry iconJarEntry = jarFile.getJarEntry(fabricModMetadata.getIcon() != null ? fabricModMetadata.getIcon() : "");
            if (iconJarEntry != null) {
                File iconFile = new File(ConfigurationManager.getInstance().ICON_CACHE_DIR + File.separator + String.format("%s.png", installedMod.getModMetadata().getId()));
                if (!iconFile.exists()) {
                    InputStream inputStream = jarFile.getInputStream(iconJarEntry);
                    FileOutputStream fileOutputStream = new FileOutputStream(iconFile);
                    while (inputStream.available() > 0) {
                        fileOutputStream.write(inputStream.read());
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }
                installedMod.setIconPath(iconFile.getAbsolutePath());
            }
            installedMod.setInstalledPath(file.getAbsolutePath());
            installedMod.assignMinecraftVersion();
        } else {
            return null;
        }
        jarFile.close();
        return installedMod;
    }

    public static List<InstalledMod> getInstalledModsFromDir(File dir) throws Exception {
        if (!dir.exists()) dir.mkdirs();
        List<InstalledMod> installedMods = new ArrayList<>();
        if (!dir.isDirectory()) {
            throw new Exception("This is not a directory???");
        }
        for (File file : Objects.requireNonNull(dir.listFiles((directory, fileName) -> fileName.endsWith(".jar") || fileName.endsWith(".fabricmod") || fileName.endsWith(".disabled")))) {
            InstalledMod installedMod = getInstalledModFromJar(file);
            if (installedMod != null)
                installedMods.add(installedMod);
        }
        return installedMods;
    }

    public static void changeInstalledModState(InstalledMod installedMod) {
        File newFile;
        if (installedMod.isEnabled()) {
            newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "disabled");
        } else {
            newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "jar");
        }
        installedMod.setInstalledPath(newFile.getAbsolutePath());
    }

    public static void changeInstalledModState(InstalledMod installedMod, boolean state) {
        File newFile;
        if (state != installedMod.isEnabled()) {
            if (state) {
                newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "jar");
            } else {
                newFile = FileUtil.changeExtension(new File(installedMod.getInstalledPath()), "disabled");
            }
            installedMod.setInstalledPath(newFile.getAbsolutePath());
        }
    }

}
