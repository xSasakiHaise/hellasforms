InteractionBottleCap (Custom Bottle Cap Interaction)

File: InteractionBottleCap.java
Module: hellasforms.interactions
Implements: IInteraction
Purpose: Provides a HellasForms-controlled version of Pixelmon’s Bottle Cap interaction UI, enforcing Hellas-specific restrictions (owner-only use, level gate, and IV-status checks) while still posting the standard Pixelmon BottleCapEvent for compatibility with other mods.

Overview

This interaction triggers when a player uses a Bottle Cap item on a Pixelmon entity. It mirrors Pixelmon’s Bottle Cap GUI flow, but adds strict prerequisites before the GUI may open:

Server-side only

Main hand only

BottlecapItem only

Owner-only usage (UUID match)

Level gate: Pokémon must be level 50+

Prevents opening if all IV work is already complete (either max IV sum or already fully hyper-trained)

If all checks pass, it opens the Pixelmon Bottle Cap GUI with additional precomputed display values.

Interaction entry point

Method:
processInteract(PixelmonEntity pixelmon, PlayerEntity player, Hand hand, ItemStack stack)

Returns:

false if the interaction should not proceed or should be treated as “not handled”

true if handled (including “blocked with message” cases)

Preconditions (hard filters)

The interaction immediately returns false if any of the following are true:

Executed on the client side (must run on server)

Interaction is with the off-hand

The held item is not an instance of BottlecapItem

These filters ensure the interaction does not run twice or on invalid item usage.

Owner restriction

After passing preconditions:

The Pokémon’s owner UUID is resolved.

The interaction proceeds only if:

owner UUID exists AND

owner UUID equals the interacting player’s UUID

If ownership fails:

returns false

no messages are sent (silent denial)

Owner UUID is read via reflective compatibility (see “Owner UUID accessor compatibility”).

Level gate

If ownership passes:

If Pokémon level is below 50:

Sends chat message key: pixelmon.interaction.bottlecap.level
Arguments: Pokémon nickname

Returns true (handled; blocked)

This ensures Bottle Cap UI can only be used from level 50 onward.

“Already complete” gate (IV summary)

If level requirement is met:

Reads Pokémon IV store.

Iterates over BattleStatsType.getEVIVStatValues() (the standard IV/EV stat list).

Computes:

sum = sum of IVs across all applicable stats

allHT = true only if every stat is already hyper-trained

Completion check:

isMax is true if sum equals 186
(i.e., 6 stats × 31 IV)

If either condition is true:

isMax == true OR allHT == true

Sends chat message key: pixelmon.interaction.bottlecap.full
Arguments: Pokémon nickname

Returns true (handled; blocked)

Meaning:

The GUI cannot be opened if the Pokémon is already “perfect” by IV sum, or already fully hyper-trained.

Event compatibility (BottleCapEvent)

Before opening the GUI, the interaction posts a standard Pixelmon event:

Pixelmon.EVENT_BUS.post(new BottleCapEvent(pixelmon, player, null, stack))

If the event is canceled (post returns true):

return false

GUI is not opened

This preserves mod interoperability: other mods can veto bottle cap usage.

GUI payload generation (screenData)

If not canceled:

n = number of EV/IV stat types

screenData is an int array of length n + 1

For each stat index i:

If that stat is NOT hyper-trained AND IV != 31:

screenData[i] is populated with a “hyper-trained stat preview value” computed via getHTValue

Otherwise:

screenData[i] = 0

The last element:

screenData[n] = pixelmon entity ID

The GUI is then opened via:

OpenScreenPacket.open(player, EnumGuiScreen.BottleCap, screenData)

This matches Pixelmon’s expected screen input format.

getHTValue (stat preview helper)

Purpose:

Compute the would-be calculated stat value if the selected stat were hyper-trained, without permanently modifying the IV store.

Mechanics:

Temporarily set hyper-trained flag for the given stat to true

Calculate the stat using:
pokemon.getStats().calculateStat(type, nature, form, level)

Restore the original hyper-trained state

Return the calculated stat value

This is strictly a preview calculation and must remain non-destructive.

Owner UUID accessor compatibility

Pixelmon API has historically used different owner UUID getter names depending on version.

This interaction resolves the accessor once at class load:

Try Pokemon.getOwnerUUID()

If missing, try Pokemon.getOwnerPlayerUUID()

If both missing:

log an error

owner checks will always fail (interaction becomes unusable)

Invocation failures are logged and treated as “no owner” (returns null), which blocks interaction.

This reflection shim exists to keep compatibility across Pixelmon builds without hard-locking to a single method name.

Non-negotiable contract (do not change)

Must run server-side only, main-hand only, BottlecapItem only.

Must enforce owner-only usage via UUID match.

Must enforce level gate: level 50 minimum.

Must block if IV sum equals 186 OR if all relevant stats are hyper-trained.

Must post BottleCapEvent before opening UI and respect cancellation.

Must populate screenData as:

per-stat HT preview value only for non-HT stats with IV != 31

otherwise 0

final entry is pixelmon entity ID

getHTValue must remain temporary and restore hyper-train state after calculation.

Owner UUID reflection must remain to support multiple Pixelmon API variants.

Summary

InteractionBottleCap is a controlled gateway to Pixelmon’s Bottle Cap GUI:

Only the owner can use it

Only at level 50+

Only if improvement is still possible

Still compatible with other mods via BottleCapEvent

Provides accurate stat previews for non-max, non-HT stats