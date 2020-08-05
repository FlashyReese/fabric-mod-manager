package me.flashyreese.fabricmm.ui;

import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.ui.components.FabricModManagerMenuBar;
import me.flashyreese.fabricmm.ui.tab.LibraryManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;
import me.flashyreese.fabricmrf.RepositoryManager;

import javax.swing.*;
import java.awt.*;

public class FabricModManagerUI extends JFrame {


    private RepositoryManager repositoryManager;
    private JTabbedPane contentPane;
    private LibraryManagerUI library;
    private ModRepositoryBrowserUI modBrowser;
    private SystemTray tray;
    private TrayIcon trayIcon;
    //private DownloadManagerUI downloadManager;

    public FabricModManagerUI() throws Exception {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fabric Mod Manager");
        setLayout(null);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
        setupComponents();
        loadComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() throws Exception {
        tray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemClassLoader().getResource("icon.png")), "Fabric Mod Manager");

        repositoryManager = new RepositoryManager(ConfigurationManager.getInstance().REPOSITORY_CACHE_DIR, ConfigurationManager.getInstance().getRepositories());
        contentPane = new JTabbedPane();
        //Fixme: Jank
        contentPane.setLocation(0, 0);
        contentPane.setPreferredSize(new Dimension(854, 480));

        library = new LibraryManagerUI(contentPane);
        modBrowser = new ModRepositoryBrowserUI(contentPane, repositoryManager, trayIcon);
        //downloadManager = new DownloadManagerUI(contentPane);
    }

    private void setupComponents() {
        contentPane.updateUI();
        contentPane.addTab("Library", library); //Fixme: Panel Scaling macOS looks chopped off
        contentPane.addTab("Browse Mods", modBrowser);
        //contentPane.addTab("Download Manager", downloadManager);

        trayIcon.setImageAutoSize(true);
        //trayIcon.setToolTip("");
    }

    private void loadComponents() throws AWTException {
        tray.add(trayIcon);
        setContentPane(contentPane);
        setJMenuBar(new FabricModManagerMenuBar(repositoryManager, modBrowser));
    }
}
