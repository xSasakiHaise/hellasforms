package com.xsasakihaise.hellasforms.util;

import com.pixelmonmod.pixelmon.api.pokemon.stats.EVStore;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility helpers for resolving Pixelmon stat enum constants across multiple API versions.
 *
 * <p>The Pixelmon API renamed {@code StatType} to {@code StatsType} in newer releases.  Attempting to
 * compile directly against either class fails when targeting the opposite version.  This helper uses
 * reflection to resolve the enum at runtime while still allowing callers to interact with EV/IV stores
 * without knowing the underlying enum type.</p>
 */
public final class PixelmonStatTypes {

    private static final String[] CANDIDATES = {
            "com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType",
            "com.pixelmonmod.pixelmon.enums.stats.BattleStatsType",
            "com.pixelmonmod.pixelmon.api.stats.BattleStatsType"
    };

    private static final Map<String, Enum<?>> CACHE = new ConcurrentHashMap<>();

    private static volatile Class<? extends Enum<?>> battleEnum;
    private static volatile Method evGetStat;
    private static volatile MethodWithOrder evSetStat;
    private static volatile MethodWithOrder ivSetStat;

    private PixelmonStatTypes() {
    }

    public static Enum<?> hp() {
        return resolve("HP");
    }

    public static Enum<?> attack() {
        return resolve("ATTACK");
    }

    public static Enum<?> defence() {
        return resolve("DEFENCE", "DEFENSE");
    }

    public static Enum<?> specialAttack() {
        return resolve("SPECIAL_ATTACK", "SP_ATTACK");
    }

    public static Enum<?> specialDefence() {
        return resolve("SPECIAL_DEFENCE", "SPECIAL_DEFENSE", "SP_DEFENSE");
    }

    public static Enum<?> speed() {
        return resolve("SPEED");
    }

    public static int getEV(EVStore store, Enum<?> stat) {
        try {
            Object value = evGetStatMethod().invoke(store, stat);
            return ((Number) value).intValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to read EV stat using Pixelmon API", e);
        }
    }

    public static void setEV(EVStore store, Enum<?> stat, int value) {
        invokeSetter(evSetStatMethod(), store, stat, value, "EV");
    }

    public static void setIV(IVStore store, Enum<?> stat, int value) {
        invokeSetter(ivSetStatMethod(), store, stat, value, "IV");
    }

    public static Enum<?> resolve(String... candidates) {
        for (String candidate : candidates) {
            try {
                return battleStatByName(candidate);
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to resolve Pixelmon stat for candidates " + Arrays.toString(candidates));
    }

    public static Enum<?> battleStatByName(String name) {
        String key = name.toUpperCase(Locale.ROOT);
        return CACHE.computeIfAbsent(key, k -> {
            @SuppressWarnings({"unchecked", "rawtypes"})
            Enum<?> value = Enum.valueOf((Class) battleEnum(), k);
            return value;
        });
    }

    private static Class<? extends Enum<?>> battleEnum() {
        Class<? extends Enum<?>> current = battleEnum;
        if (current != null) {
            return current;
        }
        synchronized (PixelmonStatTypes.class) {
            if (battleEnum != null) {
                return battleEnum;
            }
            for (String fqcn : CANDIDATES) {
                try {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum<?>> found = (Class<? extends Enum<?>>) Class.forName(fqcn);
                    return battleEnum = found;
                } catch (ClassNotFoundException ignore) {
                }
            }
            throw new IllegalStateException("Unable to locate Pixelmon BattleStatsType enum");
        }
    }

    private static Method evGetStatMethod() {
        Method current = evGetStat;
        if (current != null) {
            return current;
        }
        synchronized (PixelmonStatTypes.class) {
            if (evGetStat != null) {
                return evGetStat;
            }
            Method located = locateStatGetter(EVStore.class, "getStat", battleEnum());
            evGetStat = located;
            return located;
        }
    }

    private static MethodWithOrder evSetStatMethod() {
        MethodWithOrder current = evSetStat;
        if (current != null) {
            return current;
        }
        synchronized (PixelmonStatTypes.class) {
            if (evSetStat != null) {
                return evSetStat;
            }
            MethodWithOrder located = locateStatSetter(EVStore.class, "setStat", battleEnum());
            evSetStat = located;
            return located;
        }
    }

    private static MethodWithOrder ivSetStatMethod() {
        MethodWithOrder current = ivSetStat;
        if (current != null) {
            return current;
        }
        synchronized (PixelmonStatTypes.class) {
            if (ivSetStat != null) {
                return ivSetStat;
            }
            MethodWithOrder located = locateStatSetter(IVStore.class, "setStat", battleEnum());
            ivSetStat = located;
            return located;
        }
    }

    private static Method locateStatGetter(Class<?> type, String name, Class<? extends Enum<?>> enumClass) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && parameterTypes[0].getName().equals(enumClass.getName())) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new IllegalStateException("Unable to locate stat getter method '" + name + "' on " + type.getName());
    }

    private static MethodWithOrder locateStatSetter(Class<?> type, String name, Class<? extends Enum<?>> enumClass) {
        for (Method method : type.getMethods()) {
            if (!method.getName().equals(name)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 2) {
                continue;
            }
            boolean enumFirst = parameterTypes[0].getName().equals(enumClass.getName()) && parameterTypes[1] == int.class;
            boolean enumSecond = parameterTypes[1].getName().equals(enumClass.getName()) && parameterTypes[0] == int.class;
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
