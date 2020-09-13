package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.api.schema.repository.MinecraftVersion;
import me.flashyreese.fabricmm.api.schema.repository.Project;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class ProjectList extends JPanel {

    private final DefaultListModel<Project> listModel;
    private final JScrollPane jScrollPane1;
    private final JList<Project> list;

    public ProjectList() {
        setLayout(null);
        listModel = new DefaultListModel<>();
        list = new JList<>();
        list.setModel(listModel);
        list.setDragEnabled(true);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Project) {
                    Project project = (Project) value;
                    try {
                        renderer.setIcon(UserInterfaceUtils.getImageIconFromCache(project));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    renderer.setText(new ParsableI18nText("fmm.mod_browser.mod_list.label", project.getName(), project.getUser().getName()).toString());
                    renderer.setToolTipText(String.format("<html><p width=\"150\">%s</p></html>", project.getDescription()));
                }

                return renderer;
            }
        });
        jScrollPane1 = new JScrollPane(list);
        this.setBorder(new LineBorder(Color.BLACK));
        add(jScrollPane1);

    }

    public void searchFilter(String searchTerm, String type) {//Fixme: really jank, might include descriptions in general search
        DefaultListModel<Project> filteredItems = new DefaultListModel<>();
        ArrayList<Project> listProjects = new ArrayList<>(listModel.getSize());
        for (int i = 0; i < listModel.getSize(); i++) {
            listProjects.add(listModel.getElementAt(i));
        }
        if (searchTerm.isEmpty()) {
            list.setModel(listModel);
            return;
        }
        if (type.equals(new I18nText("fmm.mod_browser.filter.general").toString())) {
            for (Project project : listProjects) {
                if (project.getName().toLowerCase().contains(searchTerm.toLowerCase()) || project.getUser().getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    if (!filteredItems.contains(project)) {
                        filteredItems.addElement(project);
                    }
                }
                for (MinecraftVersion minecraftVersion : project.getMinecraftVersions()) {
                    if (minecraftVersion.getMinecraftVersion().toLowerCase().contains(searchTerm.toLowerCase())) {
                        if (!filteredItems.contains(project)) {
                            filteredItems.addElement(project);
                        }
                    }
                }
            }
        } else {
            for (Project project : listProjects) {
                if (type.equals(new I18nText("fmm.mod_browser.filter.name").toString())) {
                    if (project.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        filteredItems.addElement(project);
                    }
                } else if (type.equals(new I18nText("fmm.mod_browser.filter.author").toString())) {
                    if (project.getUser().getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        filteredItems.addElement(project);
                    }
                } else if (type.equals(new I18nText("fmm.mod_browser.filter.minecraft_version").toString())) {
                    for (MinecraftVersion minecraftVersion : project.getMinecraftVersions()) {
                        if (minecraftVersion.getMinecraftVersion().toLowerCase().contains(searchTerm.toLowerCase())) {
                            if (!filteredItems.contains(project)) {
                                filteredItems.addElement(project);
                            }
                        }
                    }
                }
            }
        }
        list.setModel(filteredItems);
    }

    public JList<Project> getList() {
        return list;
    }

    public Project getSelectedValue() {
        return list.getSelectedValue();
    }

    public void removeAllItems() {
        listModel.removeAllElements();
    }

    public void updateUI() {
        super.updateUI();
        if (list != null) {
            list.updateUI();
        }
    }

    public void addItem(Project p) {
        listModel.addElement(p);
    }

    public void removeSelectedItem() {
        listModel.removeElement(list.getSelectedValue());
    }

    public void setBounds(int x, int y, int x2, int y2) {
        super.setBounds(x, y, x2, y2);
    }

    public void setSize(int x, int y) {
        super.setSize(x, y);
        jScrollPane1.setSize(this.getSize());
    }
}
