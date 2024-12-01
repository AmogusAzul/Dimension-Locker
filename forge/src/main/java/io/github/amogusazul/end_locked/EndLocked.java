package io.github.amogusazul.end_locked;

import io.github.amogusazul.end_locked.platform.ForgeCommonRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class EndLocked {

    public EndLocked(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        CommonClass.init();
        ForgeCommonRegistry.loadItems(modEventBus);

    }
}