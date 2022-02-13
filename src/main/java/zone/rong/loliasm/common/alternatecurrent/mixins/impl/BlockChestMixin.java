package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

@Mixin(BlockChest.class)
public class BlockChestMixin implements IAlternateCurrentBlock {

    @Shadow @Final public BlockChest.Type chestType;

    @Override
    public boolean emitsWeakPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return chestType == BlockChest.Type.TRAP;
    }

    @Override
    public boolean emitsStrongPowerTo(World world, BlockPos pos, IBlockState state, EnumFacing dir) {
        return chestType == BlockChest.Type.TRAP && dir == EnumFacing.UP;
    }

}
