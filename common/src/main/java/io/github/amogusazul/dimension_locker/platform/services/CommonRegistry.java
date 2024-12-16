package io.github.amogusazul.dimension_locker.platform.services;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CommonRegistry {
    <T extends Item> Supplier<T> registerItem(String itemName, Function<Item.Properties, T> function, Item.Properties properties);
    <T> DataComponentType<T> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator);
}