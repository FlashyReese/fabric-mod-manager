package me.flashyreese.fabricmm.ui;

import me.flashyreese.common.i18n.I18nManager;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.api.RepositoryManager;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.ui.components.FabricModManagerMenuBar;
import me.flashyreese.fabricmm.ui.tab.DownloadManagerUI;
import me.flashyreese.fabricmm.ui.tab.LibraryManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmm.ui.tab.SettingsUI;

import javax.swing.*;
import java.awt.*;

public class FabricModManagerUI extends JFrame {

    private final int SCALE = 70;

    private I18nManager i18nManager;
    private RepositoryManager repositoryManager;
    private JTabbedPane contentPane;
    private LibraryManagerUI library;
    private ModRepositoryBrowserUI modBrowser;
    private SystemTray tray;
    private TrayIcon trayIcon;
    private DownloadManagerUI downloadManager;
    private SettingsUI settings;

    private FabricModManagerMenuBar menuBar;

    public FabricModManagerUI() throws Exception {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
        setupComponents();
        loadComponents();
        pack();
        setLocationRelativeTo(null);
        updateComponentsText();
    }

    private void initComponents() throws Exception {
        i18nManager = new I18nManager("assets/lang");
        i18nManager.setLocale(i18nManager.getLocale(ConfigurationManager.getInstance().getSettings().getLocale()));

        tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemClassLoader().getResource("icon.png")), new I18nText("fmm.title").toString());

        repositoryManager = new RepositoryManager(ConfigurationManager.getInstance().REPOSITORY_CACHE_DIR, "https://raw.githubusercontent.com/FlashyReese/fabric-mod-repository/master/curse.json");
        contentPane = new JTabbedPane();
        //Fixme: Jank
        contentPane.setLocation(0, 0);
        contentPane.setPreferredSize(new Dimension(16* SCALE, 9* SCALE));

        library = new LibraryManagerUI(contentPane);
        downloadManager = new DownloadManagerUI(contentPane, trayIcon);
        modBrowser = new ModRepositoryBrowserUI(contentPane, repositoryManager, downloadManager, trayIcon);
        settings = new SettingsUI(contentPane);

        menuBar = new FabricModManagerMenuBar(this, repositoryManager, library, modBrowser, i18nManager);
    }

    private void setupComponents() {
        contentPane.updateUI();

        trayIcon.setImageAutoSize(true);
    }

    private void loadComponents() throws AWTException {
        tray.add(trayIcon);
        setContentPane(contentPane);
        setJMenuBar(menuBar);
    }

    public void updateComponentsText(){
        int index = contentPane.getSelectedIndex();
        contentPane.removeAll();
        contentPane.addTab(new I18nText("fmm.library").toString(), library); //Fixme: Panel Scaling macOS looks cropped off
        contentPane.addTab(new I18nText("fmm.mod_browser").toString(), modBrowser);
        contentPane.addTab(new I18nText("fmm.download_manager").toString(), downloadManager);
        contentPane.addTab(new I18nText("fmm.settings").toString(), settings);
        setTitle(new I18nText("fmm.title").toString());
        library.updateComponentsText();
        modBrowser.updateComponentsText();
        menuBar.updateComponentsText();
        settings.updateComponentsText();
        contentPane.setSelectedIndex(index == -1 ? 0 : index);
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public ModRepositoryBrowserUI getModBrowser() {
        return modBrowser;
    }
}
