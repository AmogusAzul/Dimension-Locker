package io.github.amogusazul.end_locked.registry;

import io.github.amogusazul.end_locked.Constants;
import io.github.amogusazul.end_locked.platform.services.CommonRegistry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;
import io.github.amogusazul.end_locked.platform.Services;

public interface CommonItemRegistryInterface {

    /**
     * Registers a new Item.
     *
     * @param itemName The name of the item.
     * @param item     A supplier for the item.
     * @param <T>      The type of the item.
     * @return A supplier for the registered item.
     */
    static <T extends Item> Supplier<T> registerItem(String itemName, Supplier<T> item, Item.Properties properties) {
        return Services.COMMON_REGISTRY.registerItem(itemName, Item::new, properties);
    }
}
