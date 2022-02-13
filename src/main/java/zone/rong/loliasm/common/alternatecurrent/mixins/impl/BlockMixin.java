package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

@Mixin(Block.class)
public class BlockMixin implements IAlternateCurrentBlock { }
