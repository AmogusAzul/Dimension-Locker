package io.github.amogusazul.end_locked.platform.services;

import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface CommonRegistry {
    <T extends Item> Supplier<T> registerItem(String itemName, Function<Item.Properties, Item> function, Item.Properties properties);
}