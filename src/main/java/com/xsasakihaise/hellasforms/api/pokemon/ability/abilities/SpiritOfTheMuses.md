Spirit of the Muses (Ability)

Type: Passive “Dancer”-style mimic + secondary-effect amplifier
Base class: AbstractAbility
Intent:

Copy specific “dance” moves used by other Pokémon (similar to Dancer), executing them immediately with deterministic targeting rules.

Double the activation chance of all chance-based move effects used by the ability holder, except while affected by Water Pledge.

1) Dance-copy effect (post-process on other Pokémon’s move)

Triggered via:
postProcessAttackOther(PixelmonWrapper pokemon, PixelmonWrapper user, PixelmonWrapper target, Attack a)

Terminology:

pokemon = ability holder (the one that may copy the move)

user = original attacker who used move a

target = original target of move a

a = the move being processed

Copy conditions (all must be true)

The ability holder copies the move only if:

pokemon is not the original user
Condition: pokemon != user

The move is not already flagged as copied via dancer logic
Condition: a.fromDancer == false

The move is one of the allowed “dance list” moves:
Aqua Step, Feather Dance, Fiery Dance, Dragon Dance, Lunar Dance, Petal Dance, Revelation Dance, Quiver Dance, Swords Dance, Teeter Dance
(Note: Swords Dance appears twice in the list; behavior is unchanged.)

Execution rules

If conditions are met:

Set dancer flag to prevent recursion:
a.fromDancer = true

Send activation message for the ability holder:
sendActivatedMessage(pokemon)

Preserve the ability holder’s lastAttack state:

Store: lastUsed = pokemon.lastAttack

After executing the copied move, restore: pokemon.lastAttack = lastUsed

Determine the copied move’s target (newTarget)

Target selection rules:

A) If the copied move is Teeter Dance OR Feather Dance:

newTarget = user
(forced to original user)

B) Otherwise (all other listed dance moves):

If the copied move is STATUS category:
newTarget = pokemon
(self-target)

Else if original user is on the same team as pokemon:
newTarget = target
(mirror the original target when ally triggered it)

Else (original user is an opponent):
newTarget = user
(retaliate at the original user)

Execute copied move:

pokemon.useTempAttack(a, [newTarget], false)

Cleanup:

Restore lastAttack

Reset dancer flag: a.fromDancer = false

Notes / invariants

This is a direct re-use of the same Attack instance a with a temporary “fromDancer” guard.

Copy effect must never chain infinitely; the fromDancer flag is the recursion lock.

The copied move executes immediately during post-processing of the original move.

2) Chance-based effect amplification (user’s moves)

Triggered via:
modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a)

Condition

Only applies if the user does NOT have Water Pledge status:
Condition: user.hasStatus(StatusType.WaterPledge) == false

Effect

For the move being used (a), iterate over move effects:

For every effect where EffectBase.isChance() returns true,
apply: effect.changeChance(2.0F)

Meaning:

Chance-based secondary effects on the user’s move have their chance multiplier set to 2.0× via changeChance(2.0F).

Power and accuracy values returned are unchanged by this ability (only effect chances are modified).

Notes / invariants

This modifies effect chances at the move-effect layer, not via accuracy or power.

Water Pledge status fully disables this chance amplification.