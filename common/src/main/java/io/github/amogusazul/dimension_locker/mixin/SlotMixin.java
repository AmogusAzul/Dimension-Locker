package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.stream.Stream;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Final
    @Shadow
    public Container container;

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void notInEnderChestTagCheck(ItemStack stack, CallbackInfoReturnable<Boolean> cir){

        if (this.container instanceof PlayerEnderChestContainer) {
            if (dimension_locker$CheckIfCantEnterEnderChest(stack)) {
                dimension_locker$rejectItem(cir);
            }

            DataComponentMap stackComponents = stack.getComponents();

            if (stackComponents.has(DataComponents.CONTAINER) &&
                    dimension_locker$isThereInvalidItem(Objects.requireNonNull(stackComponents.get(
                            DataComponents.CONTAINER)).stream())){
                dimension_locker$rejectItem(cir);
            }

            if (stackComponents.has(DataComponents.BUNDLE_CONTENTS) &&
                    dimension_locker$isThereInvalidItem(Objects.requireNonNull(stackComponents.get(
                            DataComponents.BUNDLE_CONTENTS)).itemCopyStream())) {
                dimension_locker$rejectItem(cir);
            }

        }
    }

    @Unique
    private void dimension_locker$rejectItem(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }

    @Unique
    private boolean dimension_locker$isThereInvalidItem(Stream<ItemStack> stackStream){
        return stackStream.map(this::dimension_locker$CheckIfCantEnterEnderChest)
                .reduce(false, (result, element) -> result || element);
    }

    @Unique
    private boolean dimension_locker$CheckIfCantEnterEnderChest(ItemStack stack){
        return stack.getComponents().has(DimensionLockerDataComponents.CANT_ENTER_ENDER_CHEST.getType());
    }
}
