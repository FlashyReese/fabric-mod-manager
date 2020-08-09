package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.ui.popmenu.InstalledModPopUpMenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InstalledModPopClickListener extends MouseAdapter {

    private final InstalledModFileDropList installedModFileDropList;

    public InstalledModPopClickListener(InstalledModFileDropList installedModFileDropList) {
        this.installedModFileDropList = installedModFileDropList;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()){
            JList<InstalledMod> list = installedModFileDropList.getList();
            int row = list.locationToIndex(e.getPoint());
            list.setSelectedIndex(row);
            doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()){
            JList<InstalledMod> list = installedModFileDropList.getList();
            int row = list.locationToIndex(e.getPoint());
            list.setSelectedIndex(row);
            doPop(e);
        }
    }

    private void doPop(MouseEvent e) {
        InstalledModPopUpMenu menu = new InstalledModPopUpMenu(installedModFileDropList);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}