package me.flashyreese.fabricmm.util;

import com.squareup.moshi.Moshi;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.common.util.FileUtil;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.mmc.Component;
import me.flashyreese.fabricmm.schema.mmc.Package;
import me.flashyreese.fabricmm.schema.MinecraftInstance;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    private static final List<MinecraftInstance> minecraftInstances = new ArrayList<>();

    public static List<File> getValidMinecraftInstanceDir(File mmcDir) {
        List<File> files = new ArrayList<>();
        File instances = new File(mmcDir, "instances");
        if (instances.exists()) {
            Arrays.stream(instances.listFiles()).forEach(instance -> {
                if (instance.isDirectory()) {
                    File mmcPackJson = new File(instance, "mmc-pack.json");
                    if (mmcPackJson.exists()) {
                        files.add(instance);
                    }
                }
            });
        }
        return files;
    }

    public static List<MinecraftInstance> getFabricMinecraftInstanceDir(File mmcDir) {
        List<MinecraftInstance> minecraftInstances = new ArrayList<>();
        getValidMinecraftInstanceDir(mmcDir).forEach(instance -> {
            File mmcPackJsonFile = new File(instance, "mmc-pack.json");
            File instanceConfig = new File(instance, "instance.cfg");

            List<String> instanceConfigList = FileUtil.readLines(instanceConfig);
            Map<String, String> instanceConfigMap = new HashMap<>();

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(mmcPackJsonFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader buffered = new BufferedReader(fileReader);
            String mmcPackJson = buffered.lines().collect(Collectors.joining());

            Package mmcPackage = null;
            try {
                mmcPackage = new Moshi.Builder().build().adapter(Package.class).fromJson(mmcPackJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String line : instanceConfigList) {
                String delimiter = "=";
                String[] split = line.split(delimiter);
                String key = split[0];
                String value = split.length == 2 ? split[1] : "";
                instanceConfigMap.put(key, value);
            }

            boolean isFabric = false;
            String version = "";
            String name = "";

            for (Component component : mmcPackage.getComponents()) {
                if (component.getUid().equals("net.fabricmc.fabric-loader")) {
                    isFabric = true;
                }
                if (component.getCachedName().equals("Minecraft")) {
                    version = component.getVersion();
                }
            }
            for (Map.Entry<String, String> entry : instanceConfigMap.entrySet()) {
                if (entry.getKey().equals("name")) {
                    name = entry.getValue().isEmpty() ? instance.getName() : entry.getValue();
                }
            }
            if (isFabric) {
                MinecraftInstance minecraftInstance = new MinecraftInstance();
                minecraftInstance.setName(name);
                minecraftInstance.setDirectory(new File(instance + File.separator + ".minecraft" + File.separator + "mods"));
                minecraftInstance.setMinecraftVersion(version);
                minecraftInstances.add(minecraftInstance);
            }
        });
        return minecraftInstances;
    }

    public static File getMMCLauncher() {
        File file = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win") && !ConfigurationManager.getInstance().getSettings().getMmcPath().isEmpty()) {
            file = new File(ConfigurationManager.getInstance().getSettings().getMmcPath(), "MultiMC.exe");
        } else if (os.contains("mac") && new File(File.separator + "Applications" + File.separator + "MultiMC.app").exists()) {
            file = new File(File.separator + "Applications" + File.separator + "MultiMC.app");
        }
        return file;
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

    public static File findDefaultLauncherPath() {
        File file;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win") && System.getenv("ProgramFiles(X86)") != null) {
            file = new File(System.getenv("ProgramFiles(X86)"), "/Minecraft Launcher/MinecraftLauncher.exe");
        } else if (os.contains("mac")) {
            file = new File(File.separator + "Applications" + File.separator + "Minecraft.app");
        } else {
            file = new File("/opt/minecraft-launcher/minecraft-launcher");//Fixme: currently broken
        }
        return file;
    }

    public static File getModsDirectory() {
        return new File(findDefaultInstallDir().getAbsolutePath() + File.separator + "mods");
    }

    public static List<MinecraftInstance> getMMCInstances() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win") && !ConfigurationManager.getInstance().getSettings().getMmcPath().isEmpty()) {
            return getFabricMinecraftInstanceDir(new File(ConfigurationManager.getInstance().getSettings().getMmcPath()));
        } else if (os.contains("mac") && new File(File.separator + "Applications" + File.separator + "MultiMC.app").exists()) {
            return getFabricMinecraftInstanceDir(new File(File.separator + "Applications" + File.separator + "MultiMC.app" + File.separator + "Contents" + File.separator + "MacOS"));
        }
        return null;
    }

    public static void findMinecraftInstances() {
        MinecraftInstance defaultMinecraftInstance = new MinecraftInstance();
        defaultMinecraftInstance.setDirectory(getModsDirectory());
        defaultMinecraftInstance.setMinecraftVersion("");
        defaultMinecraftInstance.setName(new I18nText("fmm.default_minecraft").toString());
        minecraftInstances.add(defaultMinecraftInstance);
        if (getMMCInstances() != null) {
            minecraftInstances.addAll(getMMCInstances());
        }
    }

    public static List<MinecraftInstance> getMinecraftInstances() {
        return minecraftInstances;
    }

}
