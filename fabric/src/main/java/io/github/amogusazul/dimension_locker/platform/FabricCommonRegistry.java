package io.github.amogusazul.dimension_locker.platform;

import io.github.amogusazul.dimension_locker.platform.services.CommonRegistry;
import io.github.amogusazul.dimension_locker.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;


import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FabricCommonRegistry implements CommonRegistry {


    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, String id, Supplier<T> object) {
        final T registeredObject = Registry.register((Registry<T>) registry,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), object.get());

        return () -> registeredObject;
    }

    @Override
    public <T> Supplier<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {

        return () -> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    }

}