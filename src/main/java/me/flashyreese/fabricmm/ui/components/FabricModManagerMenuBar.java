package me.flashyreese.fabricmm.ui.components;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmm.util.ModUtils;
import me.flashyreese.fabricmrf.Repository;
import me.flashyreese.fabricmrf.RepositoryManager;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

public class FabricModManagerMenuBar extends JMenuBar {

    private JMenu helpMenu;
    private JMenuItem openMinecraftLauncher;
    private JMenuItem updateLocalRepositories;
    private JMenuItem checkForUpdates;

    public FabricModManagerMenuBar(RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI){
        initComponents();
        setupComponents(repositoryManager, modRepositoryBrowserUI);
        loadComponents();
    }

    private void initComponents(){
        helpMenu = new JMenu();
        openMinecraftLauncher = new JMenuItem();
        updateLocalRepositories = new JMenuItem();
        checkForUpdates = new JMenuItem();
    }

    private void setupComponents(RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI){
        helpMenu.setText("Help");

        openMinecraftLauncher.setText("Open Minecraft Launcher");
        openMinecraftLauncher.addActionListener(e -> {
            File launcher = ModUtils.findDefaultLauncherPath();
            if(launcher.exists()){
                try {
                    Desktop.getDesktop().open(launcher);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        updateLocalRepositories.setText("Update Local Repositories");
        updateLocalRepositories.addActionListener(e -> {
            new Thread(() -> {
                for(Repository repository: repositoryManager.getRepositories()){
                    try {
                        repositoryManager.updateLocalRepository(repository);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                modRepositoryBrowserUI.updateModList(repositoryManager);
            }).start();
        });

        checkForUpdates.setText("Check for Updates...");
        checkForUpdates.addActionListener(e -> {
            new Thread(() -> {
                try{
                    URL url = new URL("https://api.github.com/repos/FlashyReese/fabric-mod-manager/releases");
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String json = in.lines().collect(Collectors.joining());
                    in.close();
                    JSONArray jsonArray = new JSONArray(json);
                    if(!jsonArray.isEmpty()){
                        Semver latest = new Semver(jsonArray.getJSONObject(0).getString("tag_name"), Semver.SemverType.STRICT);
                        if(Application.getVersion().isLowerThan(latest)){
                            Desktop.getDesktop().browse(new URI("https://github.com/FlashyReese/fabric-mod-manager/releases"));
                        }else{
                            JOptionPane.showMessageDialog(null, "Up to date!");
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }).start();
        });
    }

    private void loadComponents(){
        helpMenu.add(openMinecraftLauncher);
        helpMenu.add(updateLocalRepositories);
        helpMenu.add(checkForUpdates);
        this.add(helpMenu);
    }
}
