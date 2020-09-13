package me.flashyreese.fabricmm;

import com.bulenkov.darcula.DarculaLaf;
import com.vdurmont.semver4j.Semver;
import me.flashyreese.common.util.JarUtil;
import me.flashyreese.fabricmm.ui.FabricModManagerUI;
import org.json.JSONObject;
import org.kamranzafar.jddl.DirectDownloader;

import javax.swing.*;

public class Application {

    private static Semver VERSION;
    public static final DirectDownloader DIRECT_DOWNLOADER = new DirectDownloader();

    public static void main(String[] args) throws Exception {
        VERSION = new Semver(new JSONObject(JarUtil.readTextFile("fabric.mod.manager.json")).getString("version"), Semver.SemverType.STRICT);
        try {
            UIManager.setLookAndFeel(new DarculaLaf()/*UIManager.getSystemLookAndFeelClassName()*/);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            FabricModManagerUI fabricModManagerUI = null;
            try {
                fabricModManagerUI = new FabricModManagerUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert fabricModManagerUI != null;
            fabricModManagerUI.setVisible(true);
            FabricModManagerUI finalFabricModManagerUI = fabricModManagerUI;
            new Thread(() -> {
                try {
                    finalFabricModManagerUI.getRepositoryManager().updateLocalRepository();
                    finalFabricModManagerUI.getModBrowser().updateModList(finalFabricModManagerUI.getRepositoryManager());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        Thread downloaderThread = new Thread(DIRECT_DOWNLOADER);
        downloaderThread.start();
        downloaderThread.join();
    }

    public static Semver getVersion() {
        return VERSION;
    }
}
