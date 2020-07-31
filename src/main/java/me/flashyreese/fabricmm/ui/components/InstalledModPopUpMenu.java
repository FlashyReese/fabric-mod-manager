package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.util.ModUtils;

import javax.swing.*;
import java.io.File;

public class InstalledModPopUpMenu extends JPopupMenu {

    private JMenuItem toggle;
    private JMenuItem delete;

    public InstalledModPopUpMenu(InstalledModFileDropList installedModFileDropList) {
        initComponents();
        setupComponents(installedModFileDropList);
        loadComponents();
    }

    private void initComponents(){
        toggle = new JMenuItem();
        delete = new JMenuItem();
    }

    private void setupComponents(InstalledModFileDropList installedModFileDropList){
        toggle.setText(installedModFileDropList.getSelectedValue().isEnabled() ? "Disable" : "Enable");
        toggle.addActionListener(arg0 -> {
            ModUtils.changeInstalledModState(installedModFileDropList.getSelectedValue());
            installedModFileDropList.refresh();
        });
        delete.setText("Delete");
        delete.addActionListener(arg0 -> {
            InstalledMod installedMod = installedModFileDropList.getSelectedValue();
            File location = new File(installedMod.getInstalledPath());
            if(location.delete()){
                installedModFileDropList.removeSelectedItem();
                installedModFileDropList.refresh();
            }
        });
    }

    private void loadComponents(){
        this.add(toggle);
        this.add(delete);
    }
}
