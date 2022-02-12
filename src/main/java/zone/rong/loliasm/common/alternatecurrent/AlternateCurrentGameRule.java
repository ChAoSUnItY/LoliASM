package zone.rong.loliasm.common.alternatecurrent;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AlternateCurrentGameRule {

    public static final String KEY = "alternateCurrent";

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        event.getWorld().getGameRules().addGameRule(KEY, "true", GameRules.ValueType.BOOLEAN_VALUE);
    }

    public static boolean isEnabled(World world) {
        return world.getGameRules().getBoolean(KEY);
    }

}
