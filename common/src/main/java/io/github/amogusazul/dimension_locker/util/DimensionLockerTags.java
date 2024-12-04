package io.github.amogusazul.dimension_locker.util;

import io.github.amogusazul.dimension_locker.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;

public class DimensionLockerTags {

    public static class Blocks {

    }

    public static class Items {

        public static  final TagKey<Item> CANT_ENTER_ENDER_CHEST = createTag("cant_enter_ender_chest");
        public static final TagKey<Item> UN_DESPAWNABLE = createTag("un_despawnable");

        private static TagKey<Item> createTag(String name){
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }
    }

}
