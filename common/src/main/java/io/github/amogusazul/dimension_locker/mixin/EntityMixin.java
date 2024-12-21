package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import io.github.amogusazul.dimension_locker.game_rule.DimensionLockerGameRules;
import io.github.amogusazul.dimension_locker.util.DataHandler;
import io.github.amogusazul.dimension_locker.util.DimensionLockerSavedData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract Entity teleport(TeleportTransition p_379899_);

    @Redirect(method = "handlePortal",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/world/entity/Entity;"
            ))
    private Entity wrapTeleport(Entity instance, TeleportTransition flag){
        DataHandler dataHandler = new DataHandler();
        DimensionLockerSavedData dimensionSD = dataHandler.getDimensionData(flag.newLevel());

        if (dimensionSD.isLocked()) {

            if (instance instanceof Player player) {

                if (Objects.requireNonNull(player.getServer()).getGameRules()
                        .getBoolean(DimensionLockerGameRules.OPERATOR_CAN_ENTER_LOCKED_DIMENSION)
                        && Objects.requireNonNull(player.getServer())
                        .getProfilePermissions(player.getGameProfile()) >= 2){
                    return dimension_locker$handleTeleport(flag, true);
                }

                if (player.getInventory().contains(
                        (stack) -> dimension_locker$isPebble(stack, flag.newLevel().dimension())
                )){
                    return dimension_locker$handleTeleport(flag, true);
                }

            }

            if (instance instanceof Mob mob) {

                final boolean[] hasPebble = {false};
                mob.getAllSlots().forEach(
                        (stack) -> {
                            hasPebble[0] = hasPebble[0] || dimension_locker$isPebble(stack, flag.newLevel().dimension());
                        }
                );
                return dimension_locker$handleTeleport(flag, hasPebble[0]);

            }

            return dimension_locker$handleTeleport(flag, false);
        }
        return dimension_locker$handleTeleport(flag, true);
    }

    @Unique
    private Entity dimension_locker$handleTeleport(TeleportTransition flag, boolean doTeleport){
        if (doTeleport){
            return this.teleport(flag);
        }
        return (Entity)(Object)this;
    }

    @Unique
    private boolean dimension_locker$isPebble(ItemStack stack, ResourceKey<Level> dimension){

        return stack.has(DimensionLockerDataComponents.DIMENSION.getType()) && stack.get(DimensionLockerDataComponents.DIMENSION.getType()) == dimension;
    }

}
