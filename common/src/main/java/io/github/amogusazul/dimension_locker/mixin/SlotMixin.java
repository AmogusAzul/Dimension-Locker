package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Final
    @Shadow
    public Container container;

    @Unique
    private List<ItemStack> dimension_locker$faultyBuffer = Arrays.asList(new ItemStack[2]);

    /*
        Mixin to prevent any item with the DataComponent `DimensionLockerDataComponents.CANT_ENTER_ENDER_CHEST` from entering and ender chest and giving feedback to the player trying to.

     */

    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    private void notInEnderChestTagCheck(ItemStack stack, CallbackInfoReturnable<Boolean> cir){

        if (this.container instanceof PlayerEnderChestContainer enderChestContainer) {

            if (dimension_locker$CheckIfCantEnterEnderChest(stack)) {

                dimension_locker$itemRejectedFeedback(
                        Component.translatable(
                                null == dimension_locker$faultyBuffer.getFirst() ?
                                        "feedback.dimension_locker.singleItemCantEnterEnderChest" :
                                        "feedback.dimension_locker.containerCantEnterEnderChest",
                                stack.getDisplayName().getString(),
                                Component.translatable("block.minecraft.ender_chest"),
                                null == dimension_locker$faultyBuffer.getFirst() ?
                                "" : dimension_locker$faultyBuffer.getFirst().getDisplayName().getString()
                        ),
                        enderChestContainer);

                dimension_locker$faultyBuffer = Arrays.asList(new ItemStack[2]);

                dimension_locker$rejectItem(cir);
            }

        }
    }

    @Unique
    private void dimension_locker$rejectItem(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }

    @Unique
    private boolean dimension_locker$CheckIfCantEnterEnderChest(ItemStack stack){

        boolean cantEnter = false;

        cantEnter = cantEnter || stack.has(DimensionLockerDataComponents.CANT_ENTER_ENDER_CHEST.getType());

        Stream<ItemStack> stream = null;

        if (stack.has(DataComponents.BUNDLE_CONTENTS)){
            stream = Objects.requireNonNull(stack.get(DataComponents.BUNDLE_CONTENTS)).itemCopyStream();
        }
        if (stack.has(DataComponents.CONTAINER)){
            stream = Objects.requireNonNull(stack.get(DataComponents.CONTAINER)).stream();
        }

        if (stream != null) {
            boolean hasGuiltyItem =
                    stream
                    .map(this::dimension_locker$CheckIfCantEnterEnderChest)
                    .reduce(false, (s, b) -> s || b);

            cantEnter = cantEnter || hasGuiltyItem;
        }

        if (cantEnter) {
            this.dimension_locker$faultyBuffer.set(0, this.dimension_locker$faultyBuffer.get(1));
            this.dimension_locker$faultyBuffer.set(1, stack);
            return true;

        }
        return false;
    }

    @Unique
    private void dimension_locker$itemRejectedFeedback(Component text, PlayerEnderChestContainer container){

        EnderChestBlockEntity blockEntity = ((PlayerEnderChestContainerAccessor) container).getActiveChest();
        Level level =  Objects.requireNonNull(blockEntity.getLevel());


       level.players().forEach(
                (player) -> {
                    if (player.getEnderChestInventory().isActiveChest(blockEntity)) {
                        player.displayClientMessage(text, true);
                        level.playSound(null, blockEntity.getBlockPos(), SoundEvents.PLAYER_TELEPORT, SoundSource.MASTER, 0.1f, 1f);
                    }});
    }
}
