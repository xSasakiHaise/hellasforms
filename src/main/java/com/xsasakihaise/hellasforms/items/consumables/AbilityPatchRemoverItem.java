package com.xsasakihaise.hellasforms.items.consumables;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ability.Ability;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Ability Patch Remover item.
 * <ul>
 *     <li>Detects whether the targeted Pokemon is currently on its Hidden Ability.</li>
 *     <li>Attempts to switch back to ability slot 0 via {@code /pokeedit} command execution.</li>
 *     <li>If command execution is unavailable, falls back to using {@code PokemonSpec}
 *     or direct API manipulation to ensure the change still happens.</li>
 * </ul>
 */
public class AbilityPatchRemoverItem extends PokemonInteractItem {

    private static final Logger LOGGER = LogManager.getLogger("HellasForms/AbilityPatchRemover");

    @Override
    protected boolean applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack) {
        if (player == null || player.getServer() == null) {
            LOGGER.warn("Aborted: invalid player or not on server side.");
            return false;
        }

        final String monName = safeMonName(pokemon);
        final String playerName = player.getGameProfile().getName();
        LOGGER.info("=== AbilityPatchRemover invoked for {}'s {} ===", playerName, monName);

        boolean hasHA = safeHasHA(pokemon);
        LOGGER.info("[State:before] hasHA={}, ability={}, slot={}",
                hasHA, safeAbilityName(pokemon), safeAbilitySlot(pokemon));

        if (!hasHA) {
            LOGGER.info("Skipping: Pokémon is not on a Hidden Ability.");
            return false;
        }

        // Resolve first NORMAL (slot 0) ability
        AbilityOption normal0 = resolveNormalSlot0Ability(pokemon);
        if (normal0 == null || normal0.ability == null) {
            LOGGER.warn("Failed to resolve a normal (slot 0) ability — aborting.");
            return false;
        }
        final String abilityName = normal0.ability.getName();
        LOGGER.info("Resolved normal slot 0 ability: {}", abilityName);

        // Try to find party slot for /pokeedit
        int partySlot = resolvePartySlotIndex(player, pokemon);
        if (partySlot <= 0) {
            LOGGER.warn("Could not resolve party slot (is the Pokémon in the party?) — skipping command path.");
        }

        boolean ok = false;

        // === Command path (same pattern as your BattlePass item) ===
        if (partySlot > 0) {
            // Make replies visible
            runRaw(player, "gamerule sendCommandFeedback true");
            runRaw(player, "gamerule logAdminCommands true");

            ok = tryAllPokeeditVariants(player, playerName, partySlot, abilityName);
            if (!ok) {
                LOGGER.warn("All command execution paths failed — will attempt API fallback.");
            }
        }

        // === Fallback: PokemonSpec (mirrors /pokeedit internally) ===
        if (!ok) {
            boolean specApplied = applyAbilityViaPokemonSpec(pokemon, abilityName);
            LOGGER.info("[SpecApply] attempted={}, ability={}", specApplied, abilityName);
            ok = specApplied;
        }

        // === Last resort: force set + clear HA flags ===
        if (!ok) {
            ok = forceSetAbilityAndClearHA(pokemon, abilityName);
            LOGGER.info("[ForceSet] result={}", ok);
        }

        // Sync-ish (best effort)
        safeMarkDirty(pokemon);

        boolean afterHA = safeHasHA(pokemon);
        String afterAbility = safeAbilityName(pokemon);
        int afterSlot = safeAbilitySlot(pokemon);
        boolean success = abilityName.equals(afterAbility) && !afterHA;

        LOGGER.info("[State:after] hasHA={}, ability={}, slot={}, success={}",
                afterHA, afterAbility, afterSlot, success);

        return success;
    }

    @Override
    protected ITextComponent getSuccessMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.pixelmon.ability_patch_remover.success", pokemon.getDisplayName());
    }

    @Override
    protected ITextComponent getFailureMessage(Pokemon pokemon) {
        return new TranslationTextComponent("item.hellasforms.generic.ability_fail", pokemon.getDisplayName());
    }

    /* ========================= Command execution (BattlePass-style, no reflection) ========================= */

    private int runRaw(PlayerEntity player, String raw) {
        try {
            World world = player.level;
            if (world == null || world.isClientSide()) return 0;

            CommandSource source = player.createCommandSourceStack()
                    .withPermission(4)
                    .withSuppressedOutput() // <- no-arg in 1.16.5 MCP
                    .withPosition(new Vector3d(player.getX(), player.getY(), player.getZ()));

            int ret = world.getServer().getCommands().performCommand(source, raw);
            LOGGER.info("[Cmd] '{}' -> ret={}, ok={}", raw, ret, (ret >= 1));
            return ret; // Brigadier success count
        } catch (Throwable t) {
            LOGGER.error("runRaw exception for '{}': {}", raw, t.toString());
            return 0;
        }
    }

    private boolean tryAllPokeeditVariants(PlayerEntity player, String playerName, int partySlot, String abilityName) {
        // no quotes, no leading slash; include minecraft: variants to bypass Essentials on servers
        final String baseExact = playerName + " " + partySlot + " ability:" + abilityName;
        final String baseSelf  = "@s " + partySlot + " ability:" + abilityName;
        final String baseNear  = "@p " + partySlot + " ability:" + abilityName;

        String[] variants = new String[]{
                "minecraft:pokeedit " + baseExact,
                "pokeedit " + baseExact,
                "minecraft:pokeedit " + baseSelf,
                "pokeedit " + baseSelf,
                "minecraft:pokeedit " + baseNear,
                "pokeedit " + baseNear
        };

        for (String cmd : variants) {
            LOGGER.info("Prepared command: {}", cmd);
            int ret = runRaw(player, cmd);
            if (ret >= 1) return true;
        }
        return false;
    }

    /* ========================= PokemonSpec / API fallbacks ========================= */

    private boolean applyAbilityViaPokemonSpec(Pokemon pokemon, String abilityName) {
        if (abilityName == null || abilityName.isEmpty()) return false;
        final String specString = "ability:" + abilityName;

        try {
            Class<?> specCls = Class.forName("com.pixelmonmod.pixelmon.api.pokemon.spec.PokemonSpec");

            // PokemonSpec.from(String)
            try {
                Method mFrom = specCls.getDeclaredMethod("from", String.class);
                mFrom.setAccessible(true);
                Object spec = mFrom.invoke(null, specString);
                if (spec != null) {
                    Method apply = specCls.getDeclaredMethod("apply", Class.forName("com.pixelmonmod.pixelmon.api.pokemon.Pokemon"));
                    apply.setAccessible(true);
                    apply.invoke(spec, pokemon);
                    return true;
                }
            } catch (NoSuchMethodException ignored) {}

            // PokemonSpec.from(String[])
            for (Method m : specCls.getDeclaredMethods()) {
                if (!m.getName().equals("from")) continue;
                Class<?>[] pts = m.getParameterTypes();
                if (pts.length == 1 && pts[0].isArray() && pts[0].getComponentType() == String.class) {
                    m.setAccessible(true);
                    Object spec = m.invoke(null, (Object) new String[]{specString});
                    if (spec != null) {
                        Method apply = specCls.getDeclaredMethod("apply", Class.forName("com.pixelmonmod.pixelmon.api.pokemon.Pokemon"));
                        apply.setAccessible(true);
                        apply.invoke(spec, pokemon);
                        return true;
                    }
                }
            }

            // Constructor(String[])
            try {
                Constructor<?> ctor = specCls.getDeclaredConstructor(String[].class);
                ctor.setAccessible(true);
                Object spec = ctor.newInstance((Object) new String[]{specString});
                Method apply = specCls.getDeclaredMethod("apply", Class.forName("com.pixelmonmod.pixelmon.api.pokemon.Pokemon"));
                apply.setAccessible(true);
                apply.invoke(spec, pokemon);
                return true;
            } catch (Throwable ignored) {}

        } catch (Throwable t) {
            LOGGER.warn("[SpecApply] PokemonSpec path unavailable: {}", t.toString());
        }
        return false;
    }

    private boolean forceSetAbilityAndClearHA(Pokemon pokemon, String abilityName) {
        try {
            Ability a = findAbilityByName(abilityName);
            if (a == null) return false;

            // set ability + slot/index 0
            try { pokemon.setAbility(a); } catch (Throwable ignored) {}
            try { pokemon.setAbilitySlot(0); } catch (Throwable ignored) {}
            try { invoke(pokemon, "setAbilityIndex", new Class<?>[]{int.class}, new Object[]{0}); } catch (Throwable ignored) {}

            // clear HA flag if any mapping exists
            String[] flags = new String[]{
                    "setHasHiddenAbility",
                    "setHiddenAbility",
                    "setAbilityHidden",
                    "setIsHiddenAbility",
                    "setIsHA"
            };
            for (String f : flags) {
                Object r = invoke(pokemon, f, new Class<?>[]{boolean.class}, new Object[]{false});
                if (r != null) break;
            }

            safeMarkDirty(pokemon);

            boolean ok = abilityName.equals(safeAbilityName(pokemon)) && !safeHasHA(pokemon);
            if (!ok) {
                invoke(pokemon, "refreshAbility", new Class<?>[]{}, new Object[]{});
                invoke(pokemon, "validate", new Class<?>[]{}, new Object[]{});
                ok = abilityName.equals(safeAbilityName(pokemon)) && !safeHasHA(pokemon);
            }
            return ok;
        } catch (Throwable t) {
            LOGGER.warn("[ForceSet] failed: {}", t.toString());
            return false;
        }
    }

    /* ========================= Resolve first NORMAL (slot 0) ability ========================= */

    private AbilityOption resolveNormalSlot0Ability(Pokemon pokemon) {
        Object form = invoke(pokemon, "getForm", new Class<?>[]{}, new Object[]{});
        if (form != null) {
            Object abilitiesWrapper = invoke(form, "getAbilities", new Class<?>[]{}, new Object[]{});
            AbilityOption fromWrapper = extractFirstNormalFromAbilitiesWrapper(abilitiesWrapper);
            if (fromWrapper != null) return fromWrapper;

            Object stats = invoke(form, "getBaseStats", new Class<?>[]{}, new Object[]{});
            if (stats != null) {
                Object abil = invoke(stats, "getAbilities", new Class<?>[]{}, new Object[]{});
                AbilityOption fromStats = extractFirstNormalFromAbilitiesWrapper(abil);
                if (fromStats != null) return fromStats;
            }
        }

        Object species = invoke(pokemon, "getSpecies", new Class<?>[]{}, new Object[]{});
        if (species != null) {
            Object defaultForm = tryMany(species, "getDefaultForm", "getFormDefault", "getBaseForm");
            Object abilitiesWrapper = (defaultForm != null)
                    ? invoke(defaultForm, "getAbilities", new Class<?>[]{}, new Object[]{})
                    : null;
            AbilityOption fromWrapper = extractFirstNormalFromAbilitiesWrapper(abilitiesWrapper);
            if (fromWrapper != null) return fromWrapper;
        }

        Object normals = invoke(pokemon, "getNormalAbilities", new Class<?>[]{}, new Object[]{});
        if (normals instanceof Collection) {
            for (Object o : (Collection<?>) normals) {
                Ability a = coerceAbility(o);
                if (a != null) return new AbilityOption(a, 0);
            }
        } else if (normals != null && normals.getClass().isArray()) {
            int len = Array.getLength(normals);
            for (int i = 0; i < len; i++) {
                Ability a = coerceAbility(Array.get(normals, i));
                if (a != null) return new AbilityOption(a, 0);
            }
        }

        return null;
    }

    private AbilityOption extractFirstNormalFromAbilitiesWrapper(Object abilitiesWrapper) {
        if (abilitiesWrapper == null) return null;

        Set<String> hiddenNames = new HashSet<>();
        Object hidden = invoke(abilitiesWrapper, "getHiddenAbilities", new Class<?>[]{}, new Object[]{});
        collectAbilityNames(hidden, hiddenNames);

        Object all = invoke(abilitiesWrapper, "getAll", new Class<?>[]{}, new Object[]{});
        if (all == null) all = invoke(abilitiesWrapper, "getAbilities", new Class<?>[]{}, new Object[]{});
        if (all == null) return null;

        List<AbilityOption> normals = new ArrayList<>(2);

        if (all instanceof Collection) {
            for (Object o : (Collection<?>) all) {
                Ability a = coerceAbility(o);
                if (a == null) continue;
                if (hiddenNames.contains(a.getName())) continue;

                int slot = readAbilitySlotFromWrapper(abilitiesWrapper, a, normals.size());
                if (slot < 2) normals.add(new AbilityOption(a, slot));
            }
        } else if (all.getClass().isArray()) {
            int len = Array.getLength(all);
            for (int i = 0; i < len; i++) {
                Ability a = coerceAbility(Array.get(all, i));
                if (a == null) continue;
                if (hiddenNames.contains(a.getName())) continue;

                int slot = readAbilitySlotFromWrapper(abilitiesWrapper, a, i);
                if (slot < 2) normals.add(new AbilityOption(a, slot));
            }
        }

        normals.sort(Comparator.comparingInt(o -> o.slot));
        for (AbilityOption opt : normals) {
            if (opt.slot == 0) return opt;
        }
        return normals.isEmpty() ? null : normals.get(0);
    }

    private int readAbilitySlotFromWrapper(Object wrapper, Ability a, int fallback) {
        Object slotObj = invoke(wrapper, "getAbilitySlot", new Class<?>[]{Ability.class}, new Object[]{a});
        if (slotObj instanceof Number) return ((Number) slotObj).intValue();
        return fallback;
    }

    private void collectAbilityNames(Object container, Set<String> out) {
        if (container == null) return;
        if (container instanceof Collection) {
            for (Object o : (Collection<?>) container) {
                Ability a = coerceAbility(o);
                if (a != null) out.add(a.getName());
            }
        } else if (container.getClass().isArray()) {
            int len = Array.getLength(container);
            for (int i = 0; i < len; i++) {
                Ability a = coerceAbility(Array.get(container, i));
                if (a != null) out.add(a.getName());
            }
        }
    }

    /* ========================= Party slot helpers ========================= */

    private int resolvePartySlotIndex(PlayerEntity player, Pokemon target) {
        Object maybePos = invoke(target, "getPartyPosition", new Class<?>[]{}, new Object[]{});
        if (maybePos instanceof Number) {
            int zeroBased = ((Number) maybePos).intValue();
            if (zeroBased >= 0 && zeroBased < 6) return zeroBased + 1;
        }

        UUID playerUuid = getPlayerUUID(player);
        PartyStorage party = StorageProxy.getParty(playerUuid);
        if (party == null) return 0;

        Pokemon[] team = party.getAll();
        if (team == null) return 0;

        UUID tUuid = safeUUID(target);
        for (int i = 0; i < Math.min(6, team.length); i++) {
            Pokemon p = team[i];
            if (p == null) continue;
            if (Objects.equals(safeUUID(p), tUuid) || p == target) return i + 1;
        }
        return 0;
    }

    private UUID getPlayerUUID(PlayerEntity player) {
        try {
            Method m = player.getClass().getMethod("getUniqueID"); // MCP
            Object res = m.invoke(player);
            if (res instanceof UUID) return (UUID) res;
        } catch (Exception ignored) {}
        try {
            Method m = player.getClass().getMethod("getUUID"); // Mojang
            Object res = m.invoke(player);
            if (res instanceof UUID) return (UUID) res;
        } catch (Exception ignored) {}
        return player.getGameProfile().getId();
    }

    private UUID safeUUID(Pokemon p) {
        Object u = invoke(p, "getUUID", new Class<?>[]{}, new Object[]{});
        if (u instanceof UUID) return (UUID) u;
        Object pid = invoke(p, "getPID", new Class<?>[]{}, new Object[]{});
        return new UUID(31L * Objects.hashCode(p.getSpecies()), Objects.hashCode(pid));
    }

    /* ========================= Debug-safe state helpers ========================= */

    private boolean safeHasHA(Pokemon p) {
        try { return p.hasHiddenAbility(); } catch (Throwable ignored) { return false; }
    }

    private String safeAbilityName(Pokemon p) {
        try {
            Ability a = p.getAbility();
            return a != null ? a.getName() : "<none>";
        } catch (Throwable ignored) {
            return "<none>";
        }
    }

    private int safeAbilitySlot(Pokemon p) {
        try { return p.getAbilitySlot(); } catch (Throwable ignored) { return -1; }
    }

    private void safeMarkDirty(Pokemon p) {
        try { p.markDirty(); } catch (Throwable ignored) {}
        try { invoke(p, "refreshAbility", new Class<?>[]{}, new Object[]{}); } catch (Throwable ignored) {}
    }

    /* ========================= Misc utils ========================= */

    private Ability coerceAbility(Object v) {
        if (v instanceof Ability) return (Ability) v;
        if (v == null) return null;
        try {
            Method getName = v.getClass().getDeclaredMethod("getName");
            getName.setAccessible(true);
            Object name = getName.invoke(v);
            if (name instanceof String) {
                return findAbilityByName((String) name);
            }
        } catch (Throwable ignored) {}
        if (v instanceof String) return findAbilityByName((String) v);
        if (v instanceof Enum)   return findAbilityByName(((Enum<?>) v).name());
        return null;
    }

    private Ability findAbilityByName(String name) {
        if (name == null || name.isEmpty()) return null;
        try {
            Method m = Ability.class.getDeclaredMethod("getAbility", String.class);
            m.setAccessible(true);
            Object res = m.invoke(null, name);
            return (res instanceof Ability) ? (Ability) res : null;
        } catch (Throwable ignored) {
            try {
                Class<?> reg = Class.forName("com.pixelmonmod.pixelmon.api.pokemon.ability.AbilityRegistry");
                Method gm = reg.getDeclaredMethod("getAbility", String.class);
                Object res = gm.invoke(null, name);
                return (res instanceof Ability) ? (Ability) res : null;
            } catch (Throwable ignored2) {}
        }
        return null;
    }

    private String safeMonName(Pokemon pokemon) {
        try {
            Object dn = invoke(pokemon, "getDisplayName", new Class<?>[]{}, new Object[]{});
            if (dn instanceof ITextComponent) {
                try {
                    Method getString = dn.getClass().getMethod("getString");
                    Object s = getString.invoke(dn);
                    if (s != null) return String.valueOf(s);
                } catch (Throwable ignored) {
                    return String.valueOf(dn);
                }
            } else if (dn != null) {
                return String.valueOf(dn);
            }
        } catch (Throwable ignored) { }
        Object spec = invoke(pokemon, "getSpecies", new Class<?>[]{}, new Object[]{});
        return String.valueOf(spec != null ? spec : "<?>");
    }

    private Object invoke(Object target, String name, Class<?>[] sig, Object[] args) {
        if (target == null || name == null) return null;
        try {
            Method m = target.getClass().getDeclaredMethod(name, sig);
            m.setAccessible(true);
            return m.invoke(target, args);
        } catch (Throwable ignored) {
            Method mc = findCompatible(target.getClass(), name, args);
            if (mc != null) {
                try { mc.setAccessible(true); return mc.invoke(target, args); }
                catch (Throwable ignored2) {}
            }
        }
        return null;
    }

    private Method findCompatible(Class<?> type, String name, Object[] args) {
        Class<?> c = type;
        while (c != null && c != Object.class) {
            for (Method m : c.getDeclaredMethods()) {
                if (!m.getName().equals(name)) continue;
                Class<?>[] pts = m.getParameterTypes();
                if ((args == null ? 0 : args.length) != pts.length) continue;
                boolean ok = true;
                for (int i = 0; i < pts.length; i++) {
                    if (args == null) { ok = pts.length == 0; break; }
                    Object a = args[i];
                    if (a == null) continue;
                    if (!pts[i].isInstance(a)) { ok = false; break; }
                }
                if (ok) return m;
            }
            c = c.getSuperclass();
        }
        return null;
    }

    private Object tryMany(Object target, String... methodNames) {
        if (target == null || methodNames == null) return null;
        for (String m : methodNames) {
            Object val = invoke(target, m, new Class<?>[]{}, new Object[]{});
            if (val != null) return val;
        }
        return null;
    }

    /* DTO */
    private static final class AbilityOption {
        private final Ability ability;
        private final int slot;
        private AbilityOption(Ability ability, int slot) { this.ability = ability; this.slot = slot; }
    }
}
