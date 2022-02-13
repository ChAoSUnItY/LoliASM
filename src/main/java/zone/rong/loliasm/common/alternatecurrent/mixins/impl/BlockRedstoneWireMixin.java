package zone.rong.loliasm.common.alternatecurrent.mixins.impl;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.rong.loliasm.common.alternatecurrent.AlternateCurrentGameRule;
import zone.rong.loliasm.common.alternatecurrent.IAlternateCurrentWorld;
import zone.rong.loliasm.common.alternatecurrent.structure.*;

@Mixin(BlockRedstoneWire.class)
public class BlockRedstoneWireMixin implements WireBlock {

    @Inject(method = "updateSurroundingRedstone", at = @At("HEAD"), cancellable = true)
    private void onUpdate(World world, BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (AlternateCurrentGameRule.isEnabled(world)) {
            ci.cancel();
        }
    }

    @Inject(method = "onBlockAdded", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/block/BlockRedstoneWire;updateSurroundingRedstone(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;"), cancellable = true)
    private void onBlockAdded(World world, BlockPos pos, IBlockState facing, CallbackInfo ci) {
        if (AlternateCurrentGameRule.isEnabled(world)) {
            ((IAlternateCurrentWorld) world).getAccess(this).getWireHandler().onWireAdded(pos);
        }
    }

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/block/BlockRedstoneWire;updateSurroundingRedstone(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;"), cancellable = true)
    private void onBreakBlock(World world, BlockPos pos, IBlockState facing, CallbackInfo ci) {
        if (AlternateCurrentGameRule.isEnabled(world)) {
            ((IAlternateCurrentWorld) world).getAccess(this).getWireHandler().onWireRemoved(pos);
        }
    }

    @Inject(method = "neighborChanged", at = @At("HEAD"), cancellable = true)
    private void onNeighborUpdate(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, CallbackInfo ci) {
        if (AlternateCurrentGameRule.isEnabled(world)) {
            if (!world.isRemote) {
                ((IAlternateCurrentWorld) world).getAccess(this).getWireHandler().onWireUpdated(pos);
            }
            ci.cancel();
        }
    }

    @Override
    public int getMinPower() {
        return 0;
    }

    @Override
    public int getMaxPower() {
        return 15;
    }

    @Override
    public int getPowerStep() {
        return 1;
    }

    @Override
    public int getPower(WorldAccess world, BlockPos pos, IBlockState state) {
        return state.getValue(BlockRedstoneWire.POWER);
    }

    @Override
    public IBlockState updatePowerState(WorldAccess world, BlockPos pos, IBlockState state, int power) {
        return state.withProperty(BlockRedstoneWire.POWER, power);
    }

    @Override
    public void findWireConnections(WireNode wire, WireHandler.NodeProvider nodeProvider) {
        wire.connections.set((connections, iDir) -> {
            Node neighbor = nodeProvider.getNeighbor(wire, iDir);
            if (neighbor.isWire()) {
                connections.add(neighbor.asWire(), iDir, true, true);
                return;
            }
            boolean sideIsConductor = neighbor.isConductor();
            if (!sideIsConductor) {
                Node node = nodeProvider.getNeighbor(neighbor, WireHandler.Facings.DOWN);
                if (node.isWire()) {
                    connections.add(node.asWire(), iDir, true, nodeProvider.getNeighbor(wire, WireHandler.Facings.DOWN).isConductor());
                }
            }
            if (!nodeProvider.getNeighbor(wire, WireHandler.Facings.UP).isConductor()) {
                Node node = nodeProvider.getNeighbor(neighbor, WireHandler.Facings.UP);
                if (node.isWire()) {
                    connections.add(node.asWire(), iDir, sideIsConductor, true);
                }
            }
        });
    }

}
