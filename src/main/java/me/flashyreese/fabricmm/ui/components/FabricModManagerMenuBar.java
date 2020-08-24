package me.flashyreese.fabricmm.ui.components;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.common.i18n.I18nManager;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmm.api.RepositoryManager;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.ui.FabricModManagerUI;
import me.flashyreese.fabricmm.ui.tab.DownloadManagerUI;
import me.flashyreese.fabricmm.ui.tab.LibraryManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmm.util.Util;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.stream.Collectors;

public class FabricModManagerMenuBar extends JMenuBar {


    private JMenu quickToolsMenu;
    private JMenu repositoryMenu;
    private JMenu languageMenu;
    private JMenu helpMenu;
    private JMenuItem openMinecraftLauncher;
    private JMenuItem openMMCLauncher;
    private JMenuItem updateLocalRepository;
    private JMenuItem refreshMinecraftInstances;
    private JMenuItem checkForUpdates;
    private JMenuItem about;

    public FabricModManagerMenuBar(FabricModManagerUI fabricModManagerUI, RepositoryManager repositoryManager, LibraryManagerUI library, ModRepositoryBrowserUI modRepositoryBrowserUI, DownloadManagerUI downloadManagerUI, I18nManager i18nManager){
        initComponents();
        setupComponents(fabricModManagerUI, repositoryManager, library, modRepositoryBrowserUI, downloadManagerUI, i18nManager);
        loadComponents();
        updateComponentsText();
    }

    private void initComponents(){
        quickToolsMenu = new JMenu();
        repositoryMenu = new JMenu();
        languageMenu = new JMenu();
        helpMenu = new JMenu();
        openMinecraftLauncher = new JMenuItem();
        openMMCLauncher = new JMenuItem();
        updateLocalRepository = new JMenuItem();
        refreshMinecraftInstances = new JMenuItem();
        checkForUpdates = new JMenuItem();
        about = new JMenuItem();
    }

    private void setupComponents(FabricModManagerUI fabricModManagerUI, RepositoryManager repositoryManager, LibraryManagerUI library, ModRepositoryBrowserUI modRepositoryBrowserUI, DownloadManagerUI downloadManagerUI, I18nManager i18nManager){
        openMinecraftLauncher.addActionListener(e -> {
            File launcher = Util.findDefaultLauncherPath();
            if(launcher.exists()){
                try {
                    Desktop.getDesktop().open(launcher);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        openMMCLauncher.addActionListener(e -> {
            File launcher = Util.getMMCLauncher();
            if(launcher != null){
                if (launcher.exists()){
                    try {
                        Desktop.getDesktop().open(launcher);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, new I18nText("fmm.menubar.quick_tools.open_mmc_launcher.not_available").toString());
                }
            }else{
                JOptionPane.showMessageDialog(null, new I18nText("fmm.menubar.quick_tools.open_mmc_launcher.not_available").toString());
            }
        });

        updateLocalRepository.addActionListener(e -> new Thread(() -> {
            try {
                repositoryManager.clearLocalRepository();
                repositoryManager.updateLocalRepository();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            modRepositoryBrowserUI.updateModList(repositoryManager);
        }).start());

        refreshMinecraftInstances.addActionListener(e -> {
            Util.getMinecraftInstances().clear();
            try {
                Util.findMinecraftInstances();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            library.updateInstances();
            downloadManagerUI.updateInstances();
        });

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
                        JOptionPane.showMessageDialog(null, new I18nText("fmm.menubar.help.check_for_updates.up_to_date").toString());
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }).start());

        about.addActionListener(e -> JOptionPane.showMessageDialog(null, new MessageWithLink(
                String.format("Fabric Mod Manager %s <br><p>Source can be found at " +
                        "<a href=\"https://github.com/FlashyReese/fabric-mod-manager\">GitHub</a></p></br>",
                        Application.getVersion().toString()))));

        loadAvailableLocales(fabricModManagerUI, i18nManager);
    }

    private void loadComponents(){
        quickToolsMenu.add(openMinecraftLauncher);
        quickToolsMenu.add(openMMCLauncher);
        repositoryMenu.add(updateLocalRepository);
        helpMenu.add(refreshMinecraftInstances);
        helpMenu.add(checkForUpdates);
        helpMenu.add(about);
        this.add(quickToolsMenu);
        this.add(repositoryMenu);
        this.add(languageMenu);
        this.add(helpMenu);
    }

    public void updateComponentsText(){
        quickToolsMenu.setText(new I18nText("fmm.menubar.quick_tools").toString());
        openMinecraftLauncher.setText(new I18nText("fmm.menubar.quick_tools.open_minecraft_launcher").toString());
        openMMCLauncher.setText(new I18nText("fmm.menubar.quick_tools.open_mmc_launcher").toString());
        repositoryMenu.setText(new I18nText("fmm.menubar.repository").toString());
        updateLocalRepository.setText(new I18nText("fmm.menubar.repository.update_local_repository").toString());
        languageMenu.setText(new I18nText("fmm.menubar.language").toString());
        helpMenu.setText(new I18nText("fmm.menubar.help").toString());
        refreshMinecraftInstances.setText(new I18nText("fmm.menubar.help.refresh_minecraft_instances").toString());
        checkForUpdates.setText(new I18nText("fmm.menubar.help.check_for_updates").toString());
        about.setText(new I18nText("fmm.menubar.help.about").toString());
    }

    private void loadAvailableLocales(FabricModManagerUI fabricModManagerUI, I18nManager i18nManager){
        languageMenu.removeAll();
        for (Locale locale: i18nManager.getAvailableLocales()){
            JMenuItem menuItem = new JMenuItem();
            menuItem.setText(locale.getDisplayName());
            menuItem.addActionListener(e -> {
                try {
                    i18nManager.setLocale(locale);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ConfigurationManager.getInstance().getSettings().setLocale(locale.toString().toLowerCase());
                ConfigurationManager.getInstance().saveSettings();
                fabricModManagerUI.updateComponentsText();
            });
            languageMenu.add(menuItem);
        }
    }
}
