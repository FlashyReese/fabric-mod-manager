package me.flashyreese.fabricmm.ui.popmenu;

import me.flashyreese.common.i18n.ParsableTranslatableText;
import me.flashyreese.common.i18n.TranslatableText;
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
        toggle.setText(installedModFileDropList.getSelectedValue().isEnabled() ? new TranslatableText("fmm.library.disable").toString() : new TranslatableText("fmm.library.enable").toString());
        toggle.addActionListener(arg0 -> {
            ModUtils.changeInstalledModState(installedModFileDropList.getSelectedValue());
            installedModFileDropList.refresh();
        });
        delete.setText(new TranslatableText("fmm.library.pop_menu.delete").toString());
        delete.addActionListener(arg0 -> {
            int result = JOptionPane.showConfirmDialog(null, new ParsableTranslatableText("fmm.library.pop_menu.delete.message",
                    installedModFileDropList.getSelectedValue().getName()).toString(), new TranslatableText("fmm.library.pop_menu.delete.title").toString(),
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
                InstalledMod installedMod = installedModFileDropList.getSelectedValue();
                File location = new File(installedMod.getInstalledPath());
                if(location.delete()){
                    installedModFileDropList.removeSelectedItem();
                    installedModFileDropList.refresh();
                }
            }
        });
    }

    private void loadComponents(){
        this.add(toggle);
        this.add(delete);
    }
}
