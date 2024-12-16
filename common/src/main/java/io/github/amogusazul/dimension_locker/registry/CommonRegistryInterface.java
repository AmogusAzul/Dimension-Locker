package io.github.amogusazul.dimension_locker.registry;

import io.github.amogusazul.dimension_locker.platform.Services;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CommonRegistryInterface {

    /**
     * Registers a new Item.
     *
     * @param itemName    The name of the item.
     * @param itemBuilder A supplier for the item.
     * @param <T>         The type of the item.
     * @return A supplier for the registered item.
     */
    static <T extends Item> Supplier<T> registerItem(String itemName, Function<Item.Properties, T> itemBuilder, Item.Properties properties) {
        return Services.COMMON_REGISTRY.registerItem(itemName, itemBuilder, properties);
    }

    static <T> DataComponentType<T> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return Services.COMMON_REGISTRY.registerDataComponent(name, builderOperator);
    }
}
