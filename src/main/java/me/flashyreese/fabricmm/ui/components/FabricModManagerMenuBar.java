package me.flashyreese.fabricmm.ui.components;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.common.i18n.I18nManager;
import me.flashyreese.common.i18n.TranslatableText;
import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmm.api.RepositoryManager;
import me.flashyreese.fabricmm.ui.FabricModManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmm.util.ModUtils;
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
    private JMenuItem updateLocalRepository;
    private JMenuItem checkForUpdates;
    private JMenuItem about;

    public FabricModManagerMenuBar(FabricModManagerUI fabricModManagerUI, RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI, I18nManager i18nManager){
        initComponents();
        setupComponents(fabricModManagerUI, repositoryManager, modRepositoryBrowserUI, i18nManager);
        loadComponents();
        updateComponentsText();
    }

    private void initComponents(){
        quickToolsMenu = new JMenu();
        repositoryMenu = new JMenu();
        languageMenu = new JMenu();
        helpMenu = new JMenu();
        openMinecraftLauncher = new JMenuItem();
        updateLocalRepository = new JMenuItem();
        checkForUpdates = new JMenuItem();
        about = new JMenuItem();
    }

    private void setupComponents(FabricModManagerUI fabricModManagerUI, RepositoryManager repositoryManager, ModRepositoryBrowserUI modRepositoryBrowserUI, I18nManager i18nManager){
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

        updateLocalRepository.addActionListener(e -> new Thread(() -> {
            try {
                repositoryManager.updateLocalRepository();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            modRepositoryBrowserUI.updateModList(repositoryManager);
        }).start());

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

        about.addActionListener(e -> JOptionPane.showMessageDialog(null, new MessageWithLink(
                String.format("Fabric Mod Manager %s <br><p>Source can be found at " +
                        "<a href=\"https://github.com/FlashyReese/fabric-mod-manager\">GitHub</a></p></br>",
                        Application.getVersion().toString()))));

        loadAvailableLocales(fabricModManagerUI, i18nManager);
    }

    private void loadComponents(){
        quickToolsMenu.add(openMinecraftLauncher);
        repositoryMenu.add(updateLocalRepository);
        helpMenu.add(checkForUpdates);
        helpMenu.add(about);
        this.add(quickToolsMenu);
        this.add(repositoryMenu);
        this.add(languageMenu);
        this.add(helpMenu);
    }

    public void updateComponentsText(){
        quickToolsMenu.setText(new TranslatableText("fmm.menubar.quick_tools").toString());
        openMinecraftLauncher.setText(new TranslatableText("fmm.menubar.quick_tools.open_minecraft_launcher").toString());
        repositoryMenu.setText(new TranslatableText("fmm.menubar.repository").toString());
        updateLocalRepository.setText(new TranslatableText("fmm.menubar.repository.update_local_repository").toString());
        languageMenu.setText(new TranslatableText("fmm.menubar.language").toString());
        helpMenu.setText(new TranslatableText("fmm.menubar.help").toString());
        checkForUpdates.setText(new TranslatableText("fmm.menubar.help.check_for_updates").toString());
        about.setText(new TranslatableText("fmm.menubar.help.about").toString());
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
                fabricModManagerUI.updateComponentsText();
            });
            languageMenu.add(menuItem);
        }
    }
}
