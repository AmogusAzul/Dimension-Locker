package io.github.amogusazul.end_locked;


import io.github.amogusazul.end_locked.CommonClass;
import io.github.amogusazul.end_locked.Constants;
import io.github.amogusazul.end_locked.platform.NeoForgePlatformHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class EndLocked {

    public EndLocked(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        CommonClass.init();

    }
}