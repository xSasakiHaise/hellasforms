package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * EXP candy variant that grants a configurable amount of experience to the
 * targeted Pokemon. Reflection is used so it works across multiple Pixelmon
 * builds where the EXP APIs moved between releases.
 */
public class ExperienceCandyItem extends PokemonInteractItem {
    private final int experienceAmount;
    private final String successTranslation;

    public ExperienceCandyItem(int experienceAmount, String successTranslation) {
        super();
        this.experienceAmount = experienceAmount;
        this.successTranslation = successTranslation;
    }

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        if (addExperience(pokemon, experienceAmount)) {
            pokemon.markDirty();
            return true;
        }
        return false;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent(successTranslation, pokemon.getDisplayName());
    }

    /**
     * Attempts several possible EXP APIs in order of preference so that the
     * item remains functional regardless of obfuscation mappings.
     */
    private boolean addExperience(Pokemon pokemon, int amount) {
        if (pokemon == null || amount <= 0) {
            return false;
        }

        Method directAdd = findMethod(Pokemon.class, "addExperience", int.class);
        if (directAdd != null) {
            try {
                directAdd.invoke(pokemon, amount);
                return true;
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        // Older builds exposed a nested experience store, newer ones used primitives.
        Object experienceObject = invoke(pokemon, "getExperience");
        if (experienceObject instanceof Number) {
            Method setter = findMethod(Pokemon.class, "setExperience", int.class);
            if (setter != null) {
                int current = ((Number) experienceObject).intValue();
                try {
                    setter.invoke(pokemon, current + amount);
                    return true;
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        } else if (experienceObject != null) {
            Method add = findMethod(experienceObject.getClass(), "addExperience", int.class);
            if (add != null) {
                try {
                    add.invoke(experienceObject, amount);
                    return true;
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }

        Method gain = findMethod(Pokemon.class, "gainExperience", int.class);
        if (gain != null) {
            try {
                gain.invoke(pokemon, amount);
                return true;
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        return false;
    }

    private Object invoke(Object target, String method) {
        Method m = findMethod(target.getClass(), method);
        if (m == null) {
            return null;
        }
        try {
            return m.invoke(target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Utility that walks a class hierarchy for a declared method without
     * assuming SRG or Mojang naming.
     */
    private Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        if (type == null) {
            return null;
        }
        Class<?> current = type;
        while (current != null && current != Object.class) {
            try {
                Method method = current.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
            current = current.getSuperclass();
        }
        return null;
    }
}
