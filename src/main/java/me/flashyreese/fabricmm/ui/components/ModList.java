package me.flashyreese.fabricmm.ui.components;

import me.flashyreese.fabricmm.schema.repository.Mod;
import me.flashyreese.fabricmm.util.UserInterfaceUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class ModList  extends JPanel {

    private final DefaultListModel<Mod> listModel;
    private final JScrollPane jScrollPane1;
    private final JList<Mod> list;

    public ModList() {
        setLayout(null);
        listModel = new DefaultListModel<Mod>();
        list = new JList<Mod>();
        list.setModel(listModel);
        list.setDragEnabled(true);
        list.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Mod) {
                    Mod mod = (Mod)value;
                    try {
                        renderer.setIcon(UserInterfaceUtils.getImageIconFromCache(mod));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    renderer.setText(mod.getName());
                    renderer.setToolTipText(String.format("<html><p width=\"150\">%s</p></html>", mod.getDescription()));
                }

                return renderer;
            }
        });
        jScrollPane1 = new JScrollPane(list);
        this.setBorder(new LineBorder(Color.BLACK));
        add(jScrollPane1);

    }

    public JList<Mod> getList(){
        return list;
    }

    public Mod getSelectedValue() {
        return list.getSelectedValue();
    }

    public void removeAllItems(){
        listModel.removeAllElements();
    }

    public void refresh() {
        list.repaint();
    }

    public void addItem(Mod p) {
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
}
