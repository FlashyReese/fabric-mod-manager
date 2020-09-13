package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.common.util.IntegerUtil;
import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.popmenu.InstalledModPopUpMenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModPopClickListener extends MouseAdapter {

    private final ModFileDropList modFileDropList;

    public ModPopClickListener(ModFileDropList modFileDropList) {
        this.modFileDropList = modFileDropList;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JList<InstalledMod> list = modFileDropList.getList();
            if (!IntegerUtil.containsIndexFromArray(list.getSelectedIndices(), list.locationToIndex(e.getPoint()))) {
                int row = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(row);
            }
            doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JList<InstalledMod> list = modFileDropList.getList();
            if (!IntegerUtil.containsIndexFromArray(list.getSelectedIndices(), list.locationToIndex(e.getPoint()))) {
                int row = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(row);
            }
            doPop(e);
        }
    }

    private void doPop(MouseEvent e) {
        InstalledModPopUpMenu menu = new InstalledModPopUpMenu(modFileDropList);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}