package io.github.amogusazul.dimension_locker.platform;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.amogusazul.dimension_locker.Constants;
import io.github.amogusazul.dimension_locker.platform.services.CommonRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class NeoForgeCommonRegistry implements CommonRegistry {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);

    public static List<LiteralArgumentBuilder<CommandSourceStack>> COMMAND_QUEUE = new ArrayList<>();


    @Override
    public <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    @Override
    public boolean reSupplyDataComponent() {
        return true;
    }

    @Override
    public void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        COMMAND_QUEUE.add(command);
    }


}
