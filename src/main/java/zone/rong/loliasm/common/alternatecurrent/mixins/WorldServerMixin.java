package zone.rong.loliasm.common.alternatecurrent.mixins;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentWorld;
import zone.rong.loliasm.common.alternatecurrent.structure.WireBlock;
import zone.rong.loliasm.common.alternatecurrent.structure.WorldAccess;

import java.util.Map;

@Mixin(WorldServer.class)
public class WorldServerMixin implements IAlternateCurrentWorld {

    private final Map<WireBlock, WorldAccess> alternateCurrent$accesses = new Object2ObjectOpenHashMap<>();

    @Override
    public WorldAccess getAccess(WireBlock wireBlock) {
        return alternateCurrent$accesses.computeIfAbsent(wireBlock, key -> new WorldAccess(wireBlock, (WorldServer) (Object) this));
    }

}
