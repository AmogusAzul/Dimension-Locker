package io.github.amogusazul.dimension_locker.mixin;


import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Unique
    private int dimension_locker$modifiedLifetime = 6000;
    @Unique
    private boolean dimension_locker$pebbleChecked = false;

    @Shadow public abstract ItemStack getItem();

    @Shadow private int pickupDelay;

    @Shadow @Final private static int INFINITE_PICKUP_DELAY;

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    public void modifyLifetime(ItemEntity instance) {

        if (!dimension_locker$pebbleChecked) {

            dimension_locker$modifiedLifetime =
                    dimension_locker$CheckIfIsUnDespawnable(getItem()) &&
                            pickupDelay != INFINITE_PICKUP_DELAY ?
                    Integer.MAX_VALUE : 6000;
            dimension_locker$pebbleChecked = true;
        }

        if (instance.getAge() >= dimension_locker$modifiedLifetime) {
            instance.discard();
        }

    }


    @Unique
    private boolean dimension_locker$CheckIfIsUnDespawnable(ItemStack stack){
        return stack.has(DimensionLockerDataComponents.UN_DESPAWNABLE);
    }
}

