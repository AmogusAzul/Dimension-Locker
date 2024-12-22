package io.github.amogusazul.dimension_locker.command;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.amogusazul.dimension_locker.registry.CommonRegistryInterface;
import io.github.amogusazul.dimension_locker.util.DataHandler;
import io.github.amogusazul.dimension_locker.util.DimensionLockerSavedData;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

import static net.minecraft.commands.Commands.*;


public class DimensionLockerCommands {

    public static void registerCommands(){

        LiteralArgumentBuilder<CommandSourceStack> dimension_lock = literal("dimension")
                .requires(source -> source.hasPermission(2))
                .then(literal("lock")
                        .then(argument("dimension", DimensionArgument.dimension())
                                .executes(context -> {
                                    // Getting dimension's persistent data editor
                                    DataHandler dataHandler = new DataHandler();
                                    final ServerLevel level = DimensionArgument.getDimension(context, "dimension");
                                    DimensionLockerSavedData dimensionSD = dataHandler.getDimensionData(level);
                                    dimensionSD.lockInto(true);

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable(
                                                    "command.dimension_locker.dimension_locked",
                                                    level.dimension().location().toString()

                                            ),
                                            true
                                    );

                                    return 1;

                                })));
        CommonRegistryInterface.registerCommand(dimension_lock);

        LiteralArgumentBuilder<CommandSourceStack> dimension_unlock = literal("dimension")
                .requires(source -> source.hasPermission(2))
                .then(literal("unlock")
                        .then(argument("dimension", DimensionArgument.dimension())
                                .executes(context -> {
                                    // Getting dimension's persistent data editor
                                    DataHandler dataHandler = new DataHandler();
                                    final ServerLevel level = DimensionArgument.getDimension(context, "dimension");
                                    DimensionLockerSavedData dimensionSD = dataHandler.getDimensionData(level);
                                    dimensionSD.lockInto(false);

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable(
                                                    "command.dimension_locker.dimension_unlocked",
                                                    level.dimension().location().toString()
                                            ),
                                            true
                                    );

                                    return 1;

                                })));

        CommonRegistryInterface.registerCommand(dimension_unlock);

        LiteralArgumentBuilder<CommandSourceStack> dimension_islocked = literal("dimension")
                .then(literal("isLocked")
                        .then(argument("dimension", DimensionArgument.dimension())
                                .executes(context -> {

                                    // Getting dimension's persistent data editor
                                    DataHandler dataHandler = new DataHandler();
                                    final ServerLevel level = DimensionArgument.getDimension(context, "dimension");
                                    DimensionLockerSavedData dimensionSD = dataHandler.getDimensionData(level);

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable(
                                                    "command.dimension_locker.dimension_islocked",
                                                    level.dimension().location().toString(),
                                                    Component.translatable(dimensionSD.isLocked() ?
                                                            "command.dimension_locker.locked" :
                                                            "command.dimension_locker.unlocked"
                                                    ).getString()
                                            ),
                                            false
                                    );

                                    return 0;

                                })));
        CommonRegistryInterface.registerCommand(dimension_islocked);


    }
}
