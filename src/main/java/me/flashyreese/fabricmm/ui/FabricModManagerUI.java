package me.flashyreese.fabricmm.ui;

import me.flashyreese.fabricmm.ui.components.FabricModManagerMenuBar;
import me.flashyreese.fabricmm.ui.tab.DownloadManagerUI;
import me.flashyreese.fabricmm.ui.tab.LibraryManagerUI;
import me.flashyreese.fabricmm.ui.tab.ModRepositoryBrowserUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class FabricModManagerUI extends JFrame {

    private JTabbedPane contentPane;
    private JPanel library;
    private JPanel modBrowser;
    private JPanel downloadManager;

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
        contentPane = new JTabbedPane();
        //Fixme: Jank
        contentPane.setLocation(0, 0);
        contentPane.setPreferredSize(new Dimension(854, 480));

        library = new LibraryManagerUI(contentPane);
        modBrowser = new ModRepositoryBrowserUI(contentPane);
        downloadManager = new DownloadManagerUI(contentPane);
    }

    private void setupComponents() {
        contentPane.updateUI();
        contentPane.addTab("Library", library); //Fixme: Panel Scaling macOS looks chopped off
        contentPane.addTab("Browse Mods", modBrowser);
        contentPane.addTab("Download Manager", downloadManager);
    }

    private void loadComponents(){
        setContentPane(contentPane);
        setJMenuBar(new FabricModManagerMenuBar());
    }
}
