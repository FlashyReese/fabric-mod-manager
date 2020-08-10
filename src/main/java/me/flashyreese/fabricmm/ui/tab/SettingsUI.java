package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.util.Dim2i;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URISyntaxException;

public class SettingsUI extends JPanel {

    private JLabel mmcPathLabel;
    private JTextField mmcPath;
    private JButton mmcPathChooser;

    public SettingsUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents();
        loadComponents();
        updateComponentsText();
    }

    private void initComponents() {
        mmcPathLabel = new JLabel();
        mmcPath = new JTextField();
        mmcPathChooser = new JButton();
    }

    private void setupComponents() {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Dim2i mmcPathLabelDim = new Dim2i(10, 10, this.getWidth() / 4, 30);
        mmcPathLabel.setBounds(mmcPathLabelDim.getOriginX(), mmcPathLabelDim.getOriginY(), mmcPathLabelDim.getWidth(), mmcPathLabelDim.getHeight());
        mmcPathLabel.setFont(labelFont);

        Dim2i mmcPathDim = new Dim2i(this.getWidth() / 4 + 10,  10, this.getWidth() - (this.getWidth() / 4 + 10) - 60, 30);
        mmcPath.setBounds(mmcPathDim.getOriginX(), mmcPathDim.getOriginY(), mmcPathDim.getWidth(), mmcPathDim.getHeight());
        mmcPath.setText(ConfigurationManager.getInstance().getSettings().getMmcPath());
        mmcPath.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    File file = new File(mmcPath.getText());
                    if (file.exists()){
                        ConfigurationManager.getInstance().getSettings().setMmcPath(mmcPath.getText());
                        ConfigurationManager.getInstance().saveSettings();
                    }
                }
            }

        });

        Dim2i mmcPathChooserDim = new Dim2i(this.getWidth() - 50, 10, 40, 30);
        mmcPathChooser.setBounds(mmcPathChooserDim.getOriginX(), mmcPathChooserDim.getOriginY(), mmcPathChooserDim.getWidth(), mmcPathChooserDim.getHeight());
        mmcPathChooser.setText("...");
        mmcPathChooser.addActionListener(e -> {
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (!ConfigurationManager.getInstance().getSettings().getMmcPath().isEmpty()){
                chooser.setCurrentDirectory(new File(ConfigurationManager.getInstance().getSettings().getMmcPath()));
            }else{
                try {
                    chooser.setCurrentDirectory(new File(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation()
                            .toURI()).getPath()));
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                }
            }
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getAbsolutePath();
                mmcPath.setText(path);
                ConfigurationManager.getInstance().getSettings().setMmcPath(path);
                ConfigurationManager.getInstance().saveSettings();
            }
        });
    }

    private void loadComponents() {
        add(mmcPathLabel);
        add(mmcPath);
        add(mmcPathChooser);
    }


    public void updateComponentsText() {
        mmcPathLabel.setText(new I18nText("fmm.settings.mmc_path").toString());
    }
}
