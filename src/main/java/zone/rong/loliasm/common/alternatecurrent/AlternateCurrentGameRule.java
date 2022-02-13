package zone.rong.loliasm.common.alternatecurrent;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zone.rong.loliasm.LoliLogger;
import zone.rong.loliasm.config.LoliConfig;

public class AlternateCurrentGameRule {

    public static final String KEY = "alternateCurrent";

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        GameRules gameRules = event.getWorld().getGameRules();
        if (!gameRules.hasRule(KEY)) {
            LoliLogger.instance.info("Setting {} {} for {}", KEY,LoliConfig.instance.autoAlternateCurrent, event.getWorld().provider.getDimensionType().getName());
            gameRules.addGameRule(KEY, Boolean.toString(LoliConfig.instance.autoAlternateCurrent), GameRules.ValueType.BOOLEAN_VALUE);
        }
    }

    public static boolean isEnabled(World world) {
        return world.getGameRules().getBoolean(KEY);
    }

}
