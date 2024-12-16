package io.github.amogusazul.dimension_locker.item;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import io.github.amogusazul.dimension_locker.registry.CommonRegistryInterface;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public record DimensionLockerItems() implements CommonRegistryInterface {
    public static final Supplier<Item> NETHER_PEBBLE = CommonRegistryInterface.registerItem(
            "nether_pebble", Item::new, getDefaultPebbleProperties(Level.NETHER));
    public static final Supplier<Item> END_PEBBLE = CommonRegistryInterface.registerItem(
            "end_pebble", Item::new, getDefaultPebbleProperties(Level.END));

    public static Item.Properties getDefaultPebbleProperties(ResourceKey<Level> dimension){
        return new Item.Properties()
                .stacksTo(1)
                .fireResistant()
                .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION))
                .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.NO_KNOCKBACK))
                .rarity(Rarity.EPIC);
                //.component(DimensionLockerDataComponents.DIMENSION, dimension);
    }

    public static void loadItems() {
    }

}
