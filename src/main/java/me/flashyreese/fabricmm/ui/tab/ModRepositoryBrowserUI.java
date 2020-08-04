package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.fabricmm.Application;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ModRepositoryBrowserUI extends JPanel {

    private JTextField searchBar;
    private ModList modList;
    private JComboBox<String> filterType;
    private JComboBox<MinecraftVersion> minecraftVersion;
    private JLabel minecraftVersionLabel;
    private JComboBox<ModVersion> modVersion;
    private JLabel modVersionLabel;
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
        filterType = new JComboBox<String>();
        minecraftVersion = new JComboBox<MinecraftVersion>();
        minecraftVersionLabel = new JLabel();
        modVersion = new JComboBox<ModVersion>();
        modVersionLabel = new JLabel();
        download = new JButton();
    }

    private void setupComponents() throws IOException {
        Dim2i searchBarDim = new Dim2i(10, 10, this.getWidth() / 8 * 3 - 20, 30);
        searchBar.setBounds(searchBarDim.getOriginX(), searchBarDim.getOriginY(), searchBarDim.getWidth(), searchBarDim.getHeight());
        searchBar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                modList.searchFilter(searchBar.getText(), (String)filterType.getSelectedItem());
            }
        });

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
        filterType.setBounds(searchTypeDim.getOriginX(), searchTypeDim.getOriginY(), searchTypeDim.getWidth(), searchTypeDim.getHeight());
        filterType.addItem("General");
        filterType.addItem("Name");
        filterType.addItem("Author");
        filterType.addItem("Minecraft Version");

        Font labelFont = new Font("Tahoma", Font.BOLD, 12);
        minecraftVersionLabel.setBounds(this.getWidth() / 2, this.getHeight() - 120, this.getWidth() / 4, 30);
        minecraftVersionLabel.setText("Minecraft Version");
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
        modVersionLabel.setText("Mod Version");
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
        this.add(filterType);
        this.add(minecraftVersion);
        this.add(minecraftVersionLabel);
        this.add(modVersion);
        this.add(modVersionLabel);
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

    private void downloadMod() throws MalformedURLException, FileNotFoundException {
        if(modList.getSelectedValue() != null && minecraftVersion.getSelectedItem() != null && modVersion.getSelectedItem() != null){
            Mod mod = modList.getSelectedValue();
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            ModVersion modVer = (ModVersion) modVersion.getSelectedItem();//Todo: also download dependencies and refresh library listmodel
            File fileName = new File(/*ConfigurationManager.getInstance().MOD_CACHE_DIR*/ ModUtils.getModsDirectory(), String.format("%s__%s__%s.jar", mod.getId(), mcVer.getMinecraftVersion(), modVer.getModVersion()));
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
                    //Todo: add something here xd
                }

                public void onCancel() {
                    System.out.println( fname + " cancelled" );
                }
            });
            Application.DIRECT_DOWNLOADER.download(task);
        }
    }

}
