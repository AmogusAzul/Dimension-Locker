package io.github.amogusazul.dimension_locker.mixin;

import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin for PlayerEnderChestContainer to expose its private field 'activeChest'.
 */
@Mixin(PlayerEnderChestContainer.class)
public interface  PlayerEnderChestContainerAccessor {

    @Accessor("activeChest")
    EnderChestBlockEntity getActiveChest();

}
