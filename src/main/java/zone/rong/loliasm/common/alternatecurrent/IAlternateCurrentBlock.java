package zone.rong.loliasm.common.alternatecurrent;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IAlternateCurrentBlock {

    default boolean emitsWeakPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing facing) {
        return false;
    }

    default boolean emitsStrongPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing facing) {
        return false;
    }

}
