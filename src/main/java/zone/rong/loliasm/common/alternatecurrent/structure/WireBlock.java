package zone.rong.loliasm.common.alternatecurrent.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * This interface should be implemented by each wire block type.
 * While Vanilla only has one wire block type, they could add
 * more in the future, and any mods that add more wire block
 * types that wish to take advantage of Alternate Current's
 * performance improvements should have those wire blocks
 * implement this interface.
 *
 * @author Space Walker
 */
public interface WireBlock {

    default Block asBlock() {
        return (Block) this;
    }

    default boolean isOf(IBlockState state) {
        return asBlock() == state.getBlock();
    }

    /**
     * The lowest possible power level a wire can have.
     */
    int getMinPower();

    /**
     * The largest possible power level a wire can have.
     */
    int getMaxPower();

    /**
     * The drop in power level from one wire to the next.
     */
    int getPowerStep();

    default int clampPower(int power) {
        return MathHelper.clamp(power, getMinPower(), getMaxPower());
    }

    /**
     * Return the power level of the given wire based on its
     * location and block state.
     */
    int getPower(WorldAccess world, BlockPos pos, IBlockState state);

    /**
     * Return a block state that holds the given new power level.
     */
    IBlockState updatePowerState(WorldAccess world, BlockPos pos, IBlockState state, int power);

    /**
     * Find the connections between the given WireNode and
     * neighboring WireNodes.
     */
    void findWireConnections(WireNode wire, WireHandler.NodeProvider nodeProvider);

}
