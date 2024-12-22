package io.github.amogusazul.dimension_locker.mixin;

import io.github.amogusazul.dimension_locker.data_component.DimensionLockerDataComponents;
import io.github.amogusazul.dimension_locker.game_rule.DimensionLockerGameRules;
import io.github.amogusazul.dimension_locker.util.DataHandler;
import io.github.amogusazul.dimension_locker.util.DimensionLockerSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PortalProcessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract Entity teleport(TeleportTransition p_379899_);

    @Shadow public abstract Vec3 position();

    @Shadow public PortalProcessor portalProcess;

    @Shadow public abstract AABB getBoundingBox();

    @Shadow public abstract void move(MoverType type, Vec3 pos);

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

        Vec3 vector =
                this.position().add(0, this.getBoundingBox().getYsize()/2, 0)
                        .subtract(
                                Vec3.atCenterOf(
                                        this.portalProcess.getEntryPosition()
                                ));
        double strength = Math.sqrt(
                Math.max(Math.max(
                        Math.abs(vector.x),
                        Math.abs(vector.y)),
                        Math.abs(vector.z)));


        System.out.println("################################### position "+vector);

        instance.move(MoverType.PLAYER, vector.multiply(100, 100, 100));

        if (instance instanceof Player player){
            ResourceLocation resourceLocation = flag.newLevel().dimension().location();

            player.displayClientMessage(
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
