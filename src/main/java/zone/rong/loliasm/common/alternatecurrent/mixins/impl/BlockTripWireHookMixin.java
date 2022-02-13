package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

@Mixin(BlockTripWireHook.class)
public class BlockTripWireHookMixin implements IAlternateCurrentBlock {

    @Override
    public boolean emitsWeakPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return true;
    }

    @Override
    public boolean emitsStrongPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return state.getValue(BlockTripWireHook.FACING) == dir;
    }

}
