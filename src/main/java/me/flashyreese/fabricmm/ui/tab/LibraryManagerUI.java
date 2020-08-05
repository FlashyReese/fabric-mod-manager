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
        onModFileDropListSelect();
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
        openModsFolder.setText(new TranslatableText("fmm.library.open_mods_folder").toString());
        openModsFolder.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(ModUtils.getModsDirectory());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i refreshModsDim = new Dim2i(this.getWidth() / 2 / 2 + 10, this.getHeight() - 40, this.getWidth() / 2 / 2, 30);
        refreshMods.setBounds(refreshModsDim.getOriginX(), refreshModsDim.getOriginY(), refreshModsDim.getWidth(), refreshModsDim.getHeight());
        refreshMods.setText(new TranslatableText("fmm.library.refresh_mods").toString());
        refreshMods.addActionListener(e -> {
            installedModFileDropList.removeAllItems();
            try {
                for(InstalledMod installedMod: ModUtils.getInstalledModsFromDir(ModUtils.getModsDirectory())){
                    installedModFileDropList.addItem(installedMod);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            installedModFileDropList.refresh();
        });

        Dim2i toggleInstalledModStateDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 40, this.getWidth() / 2 - 30, 30);
        toggleInstalledModState.setBounds(toggleInstalledModStateDim.getOriginX(), toggleInstalledModStateDim.getOriginY(), toggleInstalledModStateDim.getWidth(), toggleInstalledModStateDim.getHeight());
        toggleInstalledModState.addActionListener(e -> {
            if(installedModFileDropList.getSelectedValue() != null){
                ModUtils.changeInstalledModState(installedModFileDropList.getSelectedValue());
                this.installedModFileDropList.refresh();
            }
        });

        Dim2i checkForModUpdateDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 70, this.getWidth() / 2 - 30, 30);
        checkForModUpdate.setBounds(checkForModUpdateDim.getOriginX(), checkForModUpdateDim.getOriginY(), checkForModUpdateDim.getWidth(), checkForModUpdateDim.getHeight());
        checkForModUpdate.setText(new TranslatableText("fmm.library.check_for_update").toString());
        checkForModUpdate.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "This does nothing at the moment");
        });

        Dim2i modWebsiteDim = new Dim2i(this.getWidth() / 2 + 20, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modWebsite.setBounds(modWebsiteDim.getOriginX(), modWebsiteDim.getOriginY(), modWebsiteDim.getWidth(), modWebsiteDim.getHeight());
        modWebsite.setText(new TranslatableText("fmm.library.website").toString());
        modWebsite.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(installedModFileDropList.getSelectedValue().getContact().get("homepage")));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i modIssuesDim = new Dim2i(this.getWidth() / 4 * 3 + 5, this.getHeight() - 100, (this.getWidth() / 2 - 30) / 2, 30);
        modIssues.setBounds(modIssuesDim.getOriginX(), modIssuesDim.getOriginY(), modIssuesDim.getWidth(), modIssuesDim.getHeight());
        modIssues.setText(new TranslatableText("fmm.library.issues").toString());
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
        Font labelFont = new Font("Tahoma", Font.BOLD, 12);
        Font modLabelFont = new Font("Tahoma", Font.PLAIN, 12);
        int labelWidth = modInfoPanel.getWidth() - 20;
        modNameLabel.setBounds(10, 10, labelWidth, 12);
        modNameLabel.setFont(labelFont);
        modNameLabel.setText(new TranslatableText("fmm.library.mod_info.name").toString());
        modMinecraftVersionLabel.setBounds(10, 40, labelWidth, 12);
        modMinecraftVersionLabel.setFont(labelFont);
        modMinecraftVersionLabel.setText(new TranslatableText("fmm.library.mod_info.minecraft_version").toString());
        modVersionLabel.setBounds(10, 70, labelWidth, 12);
        modVersionLabel.setFont(labelFont);
        modVersionLabel.setText(new TranslatableText("fmm.library.mod_info.version").toString());
        modIdLabel.setBounds(10, 100, labelWidth, 12);
        modIdLabel.setFont(labelFont);
        modIdLabel.setText(new TranslatableText("fmm.library.mod_info.id").toString());
        modAuthorsLabel.setBounds(10, 130, labelWidth, 12);
        modAuthorsLabel.setFont(labelFont);
        modAuthorsLabel.setText(new TranslatableText("fmm.library.mod_info.authors").toString());
        modEnvironmentLabel.setBounds(10, 160, labelWidth, 12);
        modEnvironmentLabel.setFont(labelFont);
        modEnvironmentLabel.setText(new TranslatableText("fmm.library.mod_info.environment").toString());
        modName.setBounds(15, 25, labelWidth, 12);
        modName.setFont(modLabelFont);
        modMinecraftVersion.setBounds(15, 55, labelWidth, 12);
        modMinecraftVersion.setFont(modLabelFont);
        modVersion.setBounds(15, 85, labelWidth, 12);
        modVersion.setFont(modLabelFont);
        modId.setBounds(15, 115, labelWidth, 12);
        modId.setFont(modLabelFont);
        modAuthors.setBounds(15, 145, labelWidth, 12);
        modAuthors.setFont(modLabelFont);
        modEnvironment.setBounds(15, 175, labelWidth, 12);
        modEnvironment.setFont(modLabelFont);
    }

    private void loadComponents(){
        modInfoPanel.add(modNameLabel);
        modInfoPanel.add(modMinecraftVersionLabel);
        modInfoPanel.add(modVersionLabel);
        modInfoPanel.add(modIdLabel);
        modInfoPanel.add(modAuthorsLabel);
        modInfoPanel.add(modEnvironmentLabel);
        modInfoPanel.add(modName);
        modInfoPanel.add(modMinecraftVersion);
        modInfoPanel.add(modVersion);
        modInfoPanel.add(modId);
        modInfoPanel.add(modAuthors);
        modInfoPanel.add(modEnvironment);

        this.add(modInfoPanel);
        this.add(installedModFileDropList);
        this.add(toggleInstalledModState);
        this.add(checkForModUpdate);
        this.add(openModsFolder);
        this.add(refreshMods);
        this.add(modWebsite);
        this.add(modIssues);

        installedModFileDropList.refresh();
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
