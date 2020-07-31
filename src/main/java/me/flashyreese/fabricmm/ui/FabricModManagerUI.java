package me.flashyreese.fabricmm.ui;

import me.flashyreese.fabricmm.ui.components.swing.LibraryManagerUI;

import javax.swing.*;
import java.awt.*;

public class FabricModManagerUI extends JFrame {

    private JTabbedPane contentPane;

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

    private void initComponents(){
        contentPane = new JTabbedPane();
    }

    private void setupComponents() throws Exception {
        contentPane.setLocation(0, 0);
        contentPane.setPreferredSize(new Dimension(1000, 700));
        contentPane.addTab("Library", new LibraryManagerUI(contentPane));

    }

    private void loadComponents(){
        setContentPane(contentPane);
    }
}
