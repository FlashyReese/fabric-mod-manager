package me.flashyreese.fabricmm.ui.popmenu;

import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.components.ModFileDropList;
import me.flashyreese.fabricmm.util.ModUtils;

import javax.swing.*;
import java.io.File;

public class InstalledModPopUpMenu extends JPopupMenu {

    private JMenuItem enable;
    private JMenuItem disable;
    private JMenuItem checkForUpdate;
    private JMenuItem delete;

    public InstalledModPopUpMenu(ModFileDropList modFileDropList) {
        initComponents();
        setupComponents(modFileDropList);
        loadComponents();
    }

    private void initComponents(){
        enable = new JMenuItem();
        disable = new JMenuItem();
        checkForUpdate = new JMenuItem();
        delete = new JMenuItem();
    }

    private void setupComponents(ModFileDropList modFileDropList){
        enable.setText(new I18nText("fmm.library.enable").toString());
        disable.setText(new I18nText("fmm.library.disable").toString());
        checkForUpdate.setText(new I18nText("fmm.library.pop_menu.check_for_update").toString());
        delete.setText(new I18nText("fmm.library.pop_menu.delete").toString());

        if (modFileDropList.getSelectedValues().size() == 1){
            enable.setEnabled(!modFileDropList.getSelectedValue().isEnabled());
            enable.addActionListener(arg0 -> {
                ModUtils.changeInstalledModState(modFileDropList.getSelectedValue(), true);
                modFileDropList.updateUI();
            });

            disable.setEnabled(modFileDropList.getSelectedValue().isEnabled());
            disable.addActionListener(arg0 -> {
                ModUtils.changeInstalledModState(modFileDropList.getSelectedValue(), false);
                modFileDropList.updateUI();
            });

            checkForUpdate.addActionListener(e -> {
            });

            delete.addActionListener(arg0 -> {
                int result = JOptionPane.showConfirmDialog(null, new ParsableI18nText("fmm.library.pop_menu.delete.message",
                                modFileDropList.getSelectedValue().getModMetadata().getName()).toString(), new I18nText("fmm.library.pop_menu.delete.title").toString(),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    InstalledMod installedMod = modFileDropList.getSelectedValue();
                    File location = new File(installedMod.getInstalledPath());
                    if(location.delete()){
                        modFileDropList.removeSelectedItem();
                        modFileDropList.updateUI();
                    }
                }
            });
        }else{
            enable.addActionListener(arg0 -> {
                for (InstalledMod selectedValue: modFileDropList.getSelectedValues()){
                    ModUtils.changeInstalledModState(selectedValue, true);
                }
                modFileDropList.updateUI();
            });

            checkForUpdate.addActionListener(e -> {
            });

            disable.addActionListener(arg0 -> {
                for (InstalledMod selectedValue: modFileDropList.getSelectedValues()){
                    ModUtils.changeInstalledModState(selectedValue, false);
                }
                modFileDropList.updateUI();
            });
        }
    }

    private void loadComponents(){
        this.add(enable);
        this.add(disable);
        this.add(checkForUpdate);
        this.add(delete);
    }
}
