package me.flashyreese.fabricmm;

import me.flashyreese.fabricmm.ui.FabricModManagerUI;

import javax.swing.*;
import java.awt.*;

public class Application {

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            FabricModManagerUI fabricModManagerUI = null;
            try {
                fabricModManagerUI = new FabricModManagerUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fabricModManagerUI.setVisible(true);
        });
    }
}
