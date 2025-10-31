package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AbilityPatchRemoverItem extends PokemonInteractItem {
    private final Random random = new Random();

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        List<AbilityOption> normalAbilities = resolveNormalAbilities(pokemon);
        if (normalAbilities.isEmpty()) {
            return false;
        }

        List<AbilityOption> candidateSlots = new ArrayList<>();
        int currentSlot = pokemon.getAbilitySlot();
        for (AbilityOption option : normalAbilities) {
            if (option.ability != null && (normalAbilities.size() == 1 || option.slot != currentSlot)) {
                candidateSlots.add(option);
            }
        }

        if (candidateSlots.isEmpty()) {
            return false;
        }

        AbilityOption chosen = candidateSlots.get(random.nextInt(candidateSlots.size()));
        Ability chosenAbility = chosen.ability;

        if (chosenAbility == null) {
            return false;
        }

        pokemon.setAbility(chosenAbility);
        pokemon.setAbilitySlot(chosen.slot);
        pokemon.markDirty();
        return true;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.ability_patch_remover.success", pokemon.getDisplayName());
    }

    @Override
    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.hellasforms.generic.ability_fail", pokemon.getDisplayName());
    }

    private List<AbilityOption> resolveNormalAbilities(Pokemon pokemon) {
        Object stats = invokeMethod(pokemon, "getBaseStats");
        if (stats == null) {
            Object species = invokeMethod(pokemon, "getSpecies");
            if (species != null) {
                stats = invokeMethod(species, "getBaseStats");
            }
        }

        List<AbilityOption> options = normalizeAbilityContainer(invokeMethod(stats, "getAbilities"));
        if (!options.isEmpty()) {
            return options;
        }

        return normalizeAbilityContainer(invokeMethod(pokemon, "getNormalAbilities"));
    }

    private List<AbilityOption> normalizeAbilityContainer(Object container) {
        if (container == null) {
            return Collections.emptyList();
        }

        if (container instanceof Ability[]) {
            return buildOptions(Arrays.asList((Ability[]) container));
        }

        if (container instanceof List) {
            return buildOptions((Collection<?>) container);
        }

        if (container.getClass().isArray() && Ability.class.isAssignableFrom(container.getClass().getComponentType())) {
            int length = Array.getLength(container);
            List<AbilityOption> options = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                Object value = Array.get(container, i);
                if (value instanceof Ability) {
                    options.add(new AbilityOption((Ability) value, i));
                }
            }
            return options;
        }

        if (container instanceof Map) {
            return buildOptionsFromMap((Map<?, ?>) container);
        }

        for (String methodName : new String[]{"getNormalAbilities", "getAbilities", "getAll", "values"}) {
            Object nested = invokeMethod(container, methodName);
            if (nested != null && nested != container) {
                List<AbilityOption> options = normalizeAbilityContainer(nested);
                if (!options.isEmpty()) {
                    return options;
                }
            }
        }

        Method indexedGetter = findMethod(container.getClass(), "get", int.class);
        if (indexedGetter == null) {
            indexedGetter = findMethod(container.getClass(), "getAbility", int.class);
        }
        if (indexedGetter != null) {
            indexedGetter.setAccessible(true);
            List<AbilityOption> options = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Ability ability = (Ability) invokeMethod(container, indexedGetter, i);
                if (ability != null) {
                    options.add(new AbilityOption(ability, i));
                }
            }
            if (!options.isEmpty()) {
                return options;
            }
        }

        return Collections.emptyList();
    }

    private List<AbilityOption> buildOptions(Collection<?> collection) {
        List<AbilityOption> options = new ArrayList<>();
        int index = 0;
        for (Object item : collection) {
            if (item instanceof Ability) {
                options.add(new AbilityOption((Ability) item, index));
            }
            index++;
        }
        return options;
    }

    private List<AbilityOption> buildOptionsFromMap(Map<?, ?> map) {
        List<AbilityOption> options = new ArrayList<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (isHiddenSlot(entry.getKey())) {
                continue;
            }

            int slot = resolveSlot(entry.getKey(), options.size());
            Object value = entry.getValue();
            if (value instanceof Ability) {
                options.add(new AbilityOption((Ability) value, slot));
            } else if (value instanceof Collection) {
                int subSlot = slot;
                for (Object inner : (Collection<?>) value) {
                    if (inner instanceof Ability) {
                        options.add(new AbilityOption((Ability) inner, subSlot++));
                    }
                }
            } else if (value != null && value.getClass().isArray()) {
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object element = Array.get(value, i);
                    if (element instanceof Ability) {
                        options.add(new AbilityOption((Ability) element, slot + i));
                    }
                }
            }
        }
        return options;
    }

    private int resolveSlot(Object key, int fallback) {
        if (key instanceof Number) {
            return ((Number) key).intValue();
        }
        if (key instanceof Enum) {
            return ((Enum<?>) key).ordinal();
        }
        return fallback;
    }

    private boolean isHiddenSlot(Object key) {
        if (key instanceof Enum) {
            return ((Enum<?>) key).name().toUpperCase().contains("HIDDEN");
        }
        if (key instanceof String) {
            return ((String) key).toUpperCase().contains("HIDDEN");
        }
        return false;
    }

    private Object invokeMethod(Object target, String methodName, Object... args) {
        if (target == null || methodName == null) {
            return null;
        }

        Method method = findCompatibleMethod(target.getClass(), methodName, args);
        if (method == null) {
            return null;
        }

        method.setAccessible(true);
        try {
            return method.invoke(target, adaptArguments(method.getParameterTypes(), args));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private Object[] adaptArguments(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length == 0) {
            return new Object[0];
        }
        Object[] adapted = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (args != null && i < args.length) {
                Object arg = args[i];
                if (arg != null && !parameterTypes[i].isInstance(arg) && parameterTypes[i].isPrimitive()) {
                    adapted[i] = coercePrimitive(parameterTypes[i], arg);
                } else {
                    adapted[i] = arg;
                }
            } else {
                adapted[i] = null;
            }
        }
        return adapted;
    }

    private Object coercePrimitive(Class<?> primitive, Object arg) {
        if (!(arg instanceof Number)) {
            return arg;
        }
        Number number = (Number) arg;
        if (primitive == int.class) {
            return number.intValue();
        }
        if (primitive == long.class) {
            return number.longValue();
        }
        if (primitive == float.class) {
            return number.floatValue();
        }
        if (primitive == double.class) {
            return number.doubleValue();
        }
        if (primitive == short.class) {
            return number.shortValue();
        }
        if (primitive == byte.class) {
            return number.byteValue();
        }
        if (primitive == boolean.class) {
            return number.intValue() != 0;
        }
        if (primitive == char.class) {
            return (char) number.intValue();
        }
        return arg;
    }

    private Method findCompatibleMethod(Class<?> type, String name, Object[] args) {
        if (type == null || name == null) {
            return null;
        }
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(name)) {
                    continue;
                }
                if (isCompatible(method.getParameterTypes(), args)) {
                    return method;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }

    private boolean isCompatible(Class<?>[] parameterTypes, Object[] args) {
        if (parameterTypes.length == 0) {
            return args == null || args.length == 0;
        }
        if (args == null || parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            if (parameterTypes[i].isPrimitive()) {
                if (!(arg instanceof Number)) {
                    return false;
                }
                continue;
            }
            if (!parameterTypes[i].isInstance(arg)) {
                return false;
            }
        }
        return true;
    }

    private Object invokeMethod(Object target, Method method, Object... args) {
        if (target == null || method == null) {
            return null;
        }
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        if (type == null || name == null) {
            return null;
        }
        Class<?> current = type;
        while (current != null && current != Object.class) {
            for (Method method : current.getDeclaredMethods()) {
                if (!method.getName().equals(name)) {
                    continue;
                }
                if (Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }

    private static final class AbilityOption {
        private final Ability ability;
        private final int slot;

        private AbilityOption(Ability ability, int slot) {
            this.ability = ability;
            this.slot = slot;
        }
    }
}
