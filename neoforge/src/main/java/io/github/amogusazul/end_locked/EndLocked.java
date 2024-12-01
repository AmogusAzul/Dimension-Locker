package io.github.amogusazul.end_locked;


import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.amogusazul.end_locked.platform.NeoForgeCommonRegistry.ITEMS;

@Mod(Constants.MOD_ID)
public class EndLocked {

    public EndLocked(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        CommonClass.init();
        ITEMS.register(eventBus);

    }
}