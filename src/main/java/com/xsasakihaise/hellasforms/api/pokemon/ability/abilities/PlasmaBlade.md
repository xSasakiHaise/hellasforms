Plasma Blade (Ability)

Type: Passive ability (extends Sharpness)
Scope: Battle-only behavior (state resets at end of battle)
Intent: Keep all normal Sharpness effects and additionally amplify the user’s Electric-type damage.

Effects
1) Sharpness inheritance

Plasma Blade inherits from com.pixelmonmod.pixelmon.api.pokemon.ability.abilities.Sharpness and therefore retains all Sharpness mechanics (i.e., Sharpness continues to apply its usual modifiers to eligible moves). Plasma Blade does not disable or replace Sharpness.

2) Electric damage amplification

Whenever the user executes an Electric-type attack:

Condition: a.getType() == Element.ELECTRIC

Effect: outgoing damage is multiplied by 1.5× at the damage modification stage (modifyDamageUser).

Formula:

finalDamage = floor(baseDamage * 1.5)

Notes:

This applies to any Electric-type move that results in damage (physical or special), independent of “slicing”/Sharpness eligibility.

The multiplier stacks with all other damage modifiers (STAB, terrain, items, other effects) according to Pixelmon’s internal damage order; Plasma Blade’s contribution remains exactly 1.5× when the move’s type is Electric.

State & lifecycle
usedOnce flag (implementation detail)

Plasma Blade tracks a boolean usedOnce:

Set to true the first time an Electric-type attack is processed.

Currently does not change gameplay (no conditional logic depends on it).

Resets to false at end of battle.

This flag exists as a state hook / marker and must remain battle-local.

Per-Pokémon instance requirement

needNewInstance() returns true.

Meaning:

Each Pokémon must receive its own ability instance.

Internal state (e.g., usedOnce) must not leak across Pokémon or battles.

End of battle

At battle end (applyEndOfBattleEffect):

usedOnce is reset to false.