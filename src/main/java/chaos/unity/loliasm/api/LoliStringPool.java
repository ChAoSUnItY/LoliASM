package chaos.unity.loliasm.api;

import chaos.unity.loliasm.LoliLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Locale;
import java.util.function.BiConsumer;

public class LoliStringPool {
    public static final int FILE_PERMISSIONS_ID = 1;
    public static final int NBT_STRING_ID = 2;
    public static final int NBT_COMPOUND_KEY_ID = 3;
    private static final Int2ObjectMap<InternalPool> POOLS = new Int2ObjectArrayMap<>();

    static {
        establishPool(-1, 12288, "", " ");
        establishPool(NBT_STRING_ID, 4096);
        establishPool(NBT_COMPOUND_KEY_ID, 2048);
        POOLS.defaultReturnValue(POOLS.get(-1));
    }

    public static void establishPool(int poolId, int expectedSize, String... startingValues) {
        if (POOLS.containsKey(poolId))
            return;
        POOLS.put(poolId, new InternalPool(poolId, expectedSize, startingValues));
    }

    public static void foreach(BiConsumer<? super Integer, ? super InternalPool> functor) {
        POOLS.forEach(functor);
    }

    public static InternalPool purgePool(int poolId) {
        return POOLS.remove(poolId);
    }

    public static int getSize() {
        return POOLS.defaultReturnValue().internalPool.size();
    }

    public static int getSize(int poolId) {
        return POOLS.get(poolId).internalPool.size();
    }

    public static long getDeduplicatedCount() {
        return POOLS.defaultReturnValue().deduplicatedCount;
    }

    public static long getDeduplicatedCount(int poolId) {
        return POOLS.get(poolId).deduplicatedCount;
    }

    public static String canonicalize(String stringInstance) {
        synchronized (POOLS) {
            return POOLS.defaultReturnValue().addOrGet(stringInstance);
        }
    }

    @Unsafe
    public static String unsafe$canonicalize(String stringInstance) {
        return POOLS.defaultReturnValue().addOrGet(stringInstance);
    }

    public static String lowerCaseAndCanonicalize(String string) {
        synchronized (POOLS) {
            return POOLS.defaultReturnValue().addOrGet(string.toLowerCase(Locale.ROOT));
        }
    }

    @Unsafe
    public static String unsafe$LowerCaseAndCanonicalize(String string) {
        return POOLS.defaultReturnValue().addOrGet(string.toLowerCase(Locale.ROOT));
    }

    public static String canonicalize(String string, int poolId, boolean checkMainPool) {
        if (checkMainPool) {
            synchronized (POOLS) {
                String canonicalized = POOLS.get(poolId).internalPool.get(string);
                if (canonicalized != null) {
                    return canonicalized;
                }
            }
        }
        synchronized (POOLS) {
            return POOLS.get(poolId).addOrGet(string);
        }
    }

    @Unsafe
    public static String unsafe$Canonicalize(String string, int poolId, boolean checkMainPool) {
        if (checkMainPool) {
            String canonicalized = POOLS.get(poolId).internalPool.get(string);
            if (canonicalized != null) {
                return canonicalized;
            }
        }
        return POOLS.get(poolId).addOrGet(string);
    }

    public static class InternalPool implements AutoCloseable {
        final int id;
        final ObjectOpenHashSet<String> internalPool;

        long deduplicatedCount;

        InternalPool(int id, int expectedSize, String... entries) {
            this.id = id;
            this.internalPool = new ObjectOpenHashSet<>(expectedSize);

            for (var entry : entries)
                this.internalPool.add(entry);
        }

        String addOrGet(String stringInstance) {
            deduplicatedCount++;
            return internalPool.addOrGet(stringInstance);
        }

        @Override
        public void close() {
            LoliLogger.INSTANCE.warn("Releasing LoliStringPool {}, with {} unique string instance, deduplicated {} instances", id, internalPool.size(), deduplicatedCount);
        }
    }
}
