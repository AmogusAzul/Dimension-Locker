package io.github.amogusazul.dimension_locker.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DamageResistant;

public class PebbleItem extends Item {

    private final ResourceKey<Level> pebbleDimension;

    public PebbleItem(Item.Properties properties, ResourceKey<Level> dimension) {

        super(properties);

        pebbleDimension = dimension;
    }

    public ResourceKey<Level> getPebbleDimension() {
        return pebbleDimension;
    }

    public static Properties getDefaultProperties(){
        return new Properties()
                .stacksTo(1)
                .fireResistant()
                .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.IS_EXPLOSION))
                .component(DataComponents.DAMAGE_RESISTANT, new DamageResistant(DamageTypeTags.NO_KNOCKBACK))
                .rarity(Rarity.EPIC);
    }
}