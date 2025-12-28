package com.xsasakihaise.hellasforms;

import com.xsasakihaise.hellasforms.interactions.InteractionBottleCap;
import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HellasForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class HellasFormsSetup {
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent e) {
        e.enqueueWork(() -> registerInteraction(new InteractionBottleCap()));
    }

    private static void registerInteraction(final IInteraction interaction) {
        if (tryRegister(interaction, "com.pixelmonmod.pixelmon.api.interactions.InteractionRegistry", "register")) {
            return;
        }
        if (tryRegister(interaction, "com.pixelmonmod.pixelmon.api.interactions.InteractionController", "register")) {
            return;
        }
        if (tryRegister(interaction, "com.pixelmonmod.pixelmon.api.interactions.InteractionController", "registerInteraction")) {
            return;
        }
        HellasForms.getLogger().error("Unable to register Pixelmon interaction {} - no compatible registry methods found", interaction.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    private static boolean tryRegister(final IInteraction interaction, final String className, final String methodName) {
        try {
            final Class<?> registryClass = Class.forName(className);
            final Class<?> interactionType = Class.forName("com.pixelmonmod.pixelmon.api.interactions.IInteraction");
            final Method method = Arrays.stream(registryClass.getMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .filter(m -> m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);
            if (method == null) {
                return false;
            }
            final Class<?> parameterType = method.getParameterTypes()[0];
            Object argument = null;
            if (parameterType.isInstance(interaction) || parameterType.isAssignableFrom(interaction.getClass())
                    || parameterType.isAssignableFrom(interactionType)) {
                argument = interaction;
            } else if (Supplier.class.isAssignableFrom(parameterType)) {
                argument = (Supplier<IInteraction>) () -> interaction;
            }
            if (argument == null) {
                return false;
            }
            method.invoke(null, argument);
            HellasForms.getLogger().debug("Registered Pixelmon interaction {} via {}#{}", interaction.getClass().getName(), className, methodName);
            return true;
        } catch (final ReflectiveOperationException ex) {
            HellasForms.getLogger().debug("Failed to register Pixelmon interaction {} via {}#{}", interaction.getClass().getName(), className, methodName, ex);
            return false;
        }
    }
}
