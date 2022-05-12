package chaos.unity.loliasm.mixin;

import chaos.unity.loliasm.api.LoliStringPool;
import net.minecraft.nbt.NbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(NbtString.class)
public abstract class NbtStringMixin {
    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true, name = "arg1")
    private static String redirectStringLoad(String value) {
        return LoliStringPool.canonicalize(value, LoliStringPool.NBT_STRING_ID, false);
    }
}
