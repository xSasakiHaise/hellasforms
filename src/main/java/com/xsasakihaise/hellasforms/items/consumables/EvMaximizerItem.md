EvMaximizerItem (Consumable)

File: EvMaximizerItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: PokemonInteractItem
Purpose: Instantly sets one EV stat to the Pixelmon per-stat cap (252) while strictly respecting the global total EV limit (510). Intended for quickly finalizing competitive EV spreads.

Overview

EvMaximizerItem is parameterized by:

targetStat: which EV stat to maximize

successTranslation: translation key used on success

On use, it attempts to set the specified EV stat to 252 without exceeding total EV cap rules.

No EV redistribution is performed. If the operation would violate the 510 total cap, the item fails and applies no changes.

Target stat configuration

Each item instance is created with:

BattleStatsType targetStat

String successTranslation

Meaning:

Separate item variants can exist for HP/Atk/Def/SpA/SpD/Spe by instantiating this class with different targetStat values.

Success messaging is controlled per variant via successTranslation.

Effect logic

Entry point:
applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack)

Step 1: Read current EV value

Reads EVStore via pokemon.getEVs()

current = EV value for targetStat

If current is already at or above 252:

returns false (no change)

Step 2: Compute current total EVs

Sums EV values across BattleStatsType.getEVIVStatValues()

This produces:

total = current EV total (expected cap 510)

Step 3: Validate total EV cap after maximizing

The item computes what the new total would be if the target stat were set to 252:

newTotal = total - current + 252

If newTotal exceeds 510:

returns false (no change)

This ensures:

the item cannot be used to bypass Pixelmon’s total EV cap

the item only works when enough “free EV budget” exists

Step 4: Apply change

If valid:

evStore.setStat(targetStat, 252)

pokemon.markDirty()

returns true

Messaging
Success

Uses a per-item translation key provided at construction:

successTranslation
Arguments: Pokémon display name

Failure

Uses the standard HellasForms failure key:

item.hellasforms.generic.ev_max
Arguments: Pokémon display name

Non-negotiable contract (do not change)

Must only affect one EV stat (targetStat).

Must set that stat to EVStore.MAX_EVS (252).

Must fail if target stat is already 252 or higher.

Must enforce total EV cap:

compute newTotal = total - current + 252

must fail if newTotal > EVStore.MAX_TOTAL_EVS (510)

Must not redistribute EVs from other stats.

Must mark the Pokémon dirty after applying changes.

Must apply no partial changes on failure.

Success message key must remain configurable per item variant.

Summary

EvMaximizerItem is a safe “EV finisher” consumable:

Sets a chosen EV stat to 252

Only succeeds if total EVs stay within 510

Does not re-balance other stats

Designed for quick competitive spread completion without breaking Pixelmon rules