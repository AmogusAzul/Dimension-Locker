package io.github.amogusazul.dimension_locker.platform.services;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.level.GameRules;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface CommonRegistry {
    <T> Supplier<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator);
    boolean reSupplyDataComponent();
    void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command);

    GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue);
}