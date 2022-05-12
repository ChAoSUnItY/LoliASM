package chaos.unity.loliasm.common.java;

import chaos.unity.loliasm.LoliLogger;
import chaos.unity.loliasm.LoliReflector;
import chaos.unity.loliasm.api.LoliStringPool;
import com.google.common.base.Stopwatch;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.impl.launch.knot.Knot;

import java.lang.invoke.MethodHandle;
import java.security.*;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavaFixes {
    private static final MethodHandle SECURECLASSLOADER$PDCACHE$GETTER;
    private static final MethodHandle PERMISSION$NAME$SETTER;

    static {
        MethodHandle secureClassLoader$pdcache$getter = null;
        MethodHandle permission$name$setter = null;
        try {
            secureClassLoader$pdcache$getter = LoliReflector.resolveFieldGetter(SecureClassLoader.class, "pdcache");
            permission$name$setter = LoliReflector.resolveFieldSetter(Permission.class, "name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SECURECLASSLOADER$PDCACHE$GETTER = secureClassLoader$pdcache$getter;
        PERMISSION$NAME$SETTER = permission$name$setter;
    }

    public static final JavaFixes INSTANCE = new JavaFixes();

    private JavaFixes() {
        run();
    }


    public void run() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            LoliStringPool.establishPool(LoliStringPool.FILE_PERMISSIONS_ID, 512);
            Map<CodeSource, ProtectionDomain> pdcache = (ConcurrentHashMap<CodeSource, ProtectionDomain>) SECURECLASSLOADER$PDCACHE$GETTER.invoke(ClassLoader.getSystemClassLoader());
            for (ProtectionDomain pd : pdcache.values()) {
                PermissionCollection pc = pd.getPermissions();
                if (pc != null) {
                    Enumeration<Permission> perms = pc.elements();
                    while (perms.hasMoreElements()) {
                        Permission perm = perms.nextElement();
                        PERMISSION$NAME$SETTER.invokeExact(perm, LoliStringPool.canonicalize(perm.getName()));
                    }
                }
            }
            LoliStringPool.purgePool(LoliStringPool.FILE_PERMISSIONS_ID).close();
        }
        catch (ConcurrentModificationException ignored) { } // Swallow it, we don't care enough about the CME here
        catch (Throwable t) {
            t.printStackTrace();
        }
        LoliLogger.INSTANCE.info("Took {} to canonicalize Java's FilePermission caches.", stopwatch.stop());
    }
}
