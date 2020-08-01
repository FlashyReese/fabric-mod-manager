package me.flashyreese.fabricmm.ui.tab;

import javax.swing.*;
import java.awt.*;

public class DownloadManagerUI extends JPanel {

    public DownloadManagerUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents();
        loadComponents();
    }

    private void initComponents() {

    }

    private void setupComponents() {

    }

    private void loadComponents() {

    }
}
