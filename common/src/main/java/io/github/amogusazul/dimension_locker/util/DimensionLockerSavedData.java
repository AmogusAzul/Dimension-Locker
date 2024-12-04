package io.github.amogusazul.dimension_locker.util;

import io.github.amogusazul.dimension_locker.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class DimensionLockerSavedData extends SavedData {

    public static final String DATA_NAME = "dimension_locker_data";

    public static final String LOCKED_DIMENSION_KEY = "locked_dimension";

    private boolean locked;

    public DimensionLockerSavedData(){
        this.locked = false;
    }

    // Method to load data from NBT
    public static DimensionLockerSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        DimensionLockerSavedData data = new DimensionLockerSavedData();
        data.locked = tag.getBoolean(LOCKED_DIMENSION_KEY);

        Constants.LOG.debug("LOADED DIMENSION LOCKED = {}", data.locked);

        return data;
    }


    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putBoolean(LOCKED_DIMENSION_KEY, locked);
        setDirty();
        return compoundTag;
    }

    // Getter and Setter for the boolean value
    public boolean isLocked() {
        return this.locked;
    }

    public void lockInto(boolean value) {
        this.locked = value;
        setDirty(); // Mark the data as dirty to ensure it gets saved
    }
}

