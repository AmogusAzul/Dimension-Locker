package io.github.amogusazul.dimension_locker.game_rule;

import com.google.common.collect.Maps;
import io.github.amogusazul.dimension_locker.registry.CommonRegistryInterface;
import net.minecraft.world.level.GameRules;

import java.util.Comparator;
import java.util.Map;

public class DimensionLockerGameRules {

    public static final GameRules.Key<GameRules.BooleanValue> OPERATOR_CAN_ENTER_LOCKED_DIMENSION =
            CommonRegistryInterface.registerBooleanGameRule("operatorCanEnterLockedDimension", GameRules.Category.MISC, true);


    public static void registerGameRules(){

    }
}
