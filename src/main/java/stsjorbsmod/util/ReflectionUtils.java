package stsjorbsmod.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.LouseDefensive;
import javassist.Modifier;
import org.clapper.util.classutil.*;
import stsjorbsmod.JorbsMod;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<Class<T>> findAllConcreteJorbsModClasses(ClassFilter classFilter)
    {
        try {
            ClassFinder finder = new ClassFinder();
            URL url = JorbsMod.class.getProtectionDomain().getCodeSource().getLocation();
            finder.add(new File(url.toURI()));

            ClassFilter filter =
                    new AndClassFilter(
                            new NotClassFilter(new InterfaceOnlyClassFilter()),
                            new NotClassFilter(new AbstractClassFilter()),
                            new ClassModifiersClassFilter(Modifier.PUBLIC),
                            classFilter
                    );
            ArrayList<ClassInfo> foundClassInfos = new ArrayList<>();
            finder.findClasses(foundClassInfos, filter);

            ArrayList<Class<T>> foundClasses = new ArrayList<>();
            for (ClassInfo classInfo : foundClassInfos) {
                Class<T> cls = (Class<T>) Loader.getClassPool().getClassLoader().loadClass(classInfo.getClassName());
                foundClasses.add(cls);
            }
            return foundClasses;
        } catch(Exception e) {
            throw new RuntimeException("Exception while finding concrete subclasses", e);
        }
    }

    // Note: only works for base classes defined within our own jar (limitation of ClassFinder)
    public static <T> ArrayList<Class<T>> findAllConcreteJorbsModSubclasses(Class<T> baseJorbsModClass) {
        return findAllConcreteJorbsModClasses(new SubclassClassFilter(baseJorbsModClass));
    }

    // Note: only works for base classes defined within our own jar (limitation of ClassFinder)
    // Requires each class have a no-arg constructor
    public static <T> List<T> instantiateAllConcreteJorbsModSubclasses(Class<T> baseJorbsModClass) {
        ArrayList<Class<T>> classes = findAllConcreteJorbsModClasses(new SubclassClassFilter(baseJorbsModClass));
        return classes.stream().map(clazz -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Exception while instantiating " + clazz.getName() + " (no default ctor?)", e);
            }
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <FieldT, InstanceT> FieldT getPrivateField(InstanceT instance, Class<? super InstanceT> clz, String fieldName) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (FieldT) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <FieldT, InstanceT> void setPrivateField(InstanceT instance, Class<? super InstanceT> clz, String fieldName, FieldT newValue) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <FieldT, InstanceT> void setFieldIfExists(InstanceT instance, Class<? super InstanceT> clz, String fieldName, FieldT newValue) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, newValue);
        } catch(NoSuchFieldException e) {
            return; // Intentional no-op
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method tryGetMethod(Class<?> clz, String methodName, Class<?>... paramTypes) {
        try {
            return clz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static AbstractMonster tryConstructMonster(String className, float offsetX, float offsetY) {
        try {
            Class<? extends AbstractMonster> clz = (Class<? extends AbstractMonster>) Class.forName(className);
            if (!AbstractMonster.class.isAssignableFrom(clz)) {
                JorbsMod.logger.error("tryConstructMonster invoked on non-AbstractMonster className " + className);
                return null;
            }

            try {
                Constructor<? extends AbstractMonster> noArgsConstructor = clz.getConstructor();
                AbstractMonster m = noArgsConstructor.newInstance();
                m.drawX = (float) Settings.WIDTH * 0.75F + offsetX * Settings.scale;
                m.drawY = AbstractDungeon.floorY + offsetY * Settings.scale;
                return m;
            } catch (NoSuchMethodException e) { }

            try {
                Constructor<? extends AbstractMonster> offsetXoffsetYConstructor = clz.getConstructor(new Class[]{float.class, float.class});
                return offsetXoffsetYConstructor.newInstance(offsetX, offsetY);
            } catch (NoSuchMethodException e) { }

            // Base game monsters with specific unique constructors get handled here, like so:
            //
            // if (className.equals(SpecialMonster.class.getName())) {
            //     Constructor<? extends AbstractMonster> specialMonsterConstructor = tryGetConstructor(clz, FirstParamType.class, SecondParamType.class);
            //     return specialMonsterConstructor.newInstance(firstParamValue, secondParamValue);
            // }

            JorbsMod.logger.warn("Could not construct monster from className " + className +"; don't know how to construct");
            return null;
        } catch (ClassNotFoundException e) {
            JorbsMod.logger.warn("Could not construct monster from unknown className " + className +"; was a mod uninstalled?");
            return null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
