package com.xsasakihaise.hellasforms.util;

import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

/**
 * Utility helpers for resolving Pixelmon stat enum constants across multiple API versions.
 *
 * <p>The Pixelmon API renamed {@code StatType} to {@code StatsType} in newer releases.  Attempting to
 * compile directly against either class fails when targeting the opposite version.  This helper uses
 * reflection to resolve the enum at runtime while still allowing callers to interact with EV/IV stores
 * without knowing the underlying enum type.</p>
 */
public final class PixelmonStatTypes {

    private static final Class<? extends Enum<?>> STAT_ENUM_CLASS = locateStatEnum();
    private static final Enum<?> HP = resolve("HP");
    private static final Enum<?> ATTACK = resolve("ATTACK");
    private static final Enum<?> DEFENCE = resolve("DEFENCE", "DEFENSE");
    private static final Enum<?> SPECIAL_ATTACK = resolve("SPECIAL_ATTACK", "SP_ATTACK");
    private static final Enum<?> SPECIAL_DEFENCE = resolve("SPECIAL_DEFENCE", "SPECIAL_DEFENSE", "SP_DEFENSE");
    private static final Enum<?> SPEED = resolve("SPEED");

    private static final Method EV_GET_STAT = locateStatGetter(EVStore.class, "getStat");
    private static final MethodWithOrder EV_SET_STAT = locateStatSetter(EVStore.class, "setStat");
    private static final MethodWithOrder IV_SET_STAT = locateStatSetter(IVStore.class, "setStat");

    private PixelmonStatTypes() {
    }

    public static Enum<?> hp() {
        return HP;
    }

    public static Enum<?> attack() {
        return ATTACK;
    }

    public static Enum<?> defence() {
        return DEFENCE;
    }

    public static Enum<?> specialAttack() {
        return SPECIAL_ATTACK;
    }

    public static Enum<?> specialDefence() {
        return SPECIAL_DEFENCE;
    }

    public static Enum<?> speed() {
        return SPEED;
    }

    public static int getEV(EVStore store, Enum<?> stat) {
        try {
            Object value = EV_GET_STAT.invoke(store, stat);
            return ((Number) value).intValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to read EV stat using Pixelmon API", e);
        }
    }

    public static void setEV(EVStore store, Enum<?> stat, int value) {
        invokeSetter(EV_SET_STAT, store, stat, value, "EV");
    }

    public static void setIV(IVStore store, Enum<?> stat, int value) {
        invokeSetter(IV_SET_STAT, store, stat, value, "IV");
    }

    public static Enum<?> resolve(String... candidates) {
        for (String candidate : candidates) {
            try {
                return Enum.valueOf(STAT_ENUM_CLASS, candidate.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to resolve Pixelmon stat for candidates " + Arrays.toString(candidates));
    }

    private static Class<? extends Enum<?>> locateStatEnum() {
        for (String className : new String[]{
                "com.pixelmonmod.pixelmon.api.pokemon.stats.StatsType",
                "com.pixelmonmod.pixelmon.api.pokemon.stats.StatType"
        }) {
            try {
                Class<?> clazz = Class.forName(className);
                if (Enum.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
                    return enumClass;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        throw new IllegalStateException("Unable to locate Pixelmon stat enum class");
    }

    private static Method locateStatGetter(Class<?> type, String name) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].getName().equals(STAT_ENUM_CLASS.getName())) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new IllegalStateException("Unable to locate stat getter method '" + name + "' on " + type.getName());
    }

    private static MethodWithOrder locateStatSetter(Class<?> type, String name) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 2) {
                continue;
            }
            boolean enumFirst = parameterTypes[0].getName().equals(STAT_ENUM_CLASS.getName()) && parameterTypes[1] == int.class;
            boolean enumSecond = parameterTypes[1].getName().equals(STAT_ENUM_CLASS.getName()) && parameterTypes[0] == int.class;
            if (enumFirst || enumSecond) {
                method.setAccessible(true);
                return new MethodWithOrder(method, enumSecond);
            }
        }
        throw new IllegalStateException("Unable to locate stat setter method '" + name + "' on " + type.getName());
    }

    private static void invokeSetter(MethodWithOrder methodWithOrder, Object target, Enum<?> stat, int value, String label) {
        try {
            if (methodWithOrder.swapArguments) {
                methodWithOrder.method.invoke(target, value, stat);
            } else {
                methodWithOrder.method.invoke(target, stat, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to modify " + label + " stat using Pixelmon API", e);
        }
    }

    private static final class MethodWithOrder {
        private final Method method;
        private final boolean swapArguments;

        private MethodWithOrder(Method method, boolean swapArguments) {
            this.method = method;
            this.swapArguments = swapArguments;
        }
    }
}
