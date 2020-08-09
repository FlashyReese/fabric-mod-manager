package com.thebrokenrail.modupdater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModUpdater {
    public static final String NAMESPACE = "modupdater";

    private static final String LOGGER_NAME = "ModUpdater";

    private static Logger getLogger() {
        return LogManager.getLogger(LOGGER_NAME);
    }

    public static void logWarn(String name, String msg) {
        getLogger().warn(String.format("%s: %s", name, msg));
    }

    public static void logInfo(String info) {
        getLogger().info(info);
    }
}
