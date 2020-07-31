package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class InstalledModListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;
    private final FileSystemView fileSystemView;
    private final JLabel label;
    private final Color textSelectionColor = Color.BLACK;
    private final Color backgroundSelectionColor = Color.CYAN;
    private final Color textNonSelectionColor = Color.DARK_GRAY;
    private final Color textDisableColor = Color.GRAY;
    private final Color backgroundNonSelectionColor = Color.WHITE;

    InstalledModListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean expanded) {

        InstalledMod mod = (InstalledMod)value;
        File file = new File(mod.getInstalledPath());
        try {
            if(mod.isEnabled()){
                label.setIcon(UserInterfaceUtils.getIconFromFile(new File(mod.getIconPath())));
            }else{
                label.setIcon(UserInterfaceUtils.getGrayScaledIconFromFile(new File(mod.getIconPath())));
            }
        } catch (IOException e) {
            e.printStackTrace();
            label.setIcon(fileSystemView.getSystemIcon(file));
        }
        label.setText(String.format("%s %s", mod.getName(), mod.getVersion()));
        label.setToolTipText(String.format("<html><p width=\"150\">%s</p></html>", mod.getDescription()));

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            if(mod.isEnabled()){
                label.setForeground(textNonSelectionColor);
            }else{
                label.setForeground(textDisableColor);
            }
        }

        return label;
    }
}