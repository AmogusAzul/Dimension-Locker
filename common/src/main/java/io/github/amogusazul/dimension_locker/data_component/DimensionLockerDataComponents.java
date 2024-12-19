package io.github.amogusazul.dimension_locker.data_component;

import io.github.amogusazul.dimension_locker.registry.CommonRegistryInterface;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DimensionLockerDataComponents {
    public static final Supplier<DataComponentType<ResourceKey<Level>>> DIMENSION =
            CommonRegistryInterface.registerDataComponent("dimension", (DataComponentType.Builder<ResourceKey<Level>> builder) -> builder.persistent(Level.RESOURCE_KEY_CODEC));


    public static void registerDataComponentTypes() {
    }
}
