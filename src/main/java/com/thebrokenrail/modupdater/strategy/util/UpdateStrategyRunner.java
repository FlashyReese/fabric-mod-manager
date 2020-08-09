package com.thebrokenrail.modupdater.strategy.util;

import com.thebrokenrail.modupdater.ModUpdater;
import com.thebrokenrail.modupdater.api.UpdateStrategy;
import com.thebrokenrail.modupdater.data.ModUpdate;
import com.thebrokenrail.modupdater.util.Util;
import me.flashyreese.fabricmm.schema.FabricModMetadata;
import me.flashyreese.fabricmm.schema.ModUpdaterConfig;

public class UpdateStrategyRunner {

    public static ModUpdate checkModForUpdate(FabricModMetadata metadata) {
        String name = metadata.getName() + " (" + metadata.getId() + ')';

        ModUpdaterConfig obj;
        if (metadata.getCustom().getModUpdater() != null) {
            obj = metadata.getCustom().getModUpdater();
        } else {
            obj = Util.getHardcodedConfig(metadata.getId());
            if (obj == null) {
                return null;
            }
        }

        String oldVersion = metadata.getVersion();

        String strategy = obj.getStrategy();

        UpdateStrategy strategyObj = UpdateStrategyRegistry.get(strategy);
        if (strategyObj == null) {
            ModUpdater.logWarn(name, "Invalid Strategy: " + name);
            return null;
        }

        return strategyObj.run(obj, oldVersion, name, metadata.getId());
    }
}
