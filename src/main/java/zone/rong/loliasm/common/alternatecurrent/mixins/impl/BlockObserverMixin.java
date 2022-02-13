package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.BlockObserver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

@Mixin(BlockObserver.class)
public class BlockObserverMixin implements IAlternateCurrentBlock {

    @Override
    public boolean emitsWeakPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return state.getValue(BlockObserver.FACING) == dir;
    }

    @Override
    public boolean emitsStrongPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return state.getValue(BlockObserver.FACING) == dir;
    }

}
