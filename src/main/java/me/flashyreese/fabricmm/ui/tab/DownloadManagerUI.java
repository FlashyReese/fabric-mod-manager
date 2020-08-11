package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.MinecraftInstance;
import me.flashyreese.fabricmm.ui.components.ModFileDropList;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.Util;

import javax.swing.*;
import java.awt.*;

public class DownloadManagerUI extends JPanel {

    private ModFileDropList downloadModFileDropList;
    private JComboBox<MinecraftInstance> minecraftInstances;
    private JButton install;

    public DownloadManagerUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents();
        loadComponents();
        updateComponentsText();
    }

    private void initComponents() {
        downloadModFileDropList = new ModFileDropList();
        minecraftInstances = new JComboBox<>();
        install = new JButton();
    }

    private void setupComponents() {
        Dim2i downloadModFileDropListDim = new Dim2i(10, 10, this.getWidth() - 20, this.getHeight() - 60);
        downloadModFileDropList.setLocation(downloadModFileDropListDim.getOriginX(), downloadModFileDropListDim.getOriginY());
        downloadModFileDropList.setSize(downloadModFileDropListDim.getWidth(), downloadModFileDropListDim.getHeight());
        downloadModFileDropList.setDirectory(ConfigurationManager.getInstance().MOD_CACHE_DIR);
        downloadModFileDropList.getList().addListSelectionListener(arg0 -> onModFileDropListSelect());

        Dim2i minecraftInstancesDim = new Dim2i(10, this.getHeight() - 40, this.getWidth() / 4 * 3 - 30, 30);
        minecraftInstances.setBounds(minecraftInstancesDim.getOriginX(), minecraftInstancesDim.getOriginY(), minecraftInstancesDim.getWidth(), minecraftInstancesDim.getHeight());
        for(MinecraftInstance minecraftInstance: Util.getMinecraftInstances()){
            minecraftInstances.addItem(minecraftInstance);
        }
        minecraftInstances.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof MinecraftInstance) {
                    renderer.setText(((MinecraftInstance) value).getName());
                }
                return renderer;
            }
        });
        Dim2i installDim = new Dim2i(this.getWidth() / 4 * 3 - 10, this.getHeight() - 40, this.getWidth() / 4, 30);
        install.setBounds(installDim.getOriginX(), installDim.getOriginY(), installDim.getWidth(), installDim.getHeight());
    }

    private void loadComponents() {
        this.add(downloadModFileDropList);
        this.add(minecraftInstances);
        this.add(install);
    }

    public void updateComponentsText(){
        install.setText(new I18nText("fmm.download_manager.install").toString());
        onModFileDropListSelect();
    }

    private void onModFileDropListSelect(){
        install.setEnabled(downloadModFileDropList.getSelectedValue() != null);
    }
}
