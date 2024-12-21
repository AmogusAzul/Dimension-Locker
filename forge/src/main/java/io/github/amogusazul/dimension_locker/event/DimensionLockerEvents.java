package io.github.amogusazul.dimension_locker.event;

import io.github.amogusazul.dimension_locker.Constants;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import static io.github.amogusazul.dimension_locker.platform.ForgeCommonRegistry.COMMAND_QUEUE;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class DimensionLockerEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        COMMAND_QUEUE.forEach((command) -> event.getDispatcher().register(command));

        ConfigCommand.register(event.getDispatcher());
    }
}
