package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.ParsableTranslatableText;
import me.flashyreese.common.i18n.TranslatableText;
import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmrf.RepositoryManager;
import me.flashyreese.fabricmrf.schema.repository.MinecraftVersion;
import me.flashyreese.fabricmrf.schema.repository.Mod;
import me.flashyreese.fabricmrf.schema.repository.ModVersion;
import me.flashyreese.fabricmm.ui.components.ModList;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.ModUtils;
import org.kamranzafar.jddl.DownloadListener;
import org.kamranzafar.jddl.DownloadTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ModRepositoryBrowserUI extends JPanel{

    private JTextField searchBar;
    private ModList modList;
    private JComboBox<String> filterType;
    private JComboBox<MinecraftVersion> minecraftVersion;
    private JLabel minecraftVersionLabel;
    private JComboBox<ModVersion> modVersion;
    private JLabel modVersionLabel;
    private JButton download;

    public ModRepositoryBrowserUI(JTabbedPane jTabbedPane, RepositoryManager repositoryManager, TrayIcon trayIcon) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents(repositoryManager, trayIcon);
        loadComponents();
        updateComponentsText();
    }

    private void initComponents() throws Exception {
        searchBar = new JTextField();
        modList = new ModList();
        filterType = new JComboBox<String>();
        minecraftVersion = new JComboBox<MinecraftVersion>();
        minecraftVersionLabel = new JLabel();
        modVersion = new JComboBox<ModVersion>();
        modVersionLabel = new JLabel();
        download = new JButton();
    }

    private void setupComponents(RepositoryManager repositoryManager, TrayIcon trayIcon){
        Dim2i searchBarDim = new Dim2i(10, 10, this.getWidth() / 8 * 3 - 20, 30);
        searchBar.setBounds(searchBarDim.getOriginX(), searchBarDim.getOriginY(), searchBarDim.getWidth(), searchBarDim.getHeight());
        updateModList(repositoryManager);
        searchBar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                modList.searchFilter(searchBar.getText(), (String)filterType.getSelectedItem());
            }
        });

        Dim2i modFileListDim = new Dim2i(10, 50, this.getWidth() / 2 - 20, this.getHeight() - 60);
        modList.setLocation(modFileListDim.getOriginX(), modFileListDim.getOriginY());
        modList.setSize(modFileListDim.getWidth(), modFileListDim.getHeight());
        modList.getList().addListSelectionListener(arg0 -> {
            onModListSelect();
        });

        Dim2i searchTypeDim = new Dim2i(this.getWidth() / 8 * 3, 10, this.getWidth() / 8 - 10, 30);
        filterType.setBounds(searchTypeDim.getOriginX(), searchTypeDim.getOriginY(), searchTypeDim.getWidth(), searchTypeDim.getHeight());

        Font labelFont = new Font("Tahoma", Font.BOLD, 12);
        minecraftVersionLabel.setBounds(this.getWidth() / 2, this.getHeight() - 120, this.getWidth() / 4, 30);
        minecraftVersionLabel.setFont(labelFont);
        Dim2i minecraftVersionDim = new Dim2i(this.getWidth() / 4 * 3 - 10, this.getHeight() - 120, this.getWidth() / 4, 30);
        minecraftVersion.setBounds(minecraftVersionDim.getOriginX(), minecraftVersionDim.getOriginY(), minecraftVersionDim.getWidth(), minecraftVersionDim.getHeight());
        minecraftVersion.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof MinecraftVersion) {
                    renderer.setText(((MinecraftVersion) value).getMinecraftVersion());
                }
                return renderer;
            }
        });
        minecraftVersion.addActionListener(e -> {
            updateModVersions();
        });

        modVersionLabel.setBounds(this.getWidth() / 2, this.getHeight() - 80, this.getWidth() / 4, 30);
        modVersionLabel.setFont(labelFont);
        Dim2i modVersionDim = new Dim2i(this.getWidth() / 4 * 3 - 10, this.getHeight() - 80, this.getWidth() / 4, 30);
        modVersion.setBounds(modVersionDim.getOriginX(), modVersionDim.getOriginY(), modVersionDim.getWidth(), modVersionDim.getHeight());
        modVersion.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof ModVersion) {
                    renderer.setText(((ModVersion) value).getModVersion());
                }
                return renderer;
            }
        });

        Dim2i downloadDim = new Dim2i(this.getWidth() / 2, this.getHeight() - 40, this.getWidth() / 2 - 10, 30);
        download.setBounds(downloadDim.getOriginX(), downloadDim.getOriginY(), downloadDim.getWidth(), downloadDim.getHeight());
        download.addActionListener(e -> {
            try {
                downloadMod(trayIcon);
            } catch (MalformedURLException | FileNotFoundException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        });
    }

    private void loadComponents() {
        this.add(searchBar);
        this.add(modList);
        this.add(filterType);
        this.add(minecraftVersion);
        this.add(minecraftVersionLabel);
        this.add(modVersion);
        this.add(modVersionLabel);
        this.add(download);
    }


    public void updateComponentsText(){
        filterType.removeAllItems();
        filterType.addItem(new TranslatableText("fmm.mod_browser.filter.general").toString());
        filterType.addItem(new TranslatableText("fmm.mod_browser.filter.name").toString());
        filterType.addItem(new TranslatableText("fmm.mod_browser.filter.author").toString());
        filterType.addItem(new TranslatableText("fmm.mod_browser.filter.minecraft_version").toString());
        minecraftVersionLabel.setText(new TranslatableText("fmm.mod_browser.minecraft_version").toString());
        modVersionLabel.setText(new TranslatableText("fmm.mod_browser.mod_version").toString());
        download.setText(new TranslatableText("fmm.mod_browser.download").toString());
        onModListSelect();
    }

    private void onModListSelect(){
        if(modList.getSelectedValue() != null){
            updateMinecraftVersions();
            minecraftVersion.setEnabled(true);
            modVersion.setEnabled(true);
            download.setEnabled(true);
        }else{
            minecraftVersion.setEnabled(false);
            modVersion.setEnabled(false);
            download.setEnabled(false);
        }
    }

    private void updateMinecraftVersions(){
        minecraftVersion.removeAllItems();
        minecraftVersion.setSelectedItem(null);
        for(MinecraftVersion mcVer: modList.getSelectedValue().getMinecraftVersions()){
            minecraftVersion.addItem(mcVer);
        }
        updateModVersions();
    }

    private void updateModVersions(){
        if (minecraftVersion.getSelectedItem() instanceof MinecraftVersion){
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            modVersion.removeAllItems();
            modVersion.setSelectedItem(null);
            if(mcVer != null){
                for(ModVersion modVer: mcVer.getModVersions()){
                    modVersion.addItem(modVer);
                }
            }
        }
    }

    private void downloadMod(TrayIcon trayIcon) throws MalformedURLException, FileNotFoundException {//Fixme: literally
        if(modList.getSelectedValue() != null && minecraftVersion.getSelectedItem() != null && modVersion.getSelectedItem() != null){
            Mod mod = modList.getSelectedValue();
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            ModVersion modVer = (ModVersion) modVersion.getSelectedItem();//Todo: also download dependencies and refresh library listmodel
            File fileName = new File(/*ConfigurationManager.getInstance().MOD_CACHE_DIR*/ ModUtils.getModsDirectory(), String.format("%s__%s__%s.jar", mod.getId(), mcVer.getMinecraftVersion(), modVer.getModVersion()));
            DownloadTask task = new DownloadTask(new URL(modVer.getModUrl()), new FileOutputStream(fileName), new DownloadListener() {
                public void onUpdate(int bytes, int totalDownloaded) {
                }

                public void onStart(String fileName, int size) {
                }

                public void onComplete() {
                    trayIcon.displayMessage(new ParsableTranslatableText("fmm.mod_browser.tray_icon.download_complete.caption",
                            mod.getName(), modVer.getModVersion(), mcVer.getMinecraftVersion()).toString(),
                            new TranslatableText("fmm.mod_browser.tray_icon.download_complete.text").toString(), TrayIcon.MessageType.INFO);
                }

                public void onCancel() {
                }
            });
            Application.DIRECT_DOWNLOADER.download(task);
        }
    }

    public void updateModList(RepositoryManager repositoryManager) {
        modList.removeAllItems();
        for(Mod mod: repositoryManager.getModList()){
            modList.addItem(mod);
        }
        modList.refresh();
    }
}
