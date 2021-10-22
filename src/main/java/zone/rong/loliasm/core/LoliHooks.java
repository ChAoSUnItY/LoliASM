package zone.rong.loliasm.core;

import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMaps;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import mezz.jei.suffixtree.GeneralizedSuffixTree;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import zone.rong.loliasm.LoliASM;
import zone.rong.loliasm.LoliLogger;
import zone.rong.loliasm.api.StringPool;
import zone.rong.loliasm.bakedquad.SupportingBakedQuad;
import zone.rong.loliasm.config.LoliConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class LoliHooks {

    /*
    private static final MethodHandle STRING_BACKING_CHAR_ARRAY_GETTER = LoliReflector.resolveFieldGetter(String.class, "value");
    private static final char[] EMPTY_CHAR_ARRAY;

    static {
        char[] array;
        try {
            array = (char[]) STRING_BACKING_CHAR_ARRAY_GETTER.invokeExact("");
        } catch (Throwable throwable) {
            array = new char[0];
        }
        EMPTY_CHAR_ARRAY = array;
    }
     */

    public static <K> ObjectArraySet<K> createArraySet() {
        return new ObjectArraySet<>();
    }

    public static <K> ObjectOpenHashSet<K> createHashSet() {
        return new ObjectOpenHashSet<>();
    }

    public static <K, V> Object2ObjectArrayMap<K, V> createArrayMap() {
        return new Object2ObjectArrayMap<>();
    }

    public static <K, V> Object2ObjectOpenHashMap<K, V> createHashMap() {
        return new Object2ObjectOpenHashMap<>();
    }

    private static Set<Class<?>> classesThatCallBakedQuadCtor;
    private static Set<Class<?>> classesThatExtendBakedQuad;

    public static void inform(Class<?> clazz) {
        if (clazz == SupportingBakedQuad.class) {
            return;
        }
        if (classesThatCallBakedQuadCtor == null) {
            classesThatCallBakedQuadCtor = new ReferenceOpenHashSet<>();
        }
        if (classesThatCallBakedQuadCtor.add(clazz)) {
            LoliConfig.instance.editClassesThatCallBakedQuadCtor(clazz);
        }
        if (BakedQuad.class.isAssignableFrom(clazz)) {
            if (classesThatExtendBakedQuad == null) {
                classesThatExtendBakedQuad = new ReferenceOpenHashSet<>();
            }
            if (classesThatExtendBakedQuad.add(clazz)) {
                LoliConfig.instance.editClassesThatExtendBakedQuad(clazz);
            }
        }
    }

    public static void modCandidate$override$addClassEntry(ModCandidate modCandidate, String name, Set<String> foundClasses, Set<String> packages, ASMDataTable table) {
        String className = name.substring(0, name.lastIndexOf('.'));
        foundClasses.add(className);
        className = className.replace('/','.');
        int pkgIdx = className.lastIndexOf('.');
        if (pkgIdx > -1) {
            String pkg = StringPool.canonicalize(className.substring(0, pkgIdx));
            packages.add(pkg);
            table.registerPackage(modCandidate, pkg);
        }
    }

    public static String asmData$redirect$CtorStringsToIntern(String string) {
        return string == null ? null : string.intern();
    }

    public static /*char[]*/ String nbtTagString$override$ctor(String data) {
        /*
        if (data == null) {
            throw new NullPointerException("Null string not allowed");
        }
         */
        return StringPool.canonicalize(data);
        /*
        try {
            return (char[]) STRING_BACKING_CHAR_ARRAY_GETTER.invokeExact(data);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return EMPTY_CHAR_ARRAY;
        }
         */
    }

    public static final class JEI {

        public static final Char2ObjectMap<String> treeIdentifiers = new Char2ObjectLinkedOpenHashMap<>(7);
        private static final Char2ObjectMap<GeneralizedSuffixTree> trees = Char2ObjectMaps.synchronize(new Char2ObjectOpenHashMap<>(7, 0.99f));

        private static ExecutorService deserializingExecutor;

        static {
            treeIdentifiers.put(' ', "main");
            treeIdentifiers.put('@', "modname");
            treeIdentifiers.put('#', "tooltip");
            treeIdentifiers.put('$', "oredict");
            treeIdentifiers.put('%', "creativetab");
            treeIdentifiers.put('^', "colour");
            treeIdentifiers.put('&', "resourceId");
        }

        public static void init() {
            if (LoliASM.proxy.consistentModList) {
                File cacheFolder = new File(LoliASM.proxy.loliCachesFolder, "jei");
                cacheFolder.mkdir();
                for (Char2ObjectMap.Entry<String> entry : treeIdentifiers.char2ObjectEntrySet()) {
                    File cache = new File(cacheFolder, entry.getValue() + "_tree.bin");
                    if (!cache.exists()) {
                        continue;
                    }
                    if (deserializingExecutor == null) {
                        deserializingExecutor = Executors.newFixedThreadPool(Math.min(Runtime.getRuntime().availableProcessors() / 2, 7));
                    }
                    deserializingExecutor.submit(() -> {
                        try {
                            Stopwatch stopwatch = Stopwatch.createStarted();
                            FileInputStream fileInputStream = new FileInputStream(cache);
                            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                            trees.put(entry.getCharKey(), (GeneralizedSuffixTree) objectInputStream.readObject());
                            objectInputStream.close();
                            fileInputStream.close();
                            LoliLogger.instance.info("{} Search Tree took {} to be deserialized.", entry.getValue(), stopwatch.stop());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                }
                if (deserializingExecutor != null) {
                    deserializingExecutor.shutdown(); // Schedule a shutdown, if deserializingExecutor hasn't shut down, we await in getMain
                }
            }
        }

        public static GeneralizedSuffixTree getMain() { // Holds up and waits for deserializingExecutor to finish, shouldn't need to wait but just in case.
            if (deserializingExecutor == null || !trees.containsKey(' ')) { // Not deserializing
                return new GeneralizedSuffixTree();
            }
            if (!deserializingExecutor.isShutdown()) {
                try {
                    if (!deserializingExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                        deserializingExecutor.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    deserializingExecutor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
            deserializingExecutor = null;
            return trees.get(' ');
        }

        public static GeneralizedSuffixTree get(char key) {
            GeneralizedSuffixTree tree = trees.get(key);
            if (tree == null) {
                return new GeneralizedSuffixTree();
            }
            return tree;
        }

    }

}
