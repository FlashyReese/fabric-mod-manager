package me.flashyreese.fabricmm.ui;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.components.swing.ModFileDropList;
import me.flashyreese.fabricmm.utils.Dim2i;
import me.flashyreese.fabricmm.utils.ModUtils;
import me.flashyreese.fabricmm.utils.UserInterfaceUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FabricModManagerUI extends JFrame {

    private ModFileDropList modFileDropList;
    private JButton toggleInstalledModState;
    private JButton checkForModUpdate;
    private JButton openModsFolder;
    private JButton refreshMods;

    private JPanel modInfoPanel;
    private JLabel modNameLabel;
    private JLabel modVersionLabel;
    private JLabel modIdLabel;
    private JLabel modAuthorsLabel;
    //private JLabel modContactLabel;
    private JLabel modEnvironmentLabel;
    //private JLabel modMinecraftVersionLabel;
    private JLabel modName;
    private JLabel modVersion;
    private JLabel modId;
    private JLabel modAuthors;
    //private JLabel modContact;
    private JLabel modEnvironment;
    //private JLabel modMinecraftVersion;

    public FabricModManagerUI() throws Exception {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fabric Mod Manager");
        setLayout(null);
        setResizable(false);
        //this.setSize(884, 490);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png")));
        getContentPane().setPreferredSize(new Dimension(600, 400));
        pack();
        initComponents();
        setupComponents();
        loadComponents();
        onModFileDropListSelect();
    }

    private void initComponents(){
        modFileDropList = new ModFileDropList();
        toggleInstalledModState = new JButton();
        checkForModUpdate = new JButton();
        openModsFolder = new JButton();
        refreshMods = new JButton();

        modInfoPanel = new JPanel();
        modNameLabel = new JLabel();
        modVersionLabel = new JLabel();
        modIdLabel = new JLabel();
        modAuthorsLabel = new JLabel();
        //modContactLabel = new JLabel();
        modEnvironmentLabel = new JLabel();
        //modMinecraftVersionLabel = new JLabel();
        modName = new JLabel();
        modVersion = new JLabel();
        modId = new JLabel();
        modAuthors = new JLabel();
        //modContact = new JLabel();
        modEnvironment = new JLabel();
        //modMinecraftVersion = new JLabel();
    }

    private void setupComponents() throws Exception {
        Dim2i modFileDropListDim = new Dim2i(10, 10, this.getContentPane().getWidth() / 2, this.getContentPane().getHeight() - 60);
        modFileDropList.setLocation(modFileDropListDim.getOriginX(), modFileDropListDim.getOriginY());
        modFileDropList.setSize(modFileDropListDim.getWidth(), modFileDropListDim.getHeight());
        for(InstalledMod installedMod: ModUtils.getInstalledModsFromDir(ModUtils.getModsDirectory())){
            modFileDropList.addItem(installedMod);
        }
        modFileDropList.getList().addListSelectionListener(arg0 -> {
            onModFileDropListSelect();
        });

        Dim2i openModsFolderDim = new Dim2i(10, this.getContentPane().getHeight() - 40, this.getContentPane().getWidth() / 2 / 2, 30);
        openModsFolder.setBounds(openModsFolderDim.getOriginX(), openModsFolderDim.getOriginY(), openModsFolderDim.getWidth(), openModsFolderDim.getHeight());
        openModsFolder.setText("Open Mods Folder");
        openModsFolder.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(ModUtils.getModsDirectory());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i refreshModsDim = new Dim2i(this.getContentPane().getWidth() / 2 / 2 + 10, this.getContentPane().getHeight() - 40, this.getContentPane().getWidth() / 2 / 2, 30);
        refreshMods.setBounds(refreshModsDim.getOriginX(), refreshModsDim.getOriginY(), refreshModsDim.getWidth(), refreshModsDim.getHeight());
        refreshMods.setText("Refresh Mods");
        refreshMods.addActionListener(e -> {
            modFileDropList.removeAllItems();
            try {
                for(InstalledMod installedMod: ModUtils.getInstalledModsFromDir(ModUtils.getModsDirectory())){
                    modFileDropList.addItem(installedMod);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Dim2i toggleInstalledModStateDim = new Dim2i(this.getContentPane().getWidth() / 2 + 20, this.getContentPane().getHeight() - 40, this.getContentPane().getWidth() / 2 - 30, 30);
        toggleInstalledModState.setBounds(toggleInstalledModStateDim.getOriginX(), toggleInstalledModStateDim.getOriginY(), toggleInstalledModStateDim.getWidth(), toggleInstalledModStateDim.getHeight());
        toggleInstalledModState.addActionListener(e -> {
            if(modFileDropList.getSelectedValue() != null){
                ModUtils.changeInstalledModState(modFileDropList.getSelectedValue());
                this.toggleInstalledModState.setText(modFileDropList.getSelectedValue().isEnabled() ? "Disable" : "Enable");
                this.modFileDropList.refresh();
            }
        });

        Dim2i checkForModUpdateDim = new Dim2i(this.getContentPane().getWidth() / 2 + 20, this.getContentPane().getHeight() - 70, this.getContentPane().getWidth() / 2 - 30, 30);
        checkForModUpdate.setBounds(checkForModUpdateDim.getOriginX(), checkForModUpdateDim.getOriginY(), checkForModUpdateDim.getWidth(), checkForModUpdateDim.getHeight());
        checkForModUpdate.setText("Check for update");

        Dim2i modInfoPanelDim = new Dim2i(this.getContentPane().getWidth() / 2 + 20, 10, this.getContentPane().getWidth() / 2 - 30, this.getContentPane().getHeight() - 90);
        modInfoPanel.setBounds(modInfoPanelDim.getOriginX(), modInfoPanelDim.getOriginY(), modInfoPanelDim.getWidth(), modInfoPanelDim.getHeight());
        modInfoPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        modInfoPanel.setLayout(null);
        Font labelFont = new Font("Tahoma", Font.BOLD, 12);
        Font modLabelFont = new Font("Tahoma", Font.PLAIN, 12);
        int labelWidth = modInfoPanel.getWidth() - 20;
        modNameLabel.setBounds(10, 10, labelWidth, 12);
        modNameLabel.setFont(labelFont);
        modNameLabel.setText("Name:");
        modVersionLabel.setBounds(10, 40, labelWidth, 12);
        modVersionLabel.setFont(labelFont);
        modVersionLabel.setText("Version:");
        modIdLabel.setBounds(10, 70, labelWidth, 12);
        modIdLabel.setFont(labelFont);
        modIdLabel.setText("ID:");
        modAuthorsLabel.setBounds(10, 100, labelWidth, 12);
        modAuthorsLabel.setFont(labelFont);
        modAuthorsLabel.setText("Authors:");
        /*modContactLabel.setBounds(10, 130, labelWidth, 12);
        modContactLabel.setFont(labelFont);
        modContactLabel.setText("Contact:");*/
        modEnvironmentLabel.setBounds(10, 130, labelWidth, 12);
        modEnvironmentLabel.setFont(labelFont);
        modEnvironmentLabel.setText("Environment:");
        /*modMinecraftVersionLabel.setBounds(10, 190, labelWidth, 12);
        modMinecraftVersionLabel.setFont(labelFont);
        modMinecraftVersionLabel.setText("Minecraft Version:");*/
        modName.setBounds(15, 25, labelWidth, 12);
        modName.setFont(modLabelFont);
        modVersion.setBounds(15, 55, labelWidth, 12);
        modVersion.setFont(modLabelFont);
        modId.setBounds(15, 85, labelWidth, 12);
        modId.setFont(modLabelFont);
        modAuthors.setBounds(15, 115, labelWidth, 12);
        modAuthors.setFont(modLabelFont);
        /*modContact.setBounds(15, 145, labelWidth, 12);
        modContact.setFont(modLabelFont);*/
        modEnvironment.setBounds(15, 145, labelWidth, 12);
        modEnvironment.setFont(modLabelFont);
        /*modMinecraftVersion.setBounds(15, 205, labelWidth, 12);
        modMinecraftVersion.setFont(modLabelFont);*/
    }

    private void loadComponents(){
        modInfoPanel.add(modNameLabel);
        modInfoPanel.add(modVersionLabel);
        modInfoPanel.add(modIdLabel);
        modInfoPanel.add(modAuthorsLabel);
        //modInfoPanel.add(modContactLabel);
        modInfoPanel.add(modEnvironmentLabel);
        //modInfoPanel.add(modMinecraftVersionLabel);
        modInfoPanel.add(modName);
        modInfoPanel.add(modVersion);
        modInfoPanel.add(modId);
        modInfoPanel.add(modAuthors);
        //modInfoPanel.add(modContact);
        modInfoPanel.add(modEnvironment);
        //modInfoPanel.add(modMinecraftVersion);

        this.add(modInfoPanel);
        this.add(modFileDropList);
        this.add(toggleInstalledModState);
        this.add(checkForModUpdate);
        this.add(openModsFolder);
        this.add(refreshMods);

        modFileDropList.refresh();
    }

    private void onModFileDropListSelect(){
        if(modFileDropList.getSelectedValue() != null){
            this.toggleInstalledModState.setText(modFileDropList.getSelectedValue().isEnabled() ? "Disable" : "Enable");
            this.toggleInstalledModState.setEnabled(true);
            this.checkForModUpdate.setEnabled(true);
            InstalledMod selectedMod = modFileDropList.getSelectedValue();
            this.modName.setText(selectedMod.getName());
            this.modVersion.setText(selectedMod.getVersion());
            this.modId.setText(selectedMod.getId());
            this.modAuthors.setText(UserInterfaceUtils.getEnglishStringList(selectedMod.getAuthors()));
            //this.modContact.setText("WIP");
            this.modEnvironment.setText(UserInterfaceUtils.filterEnvironment(selectedMod.getEnvironment()));
            //this.modMinecraftVersion.setText(selectedMod.getMinecraftVersion());
        }else{
            this.toggleInstalledModState.setText("Enable");
            this.toggleInstalledModState.setEnabled(false);
            this.checkForModUpdate.setEnabled(false);
            this.modName.setText("None selected!");
            this.modVersion.setText("None selected!");
            this.modId.setText("None selected!");
            this.modAuthors.setText("None selected!");
            //this.modContact.setText("None selected!");
            this.modEnvironment.setText("None selected!");
            //this.modMinecraftVersion.setText("None selected!");
        }
    }

}
