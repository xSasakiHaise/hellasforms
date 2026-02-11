ExperienceCandyItem (Consumable)

File: ExperienceCandyItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: PokemonInteractItem
Purpose: EXP candy item that grants a fixed, configurable amount of experience to the targeted Pokémon. Uses reflection to remain compatible across Pixelmon builds where EXP APIs and naming changed.

Overview

ExperienceCandyItem is a parameterized consumable:

experienceAmount: how much EXP to grant

successTranslation: translation key shown on success

On use, it attempts to add EXP to the targeted Pokémon via a tiered “best available method” strategy. If any supported EXP API succeeds, the Pokémon is marked dirty and the action is treated as successful.

Configuration

Each item instance is created with:

experienceAmount (int)

successTranslation (String)

This allows multiple candy tiers (XS/S/M/L/XL/etc.) to reuse the same class with different EXP values and different success text keys.

Effect logic

Entry point:
applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack)

Behavior:

Calls addExperience(pokemon, experienceAmount)

If true:

pokemon.markDirty()

returns true

Otherwise returns false

No partial modification is attempted beyond the first successful EXP path.

EXP application strategy (reflection compatibility)

Method:
addExperience(Pokemon pokemon, int amount)

Preconditions

Returns false if:

pokemon is null

amount <= 0

Priority order (attempts)

The item attempts the following EXP APIs in sequence, stopping at the first that successfully executes without reflection exceptions.

Direct method on Pokemon:

Pokemon.addExperience(int)

If present, invoked with amount.

getExperience() path (handles numeric or store object)

The method calls pokemon.getExperience() reflectively and branches:

A) If getExperience() returns a Number:

Attempts Pokemon.setExperience(int)

Sets experience to current + amount

B) If getExperience() returns a non-null object (experience store):

Attempts experienceStore.addExperience(int)

Invokes with amount

Alternate direct method on Pokemon:

Pokemon.gainExperience(int)

If present, invoked with amount.

If none of these paths succeed:

returns false

Compatibility notes

This item is explicitly designed to survive changes in Pixelmon’s API / mapping names across releases:

Some builds expose addExperience(int)

Some expose gainExperience(int)

Some store experience as an integer property with get/set

Some expose an experience store object with addExperience

Reflection is used to avoid a hard dependency on a single signature.

Messaging
Success

Uses per-item translation key:

successTranslation
Arguments: Pokémon display name

Failure

Inherited failure messaging from PokemonInteractItem (not overridden in this class).

Meaning:

Failure feedback behavior depends on the parent class’ implementation.

Persistence / sync

On success:

pokemon.markDirty() is called

This is intended to:

persist changes

trigger appropriate sync/update flows

No additional client packet behavior is implemented here.

Non-negotiable contract (do not change)

Must grant exactly experienceAmount EXP on success.

Must return false for amount <= 0 or null Pokémon.

Must attempt EXP APIs in the defined “best effort” order:

Pokemon.addExperience(int)

getExperience() numeric → setExperience(current + amount)

getExperience() object → addExperience(amount)

Pokemon.gainExperience(int)

Must stop after first successful application.

Must call pokemon.markDirty() on success.

Must remain reflection-based to preserve cross-build compatibility.

Must not silently “assume success” if reflection calls fail; failure must return false.

Summary

ExperienceCandyItem is a configurable EXP candy that reliably applies EXP across multiple Pixelmon builds by probing known EXP method variants via reflection. It succeeds if any supported EXP application path executes and then persists the change by marking the Pokémon dirty.