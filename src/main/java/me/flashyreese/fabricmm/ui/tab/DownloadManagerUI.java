package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.schema.MinecraftInstance;
import me.flashyreese.fabricmm.ui.components.ModFileDropList;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.Util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class DownloadManagerUI extends JPanel {

    private ModFileDropList downloadModFileDropList;
    private JComboBox<MinecraftInstance> minecraftInstances;
    private JButton install;

    public DownloadManagerUI(JTabbedPane jTabbedPane, TrayIcon trayIcon) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents(trayIcon);
        loadComponents();
        updateComponentsText();
    }

    private void initComponents() {
        downloadModFileDropList = new ModFileDropList();
        minecraftInstances = new JComboBox<>();
        install = new JButton();
    }

    private void setupComponents(TrayIcon trayIcon) throws Exception {
        Dim2i downloadModFileDropListDim = new Dim2i(10, 10, this.getWidth() - 20, this.getHeight() - 60);
        downloadModFileDropList.setLocation(downloadModFileDropListDim.getOriginX(), downloadModFileDropListDim.getOriginY());
        downloadModFileDropList.setSize(downloadModFileDropListDim.getWidth(), downloadModFileDropListDim.getHeight());
        downloadModFileDropList.setDirectory(ConfigurationManager.getInstance().MOD_CACHE_DIR);
        downloadModFileDropList.reloadMods();
        downloadModFileDropList.getList().addListSelectionListener(arg0 -> onModFileDropListSelect());

        Dim2i minecraftInstancesDim = new Dim2i(10, this.getHeight() - 40, this.getWidth() / 4 * 3 - 30, 30);
        minecraftInstances.setBounds(minecraftInstancesDim.getOriginX(), minecraftInstancesDim.getOriginY(), minecraftInstancesDim.getWidth(), minecraftInstancesDim.getHeight());
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
        install.addActionListener(e -> {
            try {
                onInstallMod(trayIcon);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    }

    private void loadComponents() {
        this.add(downloadModFileDropList);
        this.add(minecraftInstances);
        this.add(install);
    }

    public void updateComponentsText(){
        install.setText(new I18nText("fmm.download_manager.install").toString());
        onModFileDropListSelect();
        updateInstances();
        loadLastSelectedInstance();
    }

    private void loadLastSelectedInstance() {
        if (ConfigurationManager.getInstance().getSettings().getLastSelectedInstance() != null){
            boolean containsItem = false;
            int index = -1;
            int size = minecraftInstances.getItemCount();
            for (int i = 0; i < size; i++) {
                MinecraftInstance item = minecraftInstances.getItemAt(i);
                if (item.getName().equals(ConfigurationManager.getInstance().getSettings().getLastSelectedInstance())){
                    containsItem = true;
                    index = i;
                    break;
                }
            }
            if (containsItem){
                minecraftInstances.setSelectedIndex(index);
            }
        }
    }

    public void updateInstances(){
        minecraftInstances.removeAllItems();
        for(MinecraftInstance minecraftInstance: Util.getMinecraftInstances()){
            minecraftInstances.addItem(minecraftInstance);
        }
    }

    private void onModFileDropListSelect(){
        install.setEnabled(downloadModFileDropList.getSelectedValue() != null);
    }

    private void onInstallMod(TrayIcon trayIcon) throws IOException {
        if (downloadModFileDropList.getSelectedValue() != null){
            InstalledMod mod = downloadModFileDropList.getSelectedValue();
            File currentLocation = new File(mod.getInstalledPath());
            MinecraftInstance instance = (MinecraftInstance) minecraftInstances.getSelectedItem();
            File newLocation = new File(instance.getDirectory(), currentLocation.getName());
            Path FROM = Paths.get(currentLocation.getAbsolutePath());
            Path TO = Paths.get(newLocation.getAbsolutePath());
            CopyOption[] options = new CopyOption[]{
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES
            };
            Files.copy(FROM, TO, options);
            trayIcon.displayMessage(new I18nText("fmm.download_manager.tray_icon.install_complete.caption").toString(),
                    new ParsableI18nText("fmm.download_manager.tray_icon.install_complete.text",
                            mod.getModMetadata().getName(), instance.getName()).toString(), TrayIcon.MessageType.INFO);
        }
    }

    public void refreshMods() throws Exception {
        downloadModFileDropList.reloadMods();
    }
}
