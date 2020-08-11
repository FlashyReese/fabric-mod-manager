package me.flashyreese.fabricmm.util;

import com.squareup.moshi.Moshi;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.mmc.Component;
import me.flashyreese.fabricmm.mmc.Package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static List<File> getValidMinecraftInstanceDir(File mmcDir){
        List<File> files = new ArrayList<>();
        File instances = new File(mmcDir, "instances");
        if (instances.exists()){
            for(File instance: Objects.requireNonNull(instances.listFiles())){
                if (instance.isDirectory()){
                    File mmcPackJson = new File(instance, "mmc-pack.json");
                    if (mmcPackJson.exists()){
                        files.add(instance);
                    }
                }
            }
        }
        return files;
    }

    public static Map<File, String> getFabricMinecraftInstanceDir(File mmcDir) throws IOException {
        Map<File, String> files = new HashMap<>();
        for (File instance: getValidMinecraftInstanceDir(mmcDir)){
            File mmcPackJsonFile = new File(instance, "mmc-pack.json");
            FileReader fileReader = new FileReader(mmcPackJsonFile);
            BufferedReader buffered = new BufferedReader(fileReader);
            String mmcPackJson = buffered.lines().collect(Collectors.joining());
            Package mmcPackage = new Moshi.Builder().build().adapter(Package.class).fromJson(mmcPackJson);
            boolean isFabric = false;
            String version = "";
            for (Component component: mmcPackage.getComponents()){
                if (component.getUid().equals("net.fabricmc.fabric-loader")){
                    isFabric = true;
                }
                if (component.getCachedName().equals("Minecraft")){
                    version = component.getVersion();
                }
            }
            if (isFabric){
                files.put(instance, version);
            }
        }
        return files;
    }

    public static File getMMCLauncher(){
        File file = null;
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win") && !ConfigurationManager.getInstance().getSettings().getMmcPath().isEmpty()){
            file = new File(ConfigurationManager.getInstance().getSettings().getMmcPath(), "MultiMC.exe");
        }else if(os.contains("mac") && new File(File.separator + "Applications" + File.separator + "MultiMC.app").exists()){
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

    ///Applications/MultiMC.app/Contents/MacOS/

    public static File getModsDirectory(){
        return new File(findDefaultInstallDir().getAbsolutePath() + File.separator + "mods");
    }

}
