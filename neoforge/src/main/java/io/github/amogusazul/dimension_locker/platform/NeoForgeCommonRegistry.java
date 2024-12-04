package io.github.amogusazul.dimension_locker.platform;

import io.github.amogusazul.dimension_locker.Constants;
import io.github.amogusazul.dimension_locker.platform.services.CommonRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeCommonRegistry implements CommonRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String itemName, Function<Item.Properties, T> function, Item.Properties properties) {

        ResourceKey<Item> itemId = createItemId(itemName);

        Supplier<Item> itemSupplier = () -> function.apply(properties.setId(itemId));

        // Register the item using the given function
        ITEMS.register(itemName, itemSupplier);

        // Return a Supplier<T> by casting the result to T
        return () -> {
            @SuppressWarnings("unchecked")
            T castedItem = (T) itemSupplier.get();
            return castedItem;
        };
    }

    private static ResourceKey<Item> createItemId(String id) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id));
    }
}
