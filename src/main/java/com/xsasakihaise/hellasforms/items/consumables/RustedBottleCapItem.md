RustedBottleCapItem (Consumable)

File: RustedBottleCapItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: PokemonInteractItem
Purpose: Negative bottle cap variant that randomly selects one IV stat, removes any hyper-training on that stat, and resets the IV value to 0.

Designed as an intentional “de-optimization” item for rerolling or correcting hyper-trained/perfect Pokémon.

Overview

When used on a Pokémon:

One IV stat is chosen at random

Hyper-training on that stat is removed

The IV value for that stat is set to 0

Pokémon is marked dirty

Item is consumed

This effect always succeeds when applied to a valid Pokémon.

Stat selection

The item selects from:

BattleStatsType.getEVIVStatValues()

This includes all standard IV stats:

HP

Attack

Defense

Special Attack

Special Defense

Speed

Selection method:

Uniform random choice across all available IV stats.

There is no weighting or filtering.

Effect logic

Entry point:
applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack)

Step 1: Choose stat

Randomly select one stat from the IV stat list.

Step 2: Remove hyper-training

store.setHyperTrained(chosen, false)

Ensures:

Any artificial perfect-stat override is cleared.

Step 3: Reset IV value

store.setStat(chosen, 0)

Sets the IV of the chosen stat to the minimum value.

Step 4: Persist change

pokemon.markDirty()

Step 5: Return success

Always returns true

No validation or failure conditions exist once a valid Pokémon is targeted.

Messaging

Success message key:
item.pixelmon.rusted_bottle_cap.success
Arguments:

Pokémon display name

Failure messaging:

Not used (applyEffect always returns true)

Base class fallback never triggered

Behavior notes

Effect applies regardless of current IV value.

Effect applies regardless of hyper-trained state.

If the stat is already 0 and not hyper-trained:

Operation still runs

Item still consumed

Considered intentional destructive/randomization behavior.

This item is not meant to be safe or reversible.

Non-negotiable contract (do not change)

Must select exactly one stat randomly from BattleStatsType.getEVIVStatValues().

Must clear hyper-training for that stat.

Must set that stat’s IV to 0.

Must call pokemon.markDirty() after modification.

Must always return true when applied to a valid Pokémon.

Must not add safety checks preventing IV reduction.

Must not attempt to preserve previous IV values.

Must not skip already-zero stats (random selection must remain uniform).

Summary

RustedBottleCapItem is a destructive IV-reset consumable:

Randomly selects one IV stat

Removes hyper-training

Sets that IV to 0

Always succeeds and consumes the item

Intended for IV rerolling, challenge modes, or controlled de-optimization systems