package me.flashyreese.fabricmm.ui.components;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.common.i18n.TranslatableText;
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


    private JMenu quickToolsMenu;
    private JMenu repositoryMenu;
    private JMenu helpMenu;
    private JMenuItem openMinecraftLauncher;
    private JMenuItem updateLocalRepositories;
    private JMenuItem checkForUpdates;
    private JMenuItem about;

    public FabricModManagerMenuBar(RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI){
        initComponents();
        setupComponents(repositoryManager, modRepositoryBrowserUI);
        loadComponents();
    }

    private void initComponents(){
        quickToolsMenu = new JMenu();
        repositoryMenu = new JMenu();
        helpMenu = new JMenu();
        openMinecraftLauncher = new JMenuItem();
        updateLocalRepositories = new JMenuItem();
        checkForUpdates = new JMenuItem();
        about = new JMenuItem();
    }

    private void setupComponents(RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI){
        quickToolsMenu.setText(new TranslatableText("fmm.menubar.quick_tools").toString());

        openMinecraftLauncher.setText(new TranslatableText("fmm.menubar.quick_tools.open_minecraft_launcher").toString());
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

        repositoryMenu.setText(new TranslatableText("fmm.menubar.repository").toString());

        updateLocalRepositories.setText(new TranslatableText("fmm.menubar.repository.update_local_repositories").toString());
        updateLocalRepositories.addActionListener(e -> new Thread(() -> {
            for(Repository repository: repositoryManager.getRepositories()){
                try {
                    repositoryManager.updateLocalRepository(repository);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            modRepositoryBrowserUI.updateModList(repositoryManager);
        }).start());

        helpMenu.setText(new TranslatableText("fmm.menubar.help").toString());

        checkForUpdates.setText(new TranslatableText("fmm.menubar.help.check_for_updates").toString());
        checkForUpdates.addActionListener(e -> new Thread(() -> {
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
                        JOptionPane.showMessageDialog(null, new TranslatableText("fmm.menubar.help.check_for_updates.up_to_date").toString());
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }).start());

        about.setText(new TranslatableText("fmm.menubar.help.about").toString());
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, new MessageWithLink(
                String.format("Fabric Mod Manager %s <br><p>Source can be found at " +
                        "<a href=\"https://github.com/FlashyReese/fabric-mod-manager\">GitHub</a></p></br>",
                        Application.getVersion().toString()))));
    }

    private void loadComponents(){
        quickToolsMenu.add(openMinecraftLauncher);
        repositoryMenu.add(updateLocalRepositories);
        helpMenu.add(checkForUpdates);
        helpMenu.add(about);
        this.add(quickToolsMenu);
        this.add(repositoryMenu);
        this.add(helpMenu);
    }
}
