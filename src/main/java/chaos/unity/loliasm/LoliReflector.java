package chaos.unity.loliasm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LoliReflector {
    public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public static MethodHandle resolveMethod(Class<?> ownerClass, String methodName, Class<?>... methodArgs) {
        try {
            Method method = ownerClass.getDeclaredMethod(methodName, methodArgs);
            method.setAccessible(true);
            return LOOKUP.unreflect(method);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MethodHandle resolveFieldGetter(Class<?> ownerClass, String fieldName) {
        try {
            Field field = ownerClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return LOOKUP.unreflectGetter(field);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MethodHandle resolveFieldSetter(Class<?> ownerClass, String fieldName) {
        try {
            Field field = ownerClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return LOOKUP.unreflectSetter(field);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
