package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.util.DimensionLockerTags;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Final
    @Shadow
    public Container container;

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void notInEnderChestTagCheck(ItemStack stack, CallbackInfoReturnable<Boolean> cir){

        if (this.container instanceof PlayerEnderChestContainer) {
            if (stack.is(DimensionLockerTags.Items.CANT_ENTER_ENDER_CHEST)) {
                rejectItem(cir);
            }

            DataComponentMap stackComponents = stack.getComponents();

            if (stackComponents.has(DataComponents.CONTAINER) &&
                    isThereInvalidItem(stackComponents.get(
                            DataComponents.CONTAINER).stream())){
                rejectItem(cir);
            }

            if (stackComponents.has(DataComponents.BUNDLE_CONTENTS) &&
                    isThereInvalidItem(stackComponents.get(
                            DataComponents.BUNDLE_CONTENTS).itemCopyStream())) {
                rejectItem(cir);
            }

        }
    }

    private void rejectItem(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }

    private boolean isThereInvalidItem(Stream<ItemStack> stackStream){
        return stackStream.map((itemStack) -> itemStack.is(DimensionLockerTags.Items.CANT_ENTER_ENDER_CHEST))
                .reduce(false, (result, element) -> result || element);
    }
}
