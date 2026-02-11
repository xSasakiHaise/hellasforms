Eeveeolite (Held Item)

File: EeveeoliteItem.java
Package: com.xsasakihaise.hellasforms.items.heldItems
Extends: HeldItem
Purpose: Held item that doubles Eevee’s Speed and Special Attack while held. Has no effect on any other species.

Overview

Eeveeolite is a species-specific held item.

While held by Eevee only:

Speed is doubled

Special Attack is doubled

All other Pokémon receive no stat changes from this item.

This modifier applies continuously while the item is held.

Activation condition

Triggered via:
modifyStats(PixelmonWrapper user, int[] stats)

Effect applies only if:
user.getSpecies().getName().equals("Eevee")

If the holder is not exactly "Eevee":

Stats are returned unchanged.

Form variants of Eevee still count as Eevee if the species name remains "Eevee".

Stat modifications

When held by Eevee:

Speed

Multiplier: 2.0×

Applied to BattleStatsType.SPEED

Special Attack

Multiplier: 2.0×

Applied to BattleStatsType.SPECIAL_ATTACK

Both modifications are applied directly to the stat array provided by Pixelmon’s stat pipeline.

Formula:
newStat = floor(originalStat × 2.0)

Scope

Applies to:

All battle contexts

All moves

All forms of Eevee (if species name is still "Eevee")

Does not apply to:

Evolutions of Eevee (Vaporeon, Jolteon, etc.)

Any non-Eevee species

Stacking behavior

The stat multipliers stack normally with:

EV/IV calculations

nature modifiers

abilities

battle boosts

other held item effects (if compatible)

Order of application is controlled by Pixelmon’s stat pipeline; Eeveeolite simply multiplies the resulting stat values.

Non-negotiable contract (do not change)

Must only affect Pokémon whose species name equals "Eevee".

Must multiply Speed by exactly 2.0×.

Must multiply Special Attack by exactly 2.0×.

Must not affect any other stats.

Must not affect non-Eevee species.

Must apply through modifyStats().

Must remain a passive held-item stat modifier with no activation messages or conditions.

Summary

Eeveeolite is a species-locked held item:

While held by Eevee:

Speed ×2

Special Attack ×2

Provides no benefit to any other Pokémon and applies continuously while equipped.