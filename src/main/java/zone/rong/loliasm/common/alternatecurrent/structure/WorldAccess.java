package zone.rong.loliasm.common.alternatecurrent.structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.Constants;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentBlock;

public class WorldAccess {

    public static final IBlockState AIR = Blocks.AIR.getDefaultState();

    private static final int Y_MIN = 0;
    private static final int Y_MAX = 256;

    private final WireBlock wireBlock;
    private final WorldServer world;
    private final WireHandler wireHandler;

    public WorldAccess(WireBlock wireBlock, WorldServer world) {
        this.wireBlock = wireBlock;
        this.world = world;
        this.wireHandler = new WireHandler(this.wireBlock, this);
    }

    public WireHandler getWireHandler() {
        return wireHandler;
    }

    /**
     * A slightly optimized version of World.getBlockState.
     */
    public IBlockState getBlockState(BlockPos pos) {
        int y = pos.getY();
        if (y < Y_MIN || y >= Y_MAX) {
            return AIR;
        }
        int x = pos.getX();
        int z = pos.getZ();
        Chunk chunk = world.getChunk(x >> 4, z >> 4);
        ExtendedBlockStorage section = chunk.getBlockStorageArray()[y >> 4];
        if (section == Chunk.NULL_BLOCK_STORAGE) {
            return AIR;
        }
        return section.get(x & 15, y & 15, z & 15);
    }

    /**
     * An optimized version of World.setBlockState. Since this method is
     * only used to update redstone wire block states, lighting checks,
     * height map updates, and block entity updates are omitted.
     */
    public boolean setWireState(BlockPos pos, IBlockState state) {
        if (!wireBlock.isOf(state)) {
            return false;
        }
        int y = pos.getY();
        if (y < Y_MIN || y >= Y_MAX) {
            return false;
        }
        int x = pos.getX();
        int z = pos.getZ();
        Chunk chunk = world.getChunk(x >> 4, z >> 4);
        ExtendedBlockStorage section = chunk.getBlockStorageArray()[y >> 4];
        if (section == null) {
            return false; // we should never get here
        }
        x &= 15;
        y &= 15;
        z &= 15;
        IBlockState prevState = section.get(x, y, z);
        if (state == prevState) {
            return false;
        }
        section.set(x, y, z, state);
        // notify clients of the IBlockState change
        world.getPlayerChunkMap().markBlockForUpdate(pos);
        // mark the chunk for saving
        chunk.markDirty();
        return true;
    }

    public boolean breakBlock(BlockPos pos, IBlockState state) {
        state.getBlock().dropBlockAsItem(world, pos, state, 0);
        return world.setBlockState(pos, AIR, Constants.BlockFlags.SEND_TO_CLIENTS);
    }

    public void updateObserver(BlockPos pos, Block fromBlock, BlockPos fromPos) {
        IBlockState state = getBlockState(pos);
        Block block = state.getBlock();
        block.observedNeighborChange(state, world, pos, fromBlock, fromPos);
    }

    public void updateNeighborBlock(BlockPos pos, BlockPos fromPos, Block fromBlock) {
        getBlockState(pos).neighborChanged(world, pos, fromBlock, fromPos);
    }

    public void updateNeighborBlock(BlockPos pos, IBlockState state, BlockPos fromPos, Block fromBlock) {
        state.neighborChanged(world, pos, fromBlock, fromPos);
    }

    public boolean isConductor(BlockPos pos) {
        return getBlockState(pos).isNormalCube();
    }

    public boolean isConductor(BlockPos pos, IBlockState state) {
        return state.isNormalCube();
    }

    public boolean emitsWeakPowerTo(BlockPos pos, IBlockState state, EnumFacing dir) {
        return ((IAlternateCurrentBlock) state.getBlock()).emitsWeakPowerTo(world, pos, state, dir);
    }

    public boolean emitsStrongPowerTo(BlockPos pos, IBlockState state, EnumFacing dir) {
        return ((IAlternateCurrentBlock) state.getBlock()).emitsStrongPowerTo(world, pos, state, dir);
    }

    public int getWeakPowerFrom(BlockPos pos, IBlockState state, EnumFacing dir) {
        return state.getWeakPower(world, pos, dir);
    }

    public int getStrongPowerFrom(BlockPos pos, IBlockState state, EnumFacing dir) {
        return state.getStrongPower(world, pos, dir);
    }

    public boolean shouldBreak(BlockPos pos, IBlockState state) {
        return !state.getBlock().isReplaceable(world, pos);
    }
}
