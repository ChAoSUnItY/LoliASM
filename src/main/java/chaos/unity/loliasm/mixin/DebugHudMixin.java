package chaos.unity.loliasm.mixin;

import chaos.unity.loliasm.api.LoliStringPool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(DebugHud.class)
@Environment(EnvType.CLIENT)
public abstract class DebugHudMixin {
    @Shadow
    protected abstract List<String> getLeftText();

    @Redirect(method = "renderLeftText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;getLeftText()Ljava/util/List;", ordinal = 0))
    private List<String> getLeftText(DebugHud instance) {
        var leftText = getLeftText();

        if (!leftText.get(leftText.size() - 1).equals("")) {
            leftText.add("");
        }

        LoliStringPool.foreach((id, pool) -> {
            int size = LoliStringPool.getSize(id);
            long deduplicatedCount = LoliStringPool.getDeduplicatedCount(id);

            leftText.add(String.format("%s%s%s String Pool %d: %s strings processed. %s unique, %s deduplicated.", Formatting.AQUA, "<LoliASM>", Formatting.RESET, id, deduplicatedCount, size, deduplicatedCount - size));
        });

        return leftText;
    }
}
