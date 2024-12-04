package io.github.amogusazul.dimension_locker.item;

import io.github.amogusazul.dimension_locker.registry.CommonItemRegistryInterface;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public record DimensionLockerItems() implements CommonItemRegistryInterface {
    public static final Supplier<Item> NETHER_PEBBLE = CommonItemRegistryInterface.registerItem(
            "nether_pebble", (properties) -> new PebbleItem(properties, Level.NETHER), PebbleItem.getDefaultProperties());
    public static final Supplier<Item> END_PEBBLE = CommonItemRegistryInterface.registerItem(
            "end_pebble", (properties) -> new PebbleItem(properties, Level.END), PebbleItem.getDefaultProperties());


    public static void loadItems() {
    }

}
