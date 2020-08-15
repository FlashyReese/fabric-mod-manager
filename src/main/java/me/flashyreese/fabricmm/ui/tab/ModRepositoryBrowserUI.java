package me.flashyreese.fabricmm.ui.tab;

import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.common.util.URLUtil;
import me.flashyreese.fabricmm.Application;
import me.flashyreese.fabricmm.api.RepositoryManager;
import me.flashyreese.fabricmm.api.schema.repository.*;
import me.flashyreese.fabricmm.core.ConfigurationManager;
import me.flashyreese.fabricmm.ui.components.ProjectList;
import me.flashyreese.fabricmm.util.Dim2i;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;
import org.kamranzafar.jddl.DownloadListener;
import org.kamranzafar.jddl.DownloadTask;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModRepositoryBrowserUI extends JPanel{

    private JTextField searchBar;
    private ProjectList projectList;
    private JComboBox<String> filterType;
    private JComboBox<MinecraftVersion> minecraftVersion;
    private JLabel minecraftVersionLabel;
    private JComboBox<ModVersion> modVersion;
    private JLabel modVersionLabel;
    private JButton download;
    private JButton projectWebsite;
    private JButton projectSources;

    private JPanel projectInfoPanel;
    private JLabel projectNameLabel;
    private JLabel projectIdLabel;
    private JLabel projectDescriptionLabel;
    private JLabel projectName;
    private JLabel projectId;
    private JLabel projectDescription;

    private JPanel authorInfoPanel;
    private JLabel authorNameLabel;
    private JLabel authorName;
    private ArrayList<JButton> authorContacts;

    public ModRepositoryBrowserUI(JTabbedPane jTabbedPane, RepositoryManager repositoryManager, DownloadManagerUI downloadManager, TrayIcon trayIcon) {
        setLayout(null);
        setSize(new Dimension((int)jTabbedPane.getPreferredSize().getWidth() - 5, (int)jTabbedPane.getPreferredSize().getHeight() - 28));//Fixme: Jank AF
        initComponents();
        setupComponents(repositoryManager, downloadManager, trayIcon);
        loadComponents();
        updateComponentsText();
    }

    private void initComponents() {
        searchBar = new JTextField();
        projectList = new ProjectList();
        filterType = new JComboBox<>();
        minecraftVersion = new JComboBox<>();
        minecraftVersionLabel = new JLabel();
        modVersion = new JComboBox<>();
        modVersionLabel = new JLabel();
        download = new JButton();
        projectWebsite = new JButton();
        projectSources = new JButton();

        projectInfoPanel = new JPanel();
        projectNameLabel = new JLabel();
        projectIdLabel = new JLabel();
        projectDescriptionLabel = new JLabel();
        projectName = new JLabel();
        projectId = new JLabel();
        projectDescription = new JLabel();

        authorInfoPanel = new JPanel();
        authorNameLabel = new JLabel();
        authorName = new JLabel();
    }

    private void setupComponents(RepositoryManager repositoryManager, DownloadManagerUI downloadManager, TrayIcon trayIcon){
        Dim2i searchBarDim = new Dim2i(10, 10, this.getWidth() / 8 * 3 - 20, 30);
        searchBar.setBounds(searchBarDim.getOriginX(), searchBarDim.getOriginY(), searchBarDim.getWidth(), searchBarDim.getHeight());
        updateModList(repositoryManager);
        searchBar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                SwingUtilities.invokeLater(() -> projectList.searchFilter(searchBar.getText(), (String)filterType.getSelectedItem()));
            }
        });

        Dim2i modFileListDim = new Dim2i(10, 50, this.getWidth() / 2 - 20, this.getHeight() - 60);
        projectList.setLocation(modFileListDim.getOriginX(), modFileListDim.getOriginY());
        projectList.setSize(modFileListDim.getWidth(), modFileListDim.getHeight());
        projectList.getList().addListSelectionListener(arg0 -> onProjectListSelect());

        Dim2i searchTypeDim = new Dim2i(this.getWidth() / 8 * 3, 10, this.getWidth() / 8 - 10, 30);
        filterType.setBounds(searchTypeDim.getOriginX(), searchTypeDim.getOriginY(), searchTypeDim.getWidth(), searchTypeDim.getHeight());

        Font labelFont = new Font("Arial", Font.BOLD, 14);
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
        minecraftVersion.addActionListener(e -> updateModVersions());

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
                downloadMod(downloadManager, trayIcon);
            } catch (MalformedURLException | FileNotFoundException malformedURLException) {
                malformedURLException.printStackTrace();
            }
        });

        Dim2i projectWebsiteDim = new Dim2i(this.getWidth() / 2, this.getHeight() - 160, this.getWidth() / 4 - 5, 30);
        projectWebsite.setBounds(projectWebsiteDim.getOriginX(), projectWebsiteDim.getOriginY(), projectWebsiteDim.getWidth(), projectWebsiteDim.getHeight());
        projectWebsite.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(projectList.getSelectedValue().getProjectUrl()));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i projectSourcesDim = new Dim2i(this.getWidth() / 4 * 3 - 5, this.getHeight() - 160, this.getWidth() / 4 - 5, 30);
        projectSources.setBounds(projectSourcesDim.getOriginX(), projectSourcesDim.getOriginY(), projectSourcesDim.getWidth(), projectSourcesDim.getHeight());
        projectSources.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(projectList.getSelectedValue().getSourcesUrl()));
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }
        });

        Dim2i projectInfoPanelDim = new Dim2i(this.getWidth() / 2, 10, this.getWidth() / 2 - 10, this.getHeight() / 3);
        projectInfoPanel.setBounds(projectInfoPanelDim.getOriginX(), projectInfoPanelDim.getOriginY(), projectInfoPanelDim.getWidth(), projectInfoPanelDim.getHeight());
        projectInfoPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        projectInfoPanel.setLayout(null);
        Font projectLabelFont = new Font("Arial", Font.PLAIN, 14);
        int labelWidth = projectInfoPanel.getWidth() - 20;
        int labelFontHeight = labelFont.getSize() + 2;
        int projectLabelFontHeight = projectLabelFont.getSize() + 2;
        int baseHeightLabel = 10;
        int offsetHeightLabel = 40;
        int projectBaseHeightLabel = 28;
        int projectOffsetHeightLabel = 40;
        projectIdLabel.setBounds(10, baseHeightLabel, labelWidth, labelFontHeight);
        projectIdLabel.setFont(labelFont);
        projectNameLabel.setBounds(10, baseHeightLabel + offsetHeightLabel, labelWidth, labelFontHeight);
        projectNameLabel.setFont(labelFont);
        projectDescriptionLabel.setBounds(10, baseHeightLabel + offsetHeightLabel * 2, labelWidth, labelFontHeight);
        projectDescriptionLabel.setFont(labelFont);
        projectId.setBounds(15, projectBaseHeightLabel, labelWidth, projectLabelFontHeight);
        projectId.setFont(projectLabelFont);
        projectName.setBounds(15, projectBaseHeightLabel + projectOffsetHeightLabel, labelWidth, projectLabelFontHeight);
        projectName.setFont(projectLabelFont);
        projectDescription.setBounds(15, projectBaseHeightLabel + projectOffsetHeightLabel * 2, labelWidth,
                projectInfoPanelDim.getHeight() - (projectBaseHeightLabel + projectOffsetHeightLabel * 2) - baseHeightLabel);
        projectDescription.setFont(projectLabelFont);
        projectDescription.setVerticalAlignment(JLabel.TOP);

        Dim2i authorInfoPanelDim = new Dim2i(this.getWidth() / 2, this.getHeight() / 3 + 20, this.getWidth() / 2 - 10, this.getHeight() - this.getHeight() / 3 - 190);
        authorInfoPanel.setBounds(authorInfoPanelDim.getOriginX(), authorInfoPanelDim.getOriginY(), authorInfoPanelDim.getWidth(), authorInfoPanelDim.getHeight());
        authorInfoPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        authorInfoPanel.setLayout(null);
        authorNameLabel.setBounds(10, 10, authorInfoPanel.getWidth() - 20, labelFontHeight);
        authorNameLabel.setFont(labelFont);
        authorName.setBounds(15, 28, authorInfoPanel.getWidth() - 20, projectLabelFontHeight);
        authorName.setFont(projectLabelFont);
    }

    private ArrayList<JButton> createContactButtons(Project project, int x, int y){
        ArrayList<JButton> jButtonList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("discordUrl", "assets/icons/discord.png");
        map.put("githubUrl", "assets/icons/github.png");
        map.put("patreonUrl", "assets/icons/patreon.png");
        map.put("twitterUrl", "assets/icons/twitter.png");
        map.put("twitchUrl", "assets/icons/twitch.png");
        map.put("youtubeUrl", "assets/icons/youtube.png");
        int offsetX = 0;
        User user = project.getUser();
        for (Map.Entry<String, String> entry: user.getContacts().entrySet()){
            if (map.containsKey(entry.getKey())){
                JButton jButton = new JButton();
                jButton.setIcon(UserInterfaceUtils.getIconFromResource(map.get(entry.getKey()), 50));
                jButton.setBorderPainted(false);
                jButton.setFocusPainted(false);
                jButton.setContentAreaFilled(false);
                jButton.setOpaque(true);
                jButton.setBounds(x + offsetX, y, 64, 64);
                jButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(entry.getValue()));
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                });
                jButtonList.add(jButton);
                offsetX += 60;
            }
        }
        return jButtonList;
    }

    private void loadComponents() {
        projectInfoPanel.add(projectIdLabel);
        projectInfoPanel.add(projectId);
        projectInfoPanel.add(projectNameLabel);
        projectInfoPanel.add(projectName);
        projectInfoPanel.add(projectDescriptionLabel);
        projectInfoPanel.add(projectDescription);

        authorInfoPanel.add(authorNameLabel);
        authorInfoPanel.add(authorName);
        this.add(searchBar);
        this.add(projectList);
        this.add(filterType);
        this.add(minecraftVersion);
        this.add(minecraftVersionLabel);
        this.add(modVersion);
        this.add(modVersionLabel);
        this.add(download);
        this.add(projectWebsite);
        this.add(projectSources);
        this.add(projectInfoPanel);
        this.add(authorInfoPanel);
    }


    public void updateComponentsText(){
        filterType.removeAllItems();
        filterType.addItem(new I18nText("fmm.mod_browser.filter.general").toString());
        filterType.addItem(new I18nText("fmm.mod_browser.filter.name").toString());
        filterType.addItem(new I18nText("fmm.mod_browser.filter.author").toString());
        filterType.addItem(new I18nText("fmm.mod_browser.filter.minecraft_version").toString());
        minecraftVersionLabel.setText(new I18nText("fmm.mod_browser.minecraft_version").toString());
        modVersionLabel.setText(new I18nText("fmm.mod_browser.mod_version").toString());
        download.setText(new I18nText("fmm.mod_browser.download").toString());
        projectWebsite.setText(new I18nText("fmm.mod_browser.project.website").toString());
        projectSources.setText(new I18nText("fmm.mod_browser.project.sources").toString());
        projectIdLabel.setText(new I18nText("fmm.mod_browser.project_info.id").toString());
        projectNameLabel.setText(new I18nText("fmm.mod_browser.project_info.name").toString());
        projectDescriptionLabel.setText(new I18nText("fmm.mod_browser.project_info.description").toString());
        authorNameLabel.setText(new I18nText("fmm.mod_browser.author_info.name").toString());
        onProjectListSelect();
    }

    private void onProjectListSelect(){
        if(projectList.getSelectedValue() != null){
            updateMinecraftVersions();
            if(minecraftVersion.getSelectedItem() != null){
                minecraftVersion.setEnabled(true);
                if (modVersion.getSelectedItem() != null){
                    modVersion.setEnabled(true);
                    download.setEnabled(true);
                }else{
                    modVersion.setEnabled(false);
                    download.setEnabled(false);
                }
            }else{
                minecraftVersion.setEnabled(false);
                modVersion.setEnabled(false);
                download.setEnabled(false);
            }
            Project project = projectList.getSelectedValue();
            projectId.setText(project.getId());
            projectName.setText(project.getName());
            projectDescription.setText("<html>" + project.getDescription() + "</html>");
            authorName.setText(project.getUser().getName());

            projectWebsite.setEnabled(project.getProjectUrl() != null && !project.getProjectUrl().isEmpty());
            projectSources.setEnabled(project.getSourcesUrl() != null && !project.getSourcesUrl().isEmpty());

            if (authorContacts != null){
                for (JButton button: authorContacts){
                    authorInfoPanel.remove(button);
                }
            }
            authorContacts = createContactButtons(projectList.getSelectedValue(), 10, 50);
            for (JButton button: authorContacts){
                authorInfoPanel.add(button);
            }
            authorInfoPanel.updateUI();
        }else{
            minecraftVersion.setEnabled(false);
            modVersion.setEnabled(false);
            download.setEnabled(false);
            projectWebsite.setEnabled(false);
            projectSources.setEnabled(false);
            projectId.setText(new I18nText("fmm.library.none_selected").toString());
            projectName.setText(new I18nText("fmm.library.none_selected").toString());
            projectDescription.setText(new I18nText("fmm.library.none_selected").toString());
            authorName.setText(new I18nText("fmm.library.none_selected").toString());
            if (authorContacts != null){
                for (JButton button: authorContacts){
                    authorInfoPanel.remove(button);
                }
            }
            authorInfoPanel.updateUI();
            minecraftVersion.removeAllItems();
            modVersion.removeAllItems();
        }
    }

    private void updateMinecraftVersions(){
        minecraftVersion.removeAllItems();
        minecraftVersion.setSelectedItem(null);
        for(MinecraftVersion mcVer: projectList.getSelectedValue().getMinecraftVersions()){
            minecraftVersion.addItem(mcVer);
        }
        updateModVersions();
    }

    private void updateModVersions(){
        modVersion.removeAllItems();
        modVersion.setSelectedItem(null);
        if (minecraftVersion.getSelectedItem() instanceof MinecraftVersion){
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            if(mcVer != null){
                for(ModVersion modVer: mcVer.getModVersions()){
                    modVersion.addItem(modVer);
                }
            }
        }
    }

    private void downloadMod(DownloadManagerUI downloadManager, TrayIcon trayIcon) throws MalformedURLException, FileNotFoundException {//Fixme: literally
        if(projectList.getSelectedValue() != null && minecraftVersion.getSelectedItem() != null && modVersion.getSelectedItem() != null){
            Project project = projectList.getSelectedValue();
            MinecraftVersion mcVer = (MinecraftVersion) minecraftVersion.getSelectedItem();
            ModVersion modVer = (ModVersion) modVersion.getSelectedItem();//Todo: also download dependencies
            File fileName = new File(ConfigurationManager.getInstance().MOD_CACHE_DIR, URLUtil.getFileNameWithExtension(modVer.getModUrl()));
            DownloadTask task = new DownloadTask(new URL(modVer.getModUrl()), new FileOutputStream(fileName), new DownloadListener() {
                public void onUpdate(int bytes, int totalDownloaded) {
                }

                public void onStart(String fileName, int size) {
                }

                public void onComplete() {
                    trayIcon.displayMessage(new ParsableI18nText("fmm.mod_browser.tray_icon.download_complete.caption",
                            project.getName(), modVer.getModVersion(), mcVer.getMinecraftVersion()).toString(),
                            new I18nText("fmm.mod_browser.tray_icon.download_complete.text").toString(), TrayIcon.MessageType.INFO);
                    try {
                        downloadManager.refreshMods();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onCancel() {
                }
            });
            Application.DIRECT_DOWNLOADER.download(task);
        }
    }

    public void updateModList(RepositoryManager repositoryManager) {
        projectList.removeAllItems();
        for(User user: repositoryManager.getUsers()){
            for (Project project: user.getProjects()){
                project.setUser(user);
                projectList.addItem(project);
            }
        }
        projectList.updateUI();
    }
}
