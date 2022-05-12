package chaos.unity.loliasm.mixin;

import chaos.unity.loliasm.api.LoliStringPool;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(NbtCompound.class)
public abstract class NbtCompoundMixin {
    @ModifyVariable(method = "put", at = @At(value = "LOAD"), argsOnly = true, index = 1)
    private String modifyPutStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putByte", at = @At(value = "LOAD"), argsOnly = true, index = 1)
    private String modifyPutByteStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putBoolean", at = @At(value = "LOAD"), argsOnly = true, index = 1)
    private String modifyPutBooleanStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putByteArray*", at = @At(value = "LOAD"), argsOnly = true, index = 1, print = true)
    private String modifyPutByteArrayStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }
}
