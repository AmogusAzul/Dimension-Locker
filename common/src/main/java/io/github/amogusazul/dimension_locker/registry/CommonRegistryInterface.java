package io.github.amogusazul.dimension_locker.registry;

import io.github.amogusazul.dimension_locker.platform.Services;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CommonRegistryInterface {

    static <T> Supplier<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return Services.COMMON_REGISTRY.registerDataComponent(name, builderOperator);
    }
}
