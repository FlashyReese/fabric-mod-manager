package me.flashyreese.fabricmm.ui;

import me.flashyreese.common.i18n.I18nManager;
import me.flashyreese.common.i18n.TranslatableText;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.ui.components.FabricModManagerMenuBar;
import me.flashyreese.fabricmm.ui.tab.LibraryManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmrf.RepositoryManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class FabricModManagerUI extends JFrame {

    private I18nManager i18nManager;
    private RepositoryManager repositoryManager;
    private JTabbedPane contentPane;
    private LibraryManagerUI library;
    private ModRepositoryBrowserUI modBrowser;
    private SystemTray tray;
    private TrayIcon trayIcon;
    //private DownloadManagerUI downloadManager;

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
        i18nManager.setLocale(Locale.US);

        tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemClassLoader().getResource("icon.png")), new TranslatableText("fmm.title").toString());

        repositoryManager = new RepositoryManager(ConfigurationManager.getInstance().REPOSITORY_CACHE_DIR, ConfigurationManager.getInstance().getRepositories());
        contentPane = new JTabbedPane();
        //Fixme: Jank
        contentPane.setLocation(0, 0);
        contentPane.setPreferredSize(new Dimension(854, 480));

        library = new LibraryManagerUI(contentPane);
        modBrowser = new ModRepositoryBrowserUI(contentPane, repositoryManager, trayIcon);
        //downloadManager = new DownloadManagerUI(contentPane);

        menuBar = new FabricModManagerMenuBar(this, repositoryManager, modBrowser, i18nManager);
    }

    private void setupComponents() {
        contentPane.updateUI();
        //contentPane.addTab("Download Manager", downloadManager);

        trayIcon.setImageAutoSize(true);
        //trayIcon.setToolTip("");
    }

    private void loadComponents() throws AWTException {
        tray.add(trayIcon);
        setContentPane(contentPane);
        setJMenuBar(menuBar);
    }

    public void updateComponentsText(){
        contentPane.removeAll();
        contentPane.addTab(new TranslatableText("fmm.library").toString(), library); //Fixme: Panel Scaling macOS looks chopped off
        contentPane.addTab(new TranslatableText("fmm.mod_browser").toString(), modBrowser);
        setTitle(new TranslatableText("fmm.title").toString());
        library.updateComponentsText();
        modBrowser.updateComponentsText();
        menuBar.updateComponentsText();
    }

}
