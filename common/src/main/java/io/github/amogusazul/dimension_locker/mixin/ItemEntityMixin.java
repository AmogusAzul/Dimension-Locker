package io.github.amogusazul.dimension_locker.mixin;


import io.github.amogusazul.dimension_locker.util.DimensionLockerTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getItem();


    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    private void wrapDiscardCall(ItemEntity instance) {
        if (!CheckIfIsUnDespawnable(this.getItem())) { // Add your wrapper condition
            ((ItemEntity)(Object)this).discard(); // Call the original discard method
        }
    }

    private boolean CheckIfIsUnDespawnable(ItemStack stack){
        return stack.is(DimensionLockerTags.Items.UN_DESPAWNABLE);
    }
}
