package me.flashyreese.fabricmm.ui.components.swing;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.utils.ModUtils;

import java.awt.Color;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class ModFileDropList extends JPanel implements DropTargetListener {

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
    public ModFileDropList() {
        setLayout(null);
        ModListCellRenderer renderer = new ModListCellRenderer();
        listModel = new DefaultListModel<InstalledMod>();
        list = new JList<InstalledMod>();
        new DropTarget(list, this);
        list.setModel(listModel);
        list.setDragEnabled(true);
        list.setCellRenderer(renderer);
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
        List<InstalledMod> installedModsEnabled = new ArrayList<InstalledMod>();
        List<InstalledMod> installedModsDisabled = new ArrayList<InstalledMod>();
        for(int i = 0; i < listModel.size(); i++){
            InstalledMod currentMod = listModel.elementAt(i);
            if(currentMod.isEnabled()){
                installedModsEnabled.add(currentMod);
            }else{
                installedModsDisabled.add(currentMod);
            }
        }
        listModel.removeAllElements();
        List<InstalledMod> newList = Stream.concat(installedModsEnabled.stream(), installedModsDisabled.stream()).collect(Collectors.toList());
        for(InstalledMod installedMod: newList){
            listModel.addElement(installedMod);
        }
        list.repaint();
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
        System.out.println(action);
        try {
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
        }
    }
}