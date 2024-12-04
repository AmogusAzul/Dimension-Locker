package io.github.amogusazul.dimension_locker.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class DataHandler {
    public DimensionLockerSavedData getDimensionData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        DimensionLockerSavedData::new,
                        DimensionLockerSavedData::load,
                        DataFixTypes.OPTIONS),
                DimensionLockerSavedData.DATA_NAME
        );
    }
}
