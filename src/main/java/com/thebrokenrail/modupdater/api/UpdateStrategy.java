package com.thebrokenrail.modupdater.api;

import com.thebrokenrail.modupdater.data.ModUpdate;
import me.flashyreese.fabricmm.schema.ModUpdaterConfig;

import javax.annotation.Nullable;

public interface UpdateStrategy {
    @Nullable
    ModUpdate run(ModUpdaterConfig obj, String oldVersion, String name, String id);

    default boolean isStrict(ModUpdaterConfig obj) {
        return obj.isStrict();
    }
}
