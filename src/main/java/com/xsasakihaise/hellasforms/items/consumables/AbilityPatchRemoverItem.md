AbilityPatchRemoverItem (Consumable)

File: AbilityPatchRemoverItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: PokemonInteractItem
Purpose: Removes a Pokémon’s Hidden Ability state (as if undoing an Ability Patch) by forcing the Pokémon back to its first normal ability (slot 0). Uses multiple execution paths to ensure the change succeeds across differing server setups and Pixelmon API variants.

This item is intentionally “robust-first”: it tries the same mechanism Pixelmon servers use (/pokeedit), then falls back to internal spec/application methods, and finally uses direct API/reflective mutation as a last resort.

High-level behavior

When used on a Pokémon:

Validates server-side execution and player availability

Confirms the Pokémon is owned by the player (via the parent interaction layer)

Checks whether the Pokémon is currently using a Hidden Ability

Resolves the Pokémon’s first normal ability (slot 0)

Attempts to set that ability using the following priority order:

Command execution via /pokeedit (multiple variants)

PokemonSpec application (reflective compatibility)

Direct API mutation + clearing HA flags (last resort)

Marks the Pokémon dirty / refreshes ability (best-effort sync)

Verifies success by checking:

current ability name matches the resolved normal ability name

Hidden Ability flag is now false

The item returns true only if the state is confirmed changed.

Preconditions and early exits

The effect aborts and returns false if:

player is null

server is null (client-side or invalid context)

If the Pokémon is not on a Hidden Ability:

logs state

returns false (no change performed)

Target success criteria (contract)

A use is considered successful only if, after execution:

Pokémon ability name equals the resolved “normal slot 0” ability name
AND

Pokémon no longer reports Hidden Ability active

This final verification is performed even if a command/spec path reported success.

Slot 0 normal ability resolution

The item resolves the first normal ability (slot 0) via multiple fallback strategies to tolerate Pixelmon API differences:

Priority (conceptual):

Pokémon form → form abilities wrapper → first non-hidden normal ability with slot ordering

Pokémon form → base stats → abilities wrapper → same extraction

Species default form → abilities wrapper → same extraction

Pokémon.getNormalAbilities() (collection or array) → first entry

Hidden abilities are filtered out by name using:

wrapper.getHiddenAbilities()

comparing candidate ability names to hidden set

The resolver attempts to infer the normal ability “slot” using wrapper.getAbilitySlot(Ability) if available; otherwise it uses list index as a fallback.

Returned result is an AbilityOption:

ability (resolved Ability instance)

slot (expected slot index, primarily used for ordering)

Contract:

The chosen ability must be the first normal ability (slot 0) whenever determinable.

Party slot resolution (for /pokeedit)

The command path requires the Pokémon’s party index.

The item resolves party slot using:

pokemon.getPartyPosition() (if available), interpreted as 0-based and converted to 1-based

StorageProxy.getParty(playerUuid) → scan first 6 Pokémon and match by UUID / object identity

If the Pokémon is not found in party:

partySlot becomes 0

command path is skipped

Contract:

/pokeedit path must only run if party slot is known (1–6).

Execution paths
1) Command path (preferred)

If partySlot is valid (1–6), the item attempts to run /pokeedit using server command execution.

Before running:

enables gamerule sendCommandFeedback true

enables gamerule logAdminCommands true

Then tries these variants (until one returns success count >= 1):

minecraft:pokeedit <playerName> <slot> ability:<abilityName>

pokeedit <playerName> <slot> ability:<abilityName>

minecraft:pokeedit @s <slot> ability:<abilityName>

pokeedit @s <slot> ability:<abilityName>

minecraft:pokeedit @p <slot> ability:<abilityName>

pokeedit @p <slot> ability:<abilityName>

Notes:

Commands are sent without leading slash.

minecraft: prefix variants exist to bypass command aliasing/rewrites on some servers.

Contract:

Command path is best-effort; failure falls through to the next method.

2) PokemonSpec fallback (mirrors pokeedit internally)

If command path fails or is skipped, the item attempts to apply:

ability:<abilityName>

via reflective access to:

PokemonSpec.from(String)

PokemonSpec.from(String[])

PokemonSpec constructor(String[])

Then invokes:
spec.apply(pokemon)

Contract:

This is a compatibility path and must remain tolerant to signature differences.

If PokemonSpec exists and can apply, it should be accepted as success.

3) Force-set fallback (last resort)

If spec path fails, the item tries to force the state:

resolve Ability instance by name

attempt to set:

pokemon.setAbility(Ability)

pokemon.setAbilitySlot(0)

pokemon.setAbilityIndex(0) (reflective)

attempt to clear “hidden ability” flags using multiple possible method names:

setHasHiddenAbility(false)

setHiddenAbility(false)

setAbilityHidden(false)

setIsHiddenAbility(false)

setIsHA(false)

Then:

markDirty + refreshAbility/validate (best effort)

verify final state using the success criteria

Contract:

This path must not silently “assume success”; it must verify post-state.

Logging and diagnostics

A dedicated logger category is used:
HellasForms/AbilityPatchRemover

The item logs:

invocation context (player + mon name)

before state (hasHA, ability, slot)

resolution result (slot 0 normal ability name)

command attempts and return counts

fallback path outcomes

after state and final success boolean

This verbose logging is intentional to support bug reports from live servers.

Sync / persistence behavior

After any attempt, the item performs best-effort syncing:

pokemon.markDirty()

pokemon.refreshAbility() (if available)

This does not guarantee client update in every environment, but enforces server-side correctness.

Messages

Success message:
item.pixelmon.ability_patch_remover.success (pokemon display name)

Failure message:
item.hellasforms.generic.ability_fail (pokemon display name)

Note:

The core logic currently returns false early if not on HA; depending on parent behavior this may show failure or no feedback. The intention is “no-op if not HA.”

Non-negotiable contract (do not change)

Must only act when the Pokémon is currently on a Hidden Ability.

Must switch the Pokémon back to its first normal ability (slot 0).

Must try command path first when party slot is resolvable.

Must keep multiple command variants (minecraft: prefix + selectors) for server compatibility.

Must fall back to PokemonSpec and then direct API mutation to maximize reliability.

Must clear hidden-ability flags where possible.

Must mark the Pokémon dirty and attempt refresh after changes.

Must verify success by checking both:

ability name matches the resolved normal ability

Hidden Ability flag is false

Summary

AbilityPatchRemoverItem is a compatibility-heavy consumable that reliably undoes Hidden Ability state by returning a Pokémon to its normal slot 0 ability, using layered execution strategies to survive differing Pixelmon versions, server permissions, and command routing setups.