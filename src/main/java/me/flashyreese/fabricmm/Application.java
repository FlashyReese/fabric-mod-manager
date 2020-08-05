package me.flashyreese.fabricmm;

import com.vdurmont.semver4j.Semver;
import me.flashyreese.fabricmm.ui.FabricModManagerUI;
import org.kamranzafar.jddl.DirectDownloader;

import javax.swing.*;
import java.awt.*;

public class Application {

    private static final Semver VERSION = new Semver("0.0.4-SNAPSHOT+build-20200804",Semver.SemverType.STRICT);
    public static final DirectDownloader DIRECT_DOWNLOADER = new DirectDownloader();

    public static void main(String[] args) throws InterruptedException {
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
        Thread downloaderThread = new Thread(DIRECT_DOWNLOADER);
        downloaderThread.start();
        downloaderThread.join();
    }

    public static Semver getVersion(){
        return VERSION;
    }
}
