PokemonInteractItem (Consumable Base Class)

File: PokemonInteractItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: QuestItem
Purpose: Common base class for HellasForms consumables that apply an effect directly to a targeted Pixelmon entity (EV/EXP/items/forms/abilities). Provides consistent interaction handling, item consumption rules, and player feedback messaging.

Overview

PokemonInteractItem standardizes the “use item on Pokémon” interaction flow so subclasses only implement:

applyEffect(...) → the actual gameplay change

getSuccessMessage(...) → localized success feedback

optionally getFailureMessage(...) → localized failure feedback

This class handles:

target validation (must be PixelmonEntity)

server-side execution

stack consumption (unless creative)

pushing updated Pokémon state back onto the entity

client message display

Interaction flow

Primary entry point:
interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand)

Step 1: Target validation

If the target is not a PixelmonEntity:

return PASS (item behaves as if it did nothing)

If PixelmonEntity has no Pokémon instance:

return PASS

This ensures the item only functions when used directly on a valid Pixelmon entity with a Pokémon backing object.

Step 2: Server-side execution

All effects run only on the server:

If player.level.isClientSide is true:

no effect is applied

the method still returns sidedSuccess for proper client behavior

This prevents double-application and keeps authority server-side.

Step 3: Apply the subclass effect

On server side, the class calls:

applyEffect(player, pokemon, pixelmonEntity, stack)

Contract:

applyEffect returns true only when the effect actually happened and the item should be consumed.

Step 4a: Success path (applyEffect == true)

If the effect succeeds:

Consume item stack (unless creative):

If player is not creative: stack.shrink(1)

Push updated Pokémon back to entity:

pixelmonEntity.setPokemon(pokemon)

This is critical for ensuring the in-world entity reflects modifications made to the Pokémon data.

Send success message to player (action bar style):

player.displayClientMessage(getSuccessMessage(pokemon), true)

Step 4b: Failure path (applyEffect == false)

If the effect fails:

Resolve failure message:
failure = getFailureMessage(pokemon)

If non-null, send to player:
player.displayClientMessage(failure, true)

No item consumption occurs on failure.

Step 5: Return value

Always returns:
ActionResultType.sidedSuccess(player.level.isClientSide)

Meaning:

Interaction reports success on the side appropriate to the caller.

The PASS return is reserved strictly for invalid targets / missing Pokémon.

Messaging defaults
Default failure message

If a subclass does not override getFailureMessage:

Translation key:
item.hellasforms.generic.no_effect
Arguments:

Pokémon display name

This provides consistent feedback for “nothing happened” cases.

Success message

Subclasses must provide getSuccessMessage(Pokemon pokemon).

This should describe what the item did (EV maxed, EXP gained, form changed, ability reverted, etc.).

Consumption rules

Item is consumed only when applyEffect returns true.

Creative players do not consume the item.

Failure never consumes.

This is the core contract that all subclass behavior relies on.

Subclass contract

Subclasses must implement:

applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack)

Rules:

Return true only if state was changed and the item should be consumed.

Perform all gameplay changes server-side (this base class already ensures it).

Call pokemon.markDirty() when modifying persistent Pokémon state (recommended, not enforced here).

Subclasses must implement:

getSuccessMessage(Pokemon pokemon)

Subclasses may override:

getFailureMessage(Pokemon pokemon)

Non-negotiable contract (do not change)

Must only apply effects when target is PixelmonEntity and pokemon != null.

Must only execute applyEffect on the server side.

Must consume the item only when applyEffect returns true and player is not creative.

Must call pixelmonEntity.setPokemon(pokemon) on success to sync entity state.

Must display success message on success, and failure message on failure (if non-null).

Must return PASS only for invalid targets / missing Pokémon.

Must return sidedSuccess(...) for valid interactions to maintain correct client/server behavior.

Summary

PokemonInteractItem is the standardized HellasForms consumable interaction base:

Validates Pixelmon targets

Runs effects server-side only

Consumes item only on confirmed success

Syncs updated Pokémon back onto the entity

Provides consistent localized player messaging

All consumables (EXP candies, EV tools, form tickets, ability tools, etc) should derive from this class to keep behavior predictable and easy to maintain.