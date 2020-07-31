package me.flashyreese.fabricmm;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.fabricmm.ui.FabricModManagerUI;

import javax.swing.*;
import java.awt.*;

public class Application {

    private static final Semver VERSION = new Semver("0.0.1-SNAPSHOT+build-20200731",Semver.SemverType.STRICT);

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

    public static Semver getVersion(){
        return VERSION;
    }
}
