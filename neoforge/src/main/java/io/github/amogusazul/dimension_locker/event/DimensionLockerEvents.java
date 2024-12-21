package io.github.amogusazul.dimension_locker.event;

import io.github.amogusazul.dimension_locker.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

import static io.github.amogusazul.dimension_locker.platform.NeoForgeCommonRegistry.COMMAND_QUEUE;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class DimensionLockerEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        COMMAND_QUEUE.forEach((command) -> event.getDispatcher().register(command));

        ConfigCommand.register(event.getDispatcher());
    }
}
