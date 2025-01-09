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

    @Shadow public abstract Level level();

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

        dimension_locker$spawnParticles(instance, flag);


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
    }

    @Unique
    private void dimension_locker$pushEntity(Entity e, TeleportTransition flag){

        double PUSH_MAGNITUDE = 1.5;


        Vec3 vec = dimension_locker$getNormalizedPushVector(e, flag);

        double absY = Math.abs(vec.y);

        double SIN_OF_ANGLE = Math.sin(Math.toRadians(15));

        double newY = absY < SIN_OF_ANGLE ? Math.copySign(SIN_OF_ANGLE, vec.y) : vec.y;

        Vec3 v = new Vec3(vec.x, newY, vec.z).scale(PUSH_MAGNITUDE);

        e.push(v);

        if (e instanceof ServerPlayer sp) {
            sp.hurtMarked = true;
        }

    }

    @Unique
    private void dimension_locker$spawnParticles(Entity e, TeleportTransition flag){

        BlockPos start = this.portalProcess.getEntryPosition();

        int PARTICLES_PER_BLOCK = 100;

        Predicate<BlockPos> isPortal = pos -> e.level().getBlockState(pos).getBlock() instanceof Portal p &&
                e.level() instanceof ServerLevel sl &&
                flag.newLevel().dimension() == p.getPortalDestination(sl, e, pos).newLevel().dimension();

        Direction portalNormal = dimension_locker$getPortalNormal(e, flag);

        Vec3 particleForce = portalNormal.getUnitVec3().scale(1);

        dimension_locker$floodFillWrapper(start, isPortal,
                (pos, dir) -> {

            if (e.level() instanceof ServerLevel sl){

                for (ServerPlayer sP : sl.players()) {

                    sl.sendParticles(
                            sP,
                            ParticleTypes.REVERSE_PORTAL,
                            false,
                            pos.getX()+0.5+(particleForce.x/2),
                            pos.getY()+0.5+(particleForce.y/2),
                            pos.getZ()+0.5+(particleForce.z/2),
                            PARTICLES_PER_BLOCK,
                            (portalNormal.getAxis() != Axis.X ? 0.5 : 0)+(particleForce.x/2),
                            (portalNormal.getAxis() != Axis.Y ? 0.5 : 0)+(particleForce.y/2),
                            (portalNormal.getAxis() != Axis.Z ? 0.5 : 0)+(particleForce.z/2),
                            0.2
                    );

                }

                for (int i = 0;i<PARTICLES_PER_BLOCK;i++){
                    double ranX = e.getRandom().nextDouble();
                    double ranY = e.getRandom().nextDouble();
                    double ranZ = e.getRandom().nextDouble();



            }
                    }
                }
                );

    }

    @Unique
    private static void dimension_locker$floodFill(BlockPos pos, Direction direction, Set<BlockPos> checked, Predicate<BlockPos> isValid, BiConsumer<BlockPos, Direction> function) {

        pos = pos.relative(direction);



        if (!isValid.test(pos) || checked.contains(pos)){

            return;
        }



        checked.add(pos);

        function.accept(pos, direction);

        for (Direction d : Direction.values()){
            dimension_locker$floodFill(pos, d, checked, isValid, function);
        }

    }

    @Unique
    private static void dimension_locker$floodFillWrapper(BlockPos start, Predicate<BlockPos> predicate, BiConsumer<BlockPos, Direction> function){

        Set<BlockPos> visited = new HashSet<>();

        for (Direction d : Direction.values()) {
            dimension_locker$floodFill(start, d, visited, predicate, function);
        }
    }

    @Unique
    private Vec3 dimension_locker$getNormalizedPushVector(Entity e, TeleportTransition flag) {

        Direction portalNormal = dimension_locker$getPortalNormal(e, flag);
        Vec3 dM = e.getDeltaMovement().normalize().subtract(portalNormal.getUnitVec3()).reverse();
        return portalNormal.getUnitVec3().add(dM).normalize();
    }

    @Unique
    private Direction dimension_locker$getPortalNormal(Entity e, TeleportTransition flag){

        Direction portalNormal;

        BlockPos start = this.portalProcess.getEntryPosition();
        final AABB[] pB = {new AABB(start)}; // Use an array as a mutable wrapper

        Predicate<BlockPos> isPortal = pos -> e.level().getBlockState(pos).getBlock() instanceof Portal p &&
                e.level() instanceof ServerLevel sl &&
                flag.newLevel().dimension() == p.getPortalDestination(sl, e, pos).newLevel().dimension();

        dimension_locker$floodFillWrapper(start, isPortal,
                (pos, dir) -> {
                    switch (dir) {
                        case DOWN -> pB[0] = pB[0].setMinY(Math.min(pos.getY(), pB[0].minY));
                        case UP -> pB[0] = pB[0].setMaxY(Math.max(pos.getY() + 1, pB[0].maxY));
                        case NORTH -> pB[0] = pB[0].setMinZ(Math.min(pos.getZ(), pB[0].minZ));
                        case SOUTH -> pB[0] = pB[0].setMaxZ(Math.max(pos.getZ() + 1, pB[0].maxZ));
                        case WEST -> pB[0] = pB[0].setMinX(Math.min(pos.getX(), pB[0].minX));
                        case EAST -> pB[0] = pB[0].setMaxX(Math.max(pos.getX() + 1, pB[0].maxX));
                    }});

        double xSize = pB[0].getXsize();
        double ySize = pB[0].getYsize();
        double zSize = pB[0].getZsize();
        double minSize = Math.min(Math.min(xSize, ySize), zSize);

        if (xSize == ySize && xSize == zSize) {portalNormal = Direction.getApproximateNearest(e.position().subtract(pB[0].getCenter()));}
        else {
            Axis axis = (minSize == xSize) ? Axis.X : (minSize == ySize) ? Axis.Y : Axis.Z;
            double entityAxisPos = e.getBoundingBox().getCenter().get(axis);
            double min = pB[0].min(axis);
            double max = pB[0].max(axis);
            AxisDirection direction = Math.abs(entityAxisPos - max) > Math.abs(entityAxisPos - min) ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE;
            portalNormal = Direction.fromAxisAndDirection(axis, direction);
        }

        return portalNormal;
    }

    @Unique
    private boolean dimension_locker$isPebble(ItemStack stack, ResourceKey<Level> dimension){

        return stack.has(DimensionLockerDataComponents.DIMENSION.getType()) && stack.get(DimensionLockerDataComponents.DIMENSION.getType()) == dimension;
    }

}
