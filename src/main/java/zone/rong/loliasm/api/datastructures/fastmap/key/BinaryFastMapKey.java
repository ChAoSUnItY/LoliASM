package zone.rong.loliasm.api.datastructures.fastmap.key;

import com.google.common.base.Preconditions;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.MathHelper;

/**
 * A bitmask-based implementation of a FastMapKey. This reduces the density of data in the value matrix, but allows
 * accessing values with only some bitwise operations, which are much faster than integer division
 */
public class BinaryFastMapKey<T extends Comparable<T>> extends FastMapKey<T> {

    private final byte firstBitInValue;
    private final byte firstBitAfterValue;

    public BinaryFastMapKey(IProperty<T> property, int mapFactor) {
        super(property);
        Preconditions.checkArgument(mapFactor != 0 && (mapFactor & mapFactor - 1) == 0); // MathHelper#isPowerOfTwo
        int numValues = numValues();
        final int addedFactor = MathHelper.smallestEncompassingPowerOfTwo(numValues);
        Preconditions.checkState(numValues <= addedFactor);
        Preconditions.checkState(addedFactor < 2 * numValues);
        final int setBitInBaseFactor = MathHelper.log2(mapFactor);
        final int setBitInAddedFactor = MathHelper.log2(addedFactor);
        Preconditions.checkState(setBitInBaseFactor + setBitInAddedFactor <= 31);
        firstBitInValue = (byte) setBitInBaseFactor;
        firstBitAfterValue = (byte) (setBitInBaseFactor + setBitInAddedFactor);
    }

    @Override
    public T getValue(int mapIndex) {
        final int clearAbove = mapIndex & lowestNBits(firstBitAfterValue);
        return byInternalIndex(clearAbove >>> firstBitInValue);
    }

    @Override
    public int replaceIn(int mapIndex, T newValue) {
        final int keepMask = ~lowestNBits(firstBitAfterValue) | lowestNBits(firstBitInValue);
        return (keepMask & mapIndex) | toPartialMapIndex(newValue);
    }

    @Override
    public int toPartialMapIndex(Comparable<?> value) {
        return getInternalIndex(value) << firstBitInValue;
    }

    @Override
    public int getFactorToNext() {
        return 1 << (firstBitAfterValue - firstBitInValue);
    }

    private int lowestNBits(byte n) {
        if (n >= Integer.SIZE) {
            return -1;
        } else {
            return (1 << n) - 1;
        }
    }
}
