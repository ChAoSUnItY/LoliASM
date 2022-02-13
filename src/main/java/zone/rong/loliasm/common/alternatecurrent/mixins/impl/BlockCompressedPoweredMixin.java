package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

@Mixin(BlockCompressedPowered.class)
public class BlockCompressedPoweredMixin implements IAlternateCurrentBlock {

    @Override
    public boolean emitsWeakPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing facing) {
        return true;
    }

}
