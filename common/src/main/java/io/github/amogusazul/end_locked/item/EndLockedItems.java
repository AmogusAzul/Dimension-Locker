package io.github.amogusazul.end_locked.item;

import io.github.amogusazul.end_locked.Constants;
import io.github.amogusazul.end_locked.registry.CommonItemRegistryInterface;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public record EndLockedItems() implements CommonItemRegistryInterface {
    public static final Supplier<Item> NETHER_PEBBLE = CommonItemRegistryInterface.registerItem(
            "nether_pebble", () -> new PebbleItem(Level.NETHER), PebbleItem.getDefaultProperties());
    public static final Supplier<Item> END_PEBBLE = CommonItemRegistryInterface.registerItem(
            "end_pebble", () -> new PebbleItem(Level.END), PebbleItem.getDefaultProperties());

    public static void loadItems() {}
}
