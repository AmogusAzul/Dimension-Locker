package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import io.github.amogusazul.dimension_locker.game_rule.DimensionLockerGameRules;
import io.github.amogusazul.dimension_locker.util.DataHandler;
import io.github.amogusazul.dimension_locker.util.DimensionLockerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract Entity teleport(TeleportTransition p_379899_);


    @Shadow public PortalProcessor portalProcess;

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
                    return dimension_locker$handleTeleport(flag, true, player);
                }

                if (player.getInventory().contains(
                        (stack) -> dimension_locker$isPebble(stack, flag.newLevel().dimension())
                )){
                    return dimension_locker$handleTeleport(flag, true, player);
                }

            }

            if (instance instanceof Mob mob) {

                final boolean[] hasPebble = {false};
                mob.getAllSlots().forEach(
                        (stack) -> hasPebble[0] = hasPebble[0] || dimension_locker$isPebble(stack, flag.newLevel().dimension())
                );
                return dimension_locker$handleTeleport(flag, hasPebble[0], instance);

            }

            return dimension_locker$handleTeleport(flag, false, instance);
        }
        return dimension_locker$handleTeleport(flag, true, instance);
    }

    @Unique
    private Entity dimension_locker$handleTeleport(TeleportTransition flag, boolean doTeleport, Entity instance){
        if (doTeleport){
            return this.teleport(flag);
        }

        dimension_locker$pushEntity(instance, flag);


        dimension_locker$feedbackPlayer(instance, flag);

        return (Entity)(Object)this;
    }

    @Unique
    private void dimension_locker$feedbackPlayer(Entity e, TeleportTransition flag){
        if (e instanceof Player p){
            ResourceLocation resourceLocation = flag.newLevel().dimension().location();

            p.displayClientMessage(
                    Component.translatable(
                            "feedback.dimension_locker.dimensionRejected",
                            Component.translatable(
                                    "dimension."+
                                            resourceLocation.getNamespace()+
                                            "."+resourceLocation.getPath())
                    ),
                    true);
        }

        return (Entity)(Object)this;
    }

    @Unique
    private boolean dimension_locker$isPebble(ItemStack stack, ResourceKey<Level> dimension){

        return stack.has(DimensionLockerDataComponents.DIMENSION.getType()) && stack.get(DimensionLockerDataComponents.DIMENSION.getType()) == dimension;
    }

}
