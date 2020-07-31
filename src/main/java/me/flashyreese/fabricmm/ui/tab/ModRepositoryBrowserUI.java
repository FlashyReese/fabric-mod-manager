package me.flashyreese.fabricmm.ui.tab;

import javax.swing.*;
import java.awt.*;

public class ModRepositoryBrowserUI extends JPanel {

    private JButton hello;

    public ModRepositoryBrowserUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 4, (int)jTabbedPane.getPreferredSize().getHeight() - 24));//Fixme: Jank AF
        initComponents();
        setupComponents();
        loadComponents();
    }

    private void initComponents() {
        hello = new JButton();
    }

    private void setupComponents() {
        hello.setBounds(10, 10, this.getWidth() - 20, 30);
        hello.setText("Hello");
        hello.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "This does nothing at the moment");
        });
    }

    private void loadComponents() {
        this.add(hello);
    }

}
