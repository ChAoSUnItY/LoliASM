package chaos.unity.loliasm;

import chaos.unity.loliasm.common.java.JavaFixes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class LoliASM implements ModInitializer {
    @Override
    public void onInitialize() {
        registerWorldLoadingEvent();
    }

    private void registerWorldLoadingEvent() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            JavaFixes.INSTANCE.run();
        });
    }
}
