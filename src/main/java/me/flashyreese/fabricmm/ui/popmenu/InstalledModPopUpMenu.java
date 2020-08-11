package me.flashyreese.fabricmm.ui.popmenu;

import me.flashyreese.common.i18n.ParsableI18nText;
import me.flashyreese.common.i18n.I18nText;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.components.ModFileDropList;
import me.flashyreese.fabricmm.util.ModUtils;

import javax.swing.*;
import java.io.File;

public class InstalledModPopUpMenu extends JPopupMenu {

    private JMenuItem toggle;
    private JMenuItem delete;

    public InstalledModPopUpMenu(ModFileDropList modFileDropList) {
        initComponents();
        setupComponents(modFileDropList);
        loadComponents();
    }

    private void initComponents(){
        toggle = new JMenuItem();
        delete = new JMenuItem();
    }

    private void setupComponents(ModFileDropList modFileDropList){
        toggle.setText(modFileDropList.getSelectedValue().isEnabled() ? new I18nText("fmm.library.disable").toString() : new I18nText("fmm.library.enable").toString());
        toggle.addActionListener(arg0 -> {
            ModUtils.changeInstalledModState(modFileDropList.getSelectedValue());
            modFileDropList.updateUI();
        });
        delete.setText(new I18nText("fmm.library.pop_menu.delete").toString());
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
    }

    private void loadComponents(){
        this.add(toggle);
        this.add(delete);
    }
}
