package me.flashyreese.fabricmm.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InstalledModPopClickListener extends MouseAdapter {

    private InstalledModFileDropList installedModFileDropList;

    public InstalledModPopClickListener(InstalledModFileDropList installedModFileDropList) {
        this.installedModFileDropList = installedModFileDropList;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger() && installedModFileDropList.getSelectedValue() != null)
            doPop(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() && installedModFileDropList.getSelectedValue() != null)
            doPop(e);
    }

    private void doPop(MouseEvent e) {
        InstalledModPopUpMenu menu = new InstalledModPopUpMenu(installedModFileDropList);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}