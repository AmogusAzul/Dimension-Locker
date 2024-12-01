package io.github.amogusazul.end_locked.platform;

import io.github.amogusazul.end_locked.Constants;
import io.github.amogusazul.end_locked.platform.services.CommonRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeCommonRegistry implements CommonRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String itemName, Function<Item.Properties, Item> function, Item.Properties properties) {

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

    public static void loadItems(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
