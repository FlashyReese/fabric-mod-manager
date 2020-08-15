package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.InstalledMod;
import me.flashyreese.fabricmm.util.ModUtils;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

public class ModFileDropList extends JPanel implements DropTargetListener {

    private final DefaultListModel<InstalledMod> listModel;
    private final JScrollPane jScrollPane1;
    private final JList<InstalledMod> list;
    private File currentDirectory;

    public ModFileDropList() {
        setLayout(null);
        listModel = new DefaultListModel<>();
        list = new JList<>();
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
                            renderer.setIcon(mod.getIconPath() != null ? UserInterfaceUtils.getIconFromFile(new File(mod.getIconPath())) : UserInterfaceUtils.getGrayScaledIconFromResource("icon.png", 64));
                        }else{
                            renderer.setIcon(mod.getIconPath() != null ? UserInterfaceUtils.getGrayScaledIconFromFile(new File(mod.getIconPath())) : UserInterfaceUtils.getGrayScaledIconFromResource("icon.png", 64));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        renderer.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                    }
                    renderer.setText(String.format("%s %s", mod.getModMetadata().getName(), mod.getModMetadata().getVersion()));
                    renderer.setToolTipText(String.format("<html><p width=\"150\">%s</p></html>", mod.getModMetadata().getDescription()));
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

    public ArrayList<InstalledMod> getSelectedValues(){
        return (ArrayList<InstalledMod>) list.getSelectedValuesList();//Fixme: add multi disable and enable
    }

    public void removeAllItems(){
        listModel.removeAllElements();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        filterListModel();
        if (list != null){
            list.updateUI();
        }
    }

    public void filterListModel(){
        if (listModel != null){
            DefaultListModel<InstalledMod> filteredItems = new DefaultListModel<>();
            ArrayList<InstalledMod> listMods = new ArrayList<>(listModel.getSize());
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
    }

    public void dragOver(DropTargetDragEvent evt) {
    }

    public void dropActionChanged(DropTargetDragEvent arg0) {
    }

    public void dragExit(DropTargetEvent arg0) {
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
                    if(file.getName().endsWith(".jar") || file.getName().endsWith(".fabricmod") || file.getName().endsWith(".disabled")){
                        InstalledMod mod = ModUtils.getInstalledModFromJar(file);
                        File newFile = new File(currentDirectory, file.getName());
                        Files.copy(file.toPath(), newFile.toPath());
                        if (newFile.length() == file.length()){
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
            updateUI();
        }
    }

    public void setDirectory(File modsDirectory) {
        this.currentDirectory = modsDirectory;
    }

    public void reloadMods() throws Exception {
        listModel.removeAllElements();
        for (InstalledMod installedMod: ModUtils.getInstalledModsFromDir(this.currentDirectory)){
            listModel.addElement(installedMod);
        }
    }
}