package me.flashyreese.fabricmm.ui.popmenu;

import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.components.InstalledModFileDropList;
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
        toggle.setText(installedModFileDropList.getSelectedValue().isEnabled() ? new I18nText("fmm.library.disable").toString() : new I18nText("fmm.library.enable").toString());
        toggle.addActionListener(arg0 -> {
            ModUtils.changeInstalledModState(installedModFileDropList.getSelectedValue());
            installedModFileDropList.updateUI();
        });
        delete.setText(new I18nText("fmm.library.pop_menu.delete").toString());
        delete.addActionListener(arg0 -> {
            int result = JOptionPane.showConfirmDialog(null, new ParsableI18nText("fmm.library.pop_menu.delete.message",
                    installedModFileDropList.getSelectedValue().getModMetadata().getName()).toString(), new I18nText("fmm.library.pop_menu.delete.title").toString(),
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
                InstalledMod installedMod = installedModFileDropList.getSelectedValue();
                File location = new File(installedMod.getInstalledPath());
                if(location.delete()){
                    installedModFileDropList.removeSelectedItem();
                    installedModFileDropList.updateUI();
                }
            }
        });
    }

    private void loadComponents(){
        this.add(toggle);
        this.add(delete);
    }
}
