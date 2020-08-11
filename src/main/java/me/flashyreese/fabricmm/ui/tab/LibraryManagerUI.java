package me.flashyreese.fabricmm.ui.tab;

import com.thebrokenrail.modupdater.strategy.util.UpdateStrategyRunner;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.schema.MinecraftInstance;
import me.flashyreese.fabricmm.ui.components.ModFileDropList;
import me.flashyreese.fabricmm.ui.components.ModPopClickListener;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.ModUtils;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;
import me.flashyreese.fabricmm.util.Util;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class LibraryManagerUI extends JPanel {

    private JLabel instanceLabel;
    private JComboBox<MinecraftInstance> instance;
    private ModFileDropList installedModFileDropList;
    private JButton toggleInstalledModState;
    private JButton checkForModUpdate;
    private JButton openModsFolder;
    private JButton refreshMods;
    private JButton modWebsite;
    private JButton modIssues;

    private JPanel modInfoPanel;
    private JLabel modNameLabel;
    private JLabel modMinecraftVersionLabel;
    private JLabel modVersionLabel;
    private JLabel modIdLabel;
    private JLabel modAuthorsLabel;
    private JLabel modEnvironmentLabel;
    private JLabel modName;
    private JLabel modMinecraftVersion;
    private JLabel modVersion;
    private JLabel modId;
    private JLabel modAuthors;
    private JLabel modEnvironment;

    public LibraryManagerUI(JTabbedPane jTabbedPane) throws Exception {
        setLayout(null);
        setLocation(0, 0);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        Util.findMinecraftInstances();
        initComponents();
        setupComponents();
        loadComponents();
        updateComponentsText();
    }

    private void initComponents(){
        instanceLabel = new JLabel();
        instance = new JComboBox<>();
        installedModFileDropList = new ModFileDropList();
        toggleInstalledModState = new JButton();
        checkForModUpdate = new JButton();
        openModsFolder = new JButton();
        refreshMods = new JButton();
        modWebsite = new JButton();
        modIssues = new JButton();

        modInfoPanel = new JPanel();
        modNameLabel = new JLabel();
        modMinecraftVersionLabel = new JLabel();
        modVersionLabel = new JLabel();
        modIdLabel = new JLabel();
        modAuthorsLabel = new JLabel();
        modEnvironmentLabel = new JLabel();
        modName = new JLabel();
        modMinecraftVersion = new JLabel();
        modVersion = new JLabel();
        modId = new JLabel();
        modAuthors = new JLabel();
        modEnvironment = new JLabel();
    }

    private void setupComponents() throws Exception {
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font modLabelFont = new Font("Arial", Font.PLAIN, 14);

        Dim2i instanceLabelDim = new Dim2i(10, 10, this.getWidth() / 6, 30);
        instanceLabel.setBounds(instanceLabelDim.getOriginX(), instanceLabelDim.getOriginY(), instanceLabelDim.getWidth(), instanceLabelDim.getHeight());
        instanceLabel.setFont(labelFont);

        Dim2i instanceDim = new Dim2i(this.getWidth() / 6 + 10, 10, this.getWidth() / 2 - this.getWidth() / 6, 30);
        instance.setBounds(instanceDim.getOriginX(), instanceDim.getOriginY(), instanceDim.getWidth(), instanceDim.getHeight());
        instance.setRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof MinecraftInstance) {
                    renderer.setText(((MinecraftInstance) value).getName());
                }
                return renderer;
            }
        });
        instance.addActionListener(e -> {
            onInstanceChange();
        });

        Dim2i installedModFileDropListDim = new Dim2i(10, 50, this.getWidth() / 2, this.getHeight() - 100);
        installedModFileDropList.setLocation(installedModFileDropListDim.getOriginX(), installedModFileDropListDim.getOriginY());
        installedModFileDropList.setSize(installedModFileDropListDim.getWidth(), installedModFileDropListDim.getHeight());

        installedModFileDropList.getList().addListSelectionListener(arg0 -> onModFileDropListSelect());
        installedModFileDropList.getList().addMouseListener(new ModPopClickListener(installedModFileDropList));

        Dim2i openModsFolderDim = new Dim2i(10, this.getHeight() - 40, this.getWidth() / 2 / 2, 30);
        openModsFolder.setBounds(openModsFolderDim.getOriginX(), openModsFolderDim.getOriginY(), openModsFolderDim.getWidth(), openModsFolderDim.getHeight());
        openModsFolder.addActionListener(e -> {
            try {
                if (instance.getSelectedItem() instanceof MinecraftInstance){
                    MinecraftInstance minecraftInstance = (MinecraftInstance) instance.getSelectedItem();
                    Desktop.getDesktop().open(minecraftInstance.getDirectory());
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i refreshModsDim = new Dim2i(this.getWidth() / 2 / 2 + 10, this.getHeight() - 40, this.getWidth() / 2 / 2, 30);
        refreshMods.setBounds(refreshModsDim.getOriginX(), refreshModsDim.getOriginY(), refreshModsDim.getWidth(), refreshModsDim.getHeight());
        refreshMods.addActionListener(e -> {
            try {
                installedModFileDropList.reloadMods();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            installedModFileDropList.updateUI();
        });

        Dim2i toggleInstalledModStateDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 40, this.getWidth() / 2 - 30, 30);
        toggleInstalledModState.setBounds(toggleInstalledModStateDim.getOriginX(), toggleInstalledModStateDim.getOriginY(), toggleInstalledModStateDim.getWidth(), toggleInstalledModStateDim.getHeight());
        toggleInstalledModState.addActionListener(e -> {
            if(installedModFileDropList.getSelectedValue() != null){
                ModUtils.changeInstalledModState(installedModFileDropList.getSelectedValue());
                this.installedModFileDropList.updateUI();
            }
        });

        Dim2i checkForModUpdateDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 70, this.getWidth() / 2 - 30, 30);
        checkForModUpdate.setBounds(checkForModUpdateDim.getOriginX(), checkForModUpdateDim.getOriginY(), checkForModUpdateDim.getWidth(), checkForModUpdateDim.getHeight());
        checkForModUpdate.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "This does nothing at the moment");
            try{
                System.out.println(Objects.requireNonNull(UpdateStrategyRunner.checkModForUpdate(installedModFileDropList.getSelectedValue().getModMetadata())).downloadURL);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        Dim2i modWebsiteDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modWebsite.setBounds(modWebsiteDim.getOriginX(), modWebsiteDim.getOriginY(), modWebsiteDim.getWidth(), modWebsiteDim.getHeight());
        modWebsite.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI((String) installedModFileDropList.getSelectedValue().getModMetadata().getContact().get("homepage")));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i modIssuesDim = new Dim2i(this.getWidth() / 4 * 3 + 7, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modIssues.setBounds(modIssuesDim.getOriginX(), modIssuesDim.getOriginY(), modIssuesDim.getWidth(), modIssuesDim.getHeight());
        modIssues.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI((String) installedModFileDropList.getSelectedValue().getModMetadata().getContact().get("sources")));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i modInfoPanelDim = new Dim2i(this.getWidth() / 2 + 20, 10, this.getWidth() / 2 - 30, this.getHeight() - 120);
        modInfoPanel.setBounds(modInfoPanelDim.getOriginX(), modInfoPanelDim.getOriginY(), modInfoPanelDim.getWidth(), modInfoPanelDim.getHeight());
        modInfoPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        modInfoPanel.setLayout(null);
        int labelWidth = modInfoPanel.getWidth() - 20;
        int labelFontHeight = labelFont.getSize() + 2;
        int modLabelFontHeight = modLabelFont.getSize() + 2;
        int baseHeightLabel = 10;
        int offsetHeightLabel = 40;
        int modBaseHeightLabel = 28;
        int modOffsetHeightLabel = 40;
        modNameLabel.setBounds(10, baseHeightLabel, labelWidth, labelFontHeight);
        modNameLabel.setFont(labelFont);
        modMinecraftVersionLabel.setBounds(10, baseHeightLabel + offsetHeightLabel, labelWidth, labelFontHeight);
        modMinecraftVersionLabel.setFont(labelFont);
        modVersionLabel.setBounds(10, baseHeightLabel + offsetHeightLabel * 2, labelWidth, labelFontHeight);
        modVersionLabel.setFont(labelFont);
        modIdLabel.setBounds(10, baseHeightLabel + offsetHeightLabel * 3, labelWidth, labelFontHeight);
        modIdLabel.setFont(labelFont);
        modAuthorsLabel.setBounds(10, baseHeightLabel + offsetHeightLabel * 4, labelWidth, labelFontHeight);
        modAuthorsLabel.setFont(labelFont);
        modEnvironmentLabel.setBounds(10, baseHeightLabel + offsetHeightLabel * 5, labelWidth, labelFontHeight);
        modEnvironmentLabel.setFont(labelFont);
        modName.setBounds(15, modBaseHeightLabel, labelWidth, modLabelFontHeight);
        modName.setFont(modLabelFont);
        modMinecraftVersion.setBounds(15, modBaseHeightLabel + modOffsetHeightLabel, labelWidth, modLabelFontHeight);
        modMinecraftVersion.setFont(modLabelFont);
        modVersion.setBounds(15, modBaseHeightLabel + modOffsetHeightLabel * 2, labelWidth, modLabelFontHeight);
        modVersion.setFont(modLabelFont);
        modId.setBounds(15, modBaseHeightLabel + modOffsetHeightLabel * 3, labelWidth, modLabelFontHeight);
        modId.setFont(modLabelFont);
        modAuthors.setBounds(15, modBaseHeightLabel + modOffsetHeightLabel * 4, labelWidth, modLabelFontHeight);
        modAuthors.setFont(modLabelFont);
        modEnvironment.setBounds(15, modBaseHeightLabel + modOffsetHeightLabel * 5, labelWidth, modLabelFontHeight);
        modEnvironment.setFont(modLabelFont);
    }

    private void loadComponents(){
        modInfoPanel.add(modNameLabel);
        modInfoPanel.add(modName);
        modInfoPanel.add(modMinecraftVersionLabel);
        modInfoPanel.add(modMinecraftVersion);
        modInfoPanel.add(modVersionLabel);
        modInfoPanel.add(modVersion);
        modInfoPanel.add(modIdLabel);
        modInfoPanel.add(modId);
        modInfoPanel.add(modAuthorsLabel);
        modInfoPanel.add(modAuthors);
        modInfoPanel.add(modEnvironmentLabel);
        modInfoPanel.add(modEnvironment);

        this.add(modInfoPanel);
        this.add(instanceLabel);
        this.add(instance);
        this.add(installedModFileDropList);
        this.add(toggleInstalledModState);
        this.add(checkForModUpdate);
        this.add(openModsFolder);
        this.add(refreshMods);
        this.add(modWebsite);
        this.add(modIssues);
        installedModFileDropList.updateUI();
    }

    public void updateComponentsText(){
        instanceLabel.setText(new I18nText("fmm.library.instance").toString());
        openModsFolder.setText(new I18nText("fmm.library.open_mods_folder").toString());
        refreshMods.setText(new I18nText("fmm.library.refresh_mods").toString());
        checkForModUpdate.setText(new I18nText("fmm.library.check_for_update").toString());
        modWebsite.setText(new I18nText("fmm.library.website").toString());
        modIssues.setText(new I18nText("fmm.library.issues").toString());
        modEnvironmentLabel.setText(new I18nText("fmm.library.mod_info.environment").toString());
        modNameLabel.setText(new I18nText("fmm.library.mod_info.name").toString());
        modMinecraftVersionLabel.setText(new I18nText("fmm.library.mod_info.minecraft_version").toString());
        modVersionLabel.setText(new I18nText("fmm.library.mod_info.version").toString());
        modIdLabel.setText(new I18nText("fmm.library.mod_info.id").toString());
        modAuthorsLabel.setText(new I18nText("fmm.library.mod_info.authors").toString());
        updateInstances();
        onModFileDropListSelect();
        onInstanceChange();
    }

    public void updateInstances(){
        instance.removeAllItems();
        for(MinecraftInstance minecraftInstance: Util.getMinecraftInstances()){
            instance.addItem(minecraftInstance);
        }
    }

    private void onInstanceChange(){
        if (instance.getSelectedItem() instanceof MinecraftInstance){
            MinecraftInstance minecraftInstance = (MinecraftInstance) instance.getSelectedItem();
            installedModFileDropList.setDirectory(minecraftInstance.getDirectory());
            try {
                installedModFileDropList.reloadMods();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            installedModFileDropList.updateUI();
        }
    }

    private void onModFileDropListSelect(){
        if(installedModFileDropList.getSelectedValue() != null){
            InstalledMod selectedMod = installedModFileDropList.getSelectedValue();
            this.toggleInstalledModState.setText(installedModFileDropList.getSelectedValue().isEnabled() ? new I18nText("fmm.library.disable").toString() : new I18nText("fmm.library.enable").toString());
            this.toggleInstalledModState.setEnabled(true);
            this.checkForModUpdate.setEnabled(true);
            this.modWebsite.setEnabled(selectedMod.getModMetadata().getContact() != null && selectedMod.getModMetadata().getContact().containsKey("homepage"));
            this.modIssues.setEnabled(selectedMod.getModMetadata().getContact() != null && selectedMod.getModMetadata().getContact().containsKey("sources"));
            this.modName.setText(selectedMod.getModMetadata().getName());
            selectedMod.assignMinecraftVersion();
            this.modMinecraftVersion.setText(selectedMod.getMinecraftVersion());
            this.modVersion.setText(selectedMod.getModMetadata().getVersion());
            this.modId.setText(selectedMod.getModMetadata().getId());
            this.modAuthors.setText(UserInterfaceUtils.getEnglishStringList((ArrayList<Object>) selectedMod.getModMetadata().getAuthors()));
            this.modEnvironment.setText(UserInterfaceUtils.filterEnvironment(selectedMod.getModMetadata().getEnvironment()));
        }else{
            this.toggleInstalledModState.setText(new I18nText("fmm.library.enable").toString());
            this.toggleInstalledModState.setEnabled(false);
            this.checkForModUpdate.setEnabled(false);
            this.modWebsite.setEnabled(false);
            this.modIssues.setEnabled(false);
            this.modName.setText(new I18nText("fmm.library.none_selected").toString());
            this.modMinecraftVersion.setText(new I18nText("fmm.library.none_selected").toString());
            this.modVersion.setText(new I18nText("fmm.library.none_selected").toString());
            this.modId.setText(new I18nText("fmm.library.none_selected").toString());
            this.modAuthors.setText(new I18nText("fmm.library.none_selected").toString());
            this.modEnvironment.setText(new I18nText("fmm.library.none_selected").toString());
        }
    }

}
