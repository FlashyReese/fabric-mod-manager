package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.TranslatableText;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.components.InstalledModFileDropList;
import me.flashyreese.fabricmm.ui.components.InstalledModPopClickListener;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.ModUtils;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LibraryManagerUI extends JPanel {

    private InstalledModFileDropList installedModFileDropList;
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
        initComponents();
        setupComponents();
        loadComponents();
        updateComponentsText();
    }

    private void initComponents(){
        installedModFileDropList = new InstalledModFileDropList();
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
        Dim2i modFileDropListDim = new Dim2i(10, 10, this.getWidth() / 2, this.getHeight() - 60);
        installedModFileDropList.setLocation(modFileDropListDim.getOriginX(), modFileDropListDim.getOriginY());
        installedModFileDropList.setSize(modFileDropListDim.getWidth(), modFileDropListDim.getHeight());
        for(InstalledMod installedMod: ModUtils.getInstalledModsFromDir(ModUtils.getModsDirectory())){
            installedModFileDropList.addItem(installedMod);
        }
        installedModFileDropList.getList().addListSelectionListener(arg0 -> {
            onModFileDropListSelect();
        });
        installedModFileDropList.getList().addMouseListener(new InstalledModPopClickListener(installedModFileDropList));

        Dim2i openModsFolderDim = new Dim2i(10, this.getHeight() - 40, this.getWidth() / 2 / 2, 30);
        openModsFolder.setBounds(openModsFolderDim.getOriginX(), openModsFolderDim.getOriginY(), openModsFolderDim.getWidth(), openModsFolderDim.getHeight());
        openModsFolder.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(ModUtils.getModsDirectory());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i refreshModsDim = new Dim2i(this.getWidth() / 2 / 2 + 10, this.getHeight() - 40, this.getWidth() / 2 / 2, 30);
        refreshMods.setBounds(refreshModsDim.getOriginX(), refreshModsDim.getOriginY(), refreshModsDim.getWidth(), refreshModsDim.getHeight());
        refreshMods.addActionListener(e -> {
            installedModFileDropList.removeAllItems();
            try {
                for(InstalledMod installedMod: ModUtils.getInstalledModsFromDir(ModUtils.getModsDirectory())){
                    installedModFileDropList.addItem(installedMod);
                }
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
        });

        Dim2i modWebsiteDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modWebsite.setBounds(modWebsiteDim.getOriginX(), modWebsiteDim.getOriginY(), modWebsiteDim.getWidth(), modWebsiteDim.getHeight());
        modWebsite.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(installedModFileDropList.getSelectedValue().getContact().get("homepage")));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i modIssuesDim = new Dim2i(this.getWidth() / 4 * 3 + 5, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modIssues.setBounds(modIssuesDim.getOriginX(), modIssuesDim.getOriginY(), modIssuesDim.getWidth(), modIssuesDim.getHeight());
        modIssues.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(installedModFileDropList.getSelectedValue().getContact().get("sources")));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i modInfoPanelDim = new Dim2i(this.getWidth() / 2 + 20, 10, this.getWidth() / 2 - 30, this.getHeight() - 120);
        modInfoPanel.setBounds(modInfoPanelDim.getOriginX(), modInfoPanelDim.getOriginY(), modInfoPanelDim.getWidth(), modInfoPanelDim.getHeight());
        modInfoPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        modInfoPanel.setLayout(null);
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font modLabelFont = new Font("Arial", Font.PLAIN, 14);
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
        openModsFolder.setText(new TranslatableText("fmm.library.open_mods_folder").toString());
        refreshMods.setText(new TranslatableText("fmm.library.refresh_mods").toString());
        checkForModUpdate.setText(new TranslatableText("fmm.library.check_for_update").toString());
        modWebsite.setText(new TranslatableText("fmm.library.website").toString());
        modIssues.setText(new TranslatableText("fmm.library.issues").toString());
        modEnvironmentLabel.setText(new TranslatableText("fmm.library.mod_info.environment").toString());
        modNameLabel.setText(new TranslatableText("fmm.library.mod_info.name").toString());
        modMinecraftVersionLabel.setText(new TranslatableText("fmm.library.mod_info.minecraft_version").toString());
        modVersionLabel.setText(new TranslatableText("fmm.library.mod_info.version").toString());
        modIdLabel.setText(new TranslatableText("fmm.library.mod_info.id").toString());
        modAuthorsLabel.setText(new TranslatableText("fmm.library.mod_info.authors").toString());
        onModFileDropListSelect();
    }

    private void onModFileDropListSelect(){
        if(installedModFileDropList.getSelectedValue() != null){
            this.toggleInstalledModState.setText(installedModFileDropList.getSelectedValue().isEnabled() ? new TranslatableText("fmm.library.disable").toString() : new TranslatableText("fmm.library.enable").toString());
            this.toggleInstalledModState.setEnabled(true);
            this.checkForModUpdate.setEnabled(true);
            this.modWebsite.setEnabled(installedModFileDropList.getSelectedValue().getContact().containsKey("homepage"));
            this.modIssues.setEnabled(installedModFileDropList.getSelectedValue().getContact().containsKey("sources"));
            InstalledMod selectedMod = installedModFileDropList.getSelectedValue();
            this.modName.setText(selectedMod.getName());
            selectedMod.assignMinecraftVersion();
            this.modMinecraftVersion.setText(selectedMod.getMinecraftVersion());
            this.modVersion.setText(selectedMod.getVersion());
            this.modId.setText(selectedMod.getId());
            this.modAuthors.setText(UserInterfaceUtils.getEnglishStringList(selectedMod.getAuthors()));
            this.modEnvironment.setText(UserInterfaceUtils.filterEnvironment(selectedMod.getEnvironment()));
        }else{
            this.toggleInstalledModState.setText(new TranslatableText("fmm.library.enable").toString());
            this.toggleInstalledModState.setEnabled(false);
            this.checkForModUpdate.setEnabled(false);
            this.modWebsite.setEnabled(false);
            this.modIssues.setEnabled(false);
            this.modName.setText(new TranslatableText("fmm.library.none_selected").toString());
            this.modMinecraftVersion.setText(new TranslatableText("fmm.library.none_selected").toString());
            this.modVersion.setText(new TranslatableText("fmm.library.none_selected").toString());
            this.modId.setText(new TranslatableText("fmm.library.none_selected").toString());
            this.modAuthors.setText(new TranslatableText("fmm.library.none_selected").toString());
            this.modEnvironment.setText(new TranslatableText("fmm.library.none_selected").toString());
        }
    }

}
