GrowthSpawningListener (Spawn Weight Rebalance)

File: GrowthSpawningListener.java
Package: com.xsasakihaise.hellasforms.listener
Type: Forge event listener
Purpose: Rebalances Pixelmon growth-size spawn weights when the server starts, producing a more even and intentional distribution across all size categories.

This listener overrides Pixelmon’s default growth rarity values globally.

Activation

Triggered on:

FMLServerStartedEvent

Meaning:

Executes once when the server finishes starting

Applies globally to all future Pixelmon spawns

Does not retroactively change already spawned Pokémon

Must be registered to the Forge event bus to function.

Behavior

On server start, the listener directly modifies:

EnumGrowth.<growth>.rarity

These values control how frequently each growth size spawns.

The change affects:

wild spawns

generated Pokémon

any spawn system referencing EnumGrowth rarity

New rarity distribution

After modification, growth rarity weights become:

Microscopic → 2
Pygmy → 4
Runt → 8
Small → 16
Ordinary → 32
Huge → 16
Giant → 8
Enormous → 4
Ginormous → 2

Distribution intent

This produces a symmetric distribution centered on Ordinary size.

Key characteristics:

Ordinary remains the most common

Small and Huge form secondary tiers

Runt and Giant are uncommon

Extremes (Microscopic/Pygmy/Enormous/Ginormous) are rare

Prevents extreme size dominance

Makes unusual sizes present but uncommon

Effectively creates a bell-curve-style growth distribution.

Technical notes

These values directly overwrite Pixelmon defaults.

Changes persist for the entire runtime of the server.

Values are simple weight integers, not percentages.

Pixelmon uses these weights comparatively across growth types.

No config system is currently used — values are hardcoded.

Non-negotiable contract (do not change)

Must execute on FMLServerStartedEvent.

Must directly modify EnumGrowth rarity values.

Must set all growth rarities explicitly (no partial overrides).

Must maintain symmetric distribution across small/large extremes.

Ordinary must remain highest weight.

Listener must remain server-side only.

Summary

GrowthSpawningListener globally rebalances Pixelmon size spawn weights at server start to create a smoother, more intentional size distribution across all growth tiers, ensuring rare extreme sizes exist without overwhelming standard gameplay.