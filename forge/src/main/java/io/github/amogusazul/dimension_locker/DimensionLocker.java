package io.github.amogusazul.dimension_locker;

import io.github.amogusazul.dimension_locker.Constants;
import io.github.amogusazul.dimension_locker.platform.ForgeCommonRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class DimensionLocker {

    public DimensionLocker(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        CommonClass.init();
        ForgeCommonRegistry.loadRegistries(modEventBus);

    }
}