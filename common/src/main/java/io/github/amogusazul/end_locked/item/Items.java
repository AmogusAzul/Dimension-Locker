package io.github.amogusazul.end_locked.item;

import io.github.amogusazul.end_locked.Constants;
import io.github.amogusazul.end_locked.registry.RegistrationProvider;
import io.github.amogusazul.end_locked.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public class Items {

    /**
     * The provider for items
     */
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM_REGISTRY, Constants.MOD_ID);
    public static final RegistryObject<Item> EXAMPLE = ITEMS.register("example", () -> new Item(new Item.Properties().fireResistant().stacksTo(12)));

    public static void loadClass() {}
}
