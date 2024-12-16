package io.github.amogusazul.dimension_locker.platform;

import com.mojang.datafixers.kinds.Const;
import io.github.amogusazul.dimension_locker.Constants;
import io.github.amogusazul.dimension_locker.platform.services.CommonRegistry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ForgeCommonRegistry implements CommonRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

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

    @Override
    public <T> DataComponentType<T> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build()).get();
    }

    private static ResourceKey<Item> createItemId(String id) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id));
    }

    public static void loadRegistries(IEventBus eventBus) {
        ITEMS.register(eventBus);
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
