Photon Construct — Mechanics Descriptor (Implementation Contract)

Purpose: Define the exact trigger, form-change behavior, HP handling, and cleanup rules for the Photon Construct transformation. Refactors must preserve these semantics and numeric thresholds.

Trigger (Battle, repeated check)

This ability is evaluated via applyRepeatedEffectAfterStatus(PixelmonWrapper pokemon).

Transformation must occur only if all conditions are true:

pokemon.bc.simulateMode == false (never triggers in simulate mode)

Species is exactly Zygarde

Condition: pokemon.getSpecies().is(PixelmonSpecies.ZYGARDE)

Current form is not "photon-construct"

Condition: !pokemon.getForm().isForm("photon-construct")

Current HP% is <= 75.0%

Condition: pokemon.getHealthPercent() <= 75.0F

Activation Messaging

On successful trigger (before applying the transformation):

Broadcast: pixelmon.abilities.photonconstruct.activate

After transformation completes:

Broadcast: pixelmon.abilities.photonconstruct.transform with {nickname} as argument.

Form source tracking

Before changing form:

Store the original form name in persistent Pokémon data:

Key: "SrcForm"

Value: pokemon.getForm().getName()

This storage is part of the contract (used for rollback/debug parity), even if not consumed elsewhere.

Battle evolution staging

On trigger:

Set pokemon.evolution to an EvolutionQuery targeting the photon-construct form:

new EvolutionQuery(pokemon.pokemon, pokemon.getSpecies().getForm("photon-construct"))

This is not optional: it ensures the form change is treated as a battle evolution/temporary transformation by the battle system.

HP invariants and exact HP transfer rule

HP handling must preserve the absolute HP deficit across the form change.

Before form change:

maxHealth = pokemon.getMaxHealth()

deficit = pokemon.getHealthDeficit() (i.e., maxHP - currentHP in battle context)

After setting form:

Apply form change:

pokemon.setForm("photon-construct")

If pokemon.entity != null, call pokemon.bc.updateFormChange(pokemon.entity)

Recalculate HP and sync battle state:

pokemon.recalculateMaxHP()

pokemon.updateHPIncrease()

Restore HP by deficit (critical invariant):

Set HP to pokemon.getMaxHealth() - deficit

This means: currentHP becomes (newMaxHP - oldDeficit)
(HP% may change; the deficit must not.)

Adjust battle damage bookkeeping:

pokemon.updateBattleDamage(maxHealth - pokemon.getMaxHealth())

End-of-battle cleanup

This ability must revert the Pokémon after battle if it is in "photon-construct" form.

Executed in applyEndOfBattleEffect(PixelmonWrapper pokemon):

If form is "photon-construct":

hp = pokemon.getHealth(true)

pokemon.resetBattleEvolution()

pokemon.setHealth(min(hp, pokemon.getMaxHealth(true)))

This ensures the Pokémon exits battle in its original (pre-battle) state, while clamping HP to the post-reset max HP.