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

    @ModifyVariable(method = "putByteArray*", at = @At(value = "LOAD"), argsOnly = true, index = 1)
    private String modifyPutByteArrayStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putDouble", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutDoubleStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putFloat", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutFloatStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putInt", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutIntStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putIntArray*", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutIntArrayStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putLong", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutLongStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putLongArray*", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutLongArrayStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putShort", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutShortStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putString", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutStringStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }

    @ModifyVariable(method = "putUuid", at = @At("LOAD"), argsOnly = true, index = 1)
    private String modifyPutUuidStringKey(String key) {
        return LoliStringPool.canonicalize(key, LoliStringPool.NBT_COMPOUND_KEY_ID, false);
    }
}
