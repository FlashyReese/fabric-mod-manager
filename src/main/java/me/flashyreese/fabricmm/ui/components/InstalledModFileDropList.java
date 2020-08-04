package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.util.ModUtils;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

public class InstalledModFileDropList extends JPanel implements DropTargetListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DefaultListModel<InstalledMod> listModel;
    private final JScrollPane jScrollPane1;
    private final JList<InstalledMod> list;
    /**
     * Create the panel.
     */
    public InstalledModFileDropList() {
        setLayout(null);
        listModel = new DefaultListModel<InstalledMod>();
        list = new JList<InstalledMod>();
        new DropTarget(list, this);
        list.setModel(listModel);
        list.setDragEnabled(true);
        list.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof InstalledMod) {
                    InstalledMod mod = (InstalledMod)value;
                    File file = new File(mod.getInstalledPath());
                    try {
                        if(mod.isEnabled()){
                            renderer.setIcon(UserInterfaceUtils.getIconFromFile(new File(mod.getIconPath())));
                        }else{
                            renderer.setIcon(UserInterfaceUtils.getGrayScaledIconFromFile(new File(mod.getIconPath())));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        renderer.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                    }
                    renderer.setText(String.format("%s %s", mod.getName(), mod.getVersion()));
                    renderer.setToolTipText(String.format("<html><p width=\"150\">%s</p></html>", mod.getDescription()));
                }

                return renderer;
            }
        });
        jScrollPane1 = new JScrollPane(list);
        this.setBorder(new LineBorder(Color.BLACK));
        add(jScrollPane1);

    }

    public JList<InstalledMod> getList(){
        return list;
    }

    public InstalledMod getSelectedValue() {
        return list.getSelectedValue();
    }

    public void removeAllItems(){
        listModel.removeAllElements();
    }

    public void refresh() {
        filterListModel();
        list.repaint();
    }

    public void filterListModel(){
        DefaultListModel<InstalledMod> filteredItems = new DefaultListModel<InstalledMod>();
        ArrayList<InstalledMod> listMods = new ArrayList<InstalledMod>(listModel.getSize());
        for (int i = 0; i < listModel.getSize(); i++) {
            listMods.add(listModel.getElementAt(i));
        }
        for (InstalledMod mod: listMods){
            if (mod.isEnabled()){
                filteredItems.addElement(mod);
            }
        }
        for (InstalledMod mod: listMods){
            if (!mod.isEnabled()){
                filteredItems.addElement(mod);
            }
        }
        list.setModel(filteredItems);
    }

    public void addItem(InstalledMod p) {
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

    public void dragEnter(DropTargetDragEvent arg0) {
        // nothing
    }

    public void dragOver(DropTargetDragEvent evt) {
        /*int action = evt.getDropAction();
        evt.rejectDrag();*/

        // nothing
    }

    public void dropActionChanged(DropTargetDragEvent arg0) {
        // nothing
    }

    public void dragExit(DropTargetEvent arg0) {
        // nothing
    }


    public void drop(DropTargetDropEvent evt) {
        int action = evt.getDropAction();
        evt.acceptDrop(action);
        try {//Todo: add overlapping mod versions support - idea is to swap out old for new
            Transferable data = evt.getTransferable();
            if (data.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) data.getTransferData(DataFlavor.javaFileListFlavor);
                for (File file : files) {
                    if(file.getName().endsWith(".jar") || file.getName().endsWith(".fabricmod")){
                        InstalledMod mod = ModUtils.getInstalledModFromJar(file);
                        if(mod != null){
                            File newFile = new File(ModUtils.getModsDirectory(), file.getName());
                            Files.copy(file.toPath(), newFile.toPath());
                            InstalledMod newMod = ModUtils.getInstalledModFromJar(newFile);
                            listModel.addElement(newMod);
                        }
                    }
                }
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        } finally {
            evt.dropComplete(true);
            refresh();
        }
    }
}