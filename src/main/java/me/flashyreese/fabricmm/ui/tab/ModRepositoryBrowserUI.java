package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.schema.repository.MinecraftVersion;
import me.flashyreese.fabricmm.schema.repository.Mod;
import me.flashyreese.fabricmm.schema.repository.ModVersion;
import me.flashyreese.fabricmm.ui.components.ModList;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.ModUtils;
import org.kamranzafar.jddl.DownloadListener;
import org.kamranzafar.jddl.DownloadTask;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ModRepositoryBrowserUI extends JPanel {

    private JTextField searchBar;
    private ModList modList;
    private JComboBox<String> searchType;
    private JComboBox<MinecraftVersion> minecraftVersion;
    private JComboBox<ModVersion> modVersion;
    private JButton download;

    public ModRepositoryBrowserUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents();
        loadComponents();
    }

    private void initComponents() {
        searchBar = new JTextField();
        modList = new ModList();
        searchType = new JComboBox<String>();
        minecraftVersion = new JComboBox<MinecraftVersion>();
        modVersion = new JComboBox<ModVersion>();
        download = new JButton();
    }

    private void setupComponents() throws FileNotFoundException {
        Dim2i searchBarDim = new Dim2i(10, 10, this.getWidth() / 8 * 3 - 20, 30);
        searchBar.setBounds(searchBarDim.getOriginX(), searchBarDim.getOriginY(), searchBarDim.getWidth(), searchBarDim.getHeight());

        Dim2i modFileListDim = new Dim2i(10, 50, this.getWidth() / 2 - 20, this.getHeight() - 60);
        modList.setLocation(modFileListDim.getOriginX(), modFileListDim.getOriginY());
        modList.setSize(modFileListDim.getWidth(), modFileListDim.getHeight());
        for(Mod mod: ModUtils.getModList()){//Fixme: Disaster
            modList.addItem(mod);
        }
        modList.getList().addListSelectionListener(arg0 -> {
            onModFileDropListSelect();
        });

        Dim2i searchTypeDim = new Dim2i(this.getWidth() / 8 * 3, 10, this.getWidth() / 8 - 10, 30);
        searchType.setBounds(searchTypeDim.getOriginX(), searchTypeDim.getOriginY(), searchTypeDim.getWidth(), searchTypeDim.getHeight());

        Dim2i minecraftVersionDim = new Dim2i(this.getWidth() / 2, 10, this.getWidth() / 6, 30);
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

        Dim2i modVersionDim = new Dim2i(this.getWidth() / 6 * 4 + 10, 10, this.getWidth() / 6, 30);
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

        Dim2i downloadDim = new Dim2i(this.getWidth() / 6 * 5 + 20, 10, this.getWidth() / 6 - 30, 30);
        download.setBounds(downloadDim.getOriginX(), downloadDim.getOriginY(), downloadDim.getWidth(), downloadDim.getHeight());
        download.setText("Download");
        download.addActionListener(e -> {
            try {
                downloadMod();
            } catch (MalformedURLException | FileNotFoundException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        });
    }

    private void loadComponents() {
        this.add(searchBar);
        this.add(modList);
        this.add(searchType);
        this.add(minecraftVersion);
        this.add(modVersion);
        this.add(download);
        onModFileDropListSelect();
    }

    private void onModFileDropListSelect(){
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
        minecraftVersion.setSelectedItem(null);
        minecraftVersion.removeAllItems();
        for(MinecraftVersion mcVer: modList.getSelectedValue().getMinecraftVersions()){
            minecraftVersion.addItem(mcVer);
        }
        minecraftVersion.setSelectedIndex(0);
        updateModVersions();
    }

    private void updateModVersions(){
        MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
        if(mcVer != null){
            modVersion.setSelectedItem(null);
            modVersion.removeAllItems();
            for(ModVersion modVer: mcVer.getModVersions()){
                modVersion.addItem(modVer);
            }
        }
    }

    private void downloadMod() throws MalformedURLException, FileNotFoundException {
        if(modList.getSelectedValue() != null && minecraftVersion.getSelectedItem() != null && modVersion.getSelectedItem() != null){
            Mod mod = (Mod) modList.getSelectedValue();
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            ModVersion modVer = (ModVersion) modVersion.getSelectedItem();
            File fileName = new File(/*ConfigurationManager.getInstance().MOD_CACHE_DIR*/ ModUtils.getModsDirectory(), String.format("%s-%s-%s.jar", mod.getId(), mcVer.getMinecraftVersion(), modVer.getModVersion()));
            DownloadTask task = new DownloadTask(new URL(modVer.getModUrl()), new FileOutputStream(fileName), new DownloadListener() {
                String fname;

                public void onUpdate(int bytes, int totalDownloaded) {
                }

                public void onStart(String fname, int size) {
                    this.fname = fname;
                    System.out.println( "Downloading " + fname + " of size " + size );
                }

                public void onComplete() {
                    System.out.println( fname + " downloaded" );
                }

                public void onCancel() {
                    System.out.println( fname + " cancelled" );
                }
            });
            Application.DIRECT_DOWNLOADER.download(task);
        }
    }

}
