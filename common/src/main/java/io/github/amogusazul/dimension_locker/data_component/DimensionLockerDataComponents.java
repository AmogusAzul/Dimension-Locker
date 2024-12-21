package io.github.amogusazul.dimension_locker.data_component;

import io.github.amogusazul.dimension_locker.registry.CommonRegistryInterface;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class DimensionLockerDataComponents {
    public static final TypeProvider<ResourceKey<Level>> DIMENSION =
            new TypeProvider<>(
                    CommonRegistryInterface.registerDataComponent(
                            "dimension",
                            (DataComponentType.Builder<ResourceKey<Level>> builder) ->
                                    builder.persistent(Level.RESOURCE_KEY_CODEC)),
                    CommonRegistryInterface.reSupplyDataComponent()
            );
    public static final DataComponentType<Unit> CANT_ENTER_ENDER_CHEST =
            CommonRegistryInterface.registerDataComponent("cant_enter_ender_chest", (DataComponentType.Builder<Unit> builder) -> builder.persistent(Unit.CODEC)).get();
    public static final DataComponentType<Unit> UN_DESPAWNABLE =
            CommonRegistryInterface.registerDataComponent("un_despawnable", (DataComponentType.Builder<Unit> builder) -> builder.persistent(Unit.CODEC)).get();

    public static class TypeProvider<T>{

        private final boolean reSupply;
        private Supplier<DataComponentType<T>> supplier;
        private DataComponentType<T> type;

        TypeProvider(Supplier<DataComponentType<T>> supplier, boolean reSupply){

            this.reSupply = reSupply;
            if (reSupply){
                this.supplier = supplier;
            } else{
                this.type = supplier.get();
            }
        }

        public DataComponentType<T> getType(){
            return reSupply ? supplier.get() : type;
        }
    }

    public static void registerDataComponentTypes() {
    }
}
