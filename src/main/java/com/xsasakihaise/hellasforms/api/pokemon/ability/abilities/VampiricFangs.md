Vampiric Fangs (Ability)

Type: Defensive absorption ability
Base class: AbstractAbility
Intent: Convert incoming Dark- and Ghost-type attacks into healing instead of damage.

Trigger

Evaluated whenever the Pokémon with Vampiric Fangs is targeted by an incoming move via:

allowsIncomingAttack(PixelmonWrapper pokemon, PixelmonWrapper user, Attack a)

Where:

pokemon = ability holder (defender)

user = attacker

a = incoming attack

Absorption condition

If the incoming move’s type is:

Dark

Ghost

Condition:
a.getType() == Element.DARK
OR
a.getType() == Element.GHOST

Then the ability activates.

Effect

When triggered:

The ability holder heals 25% of its maximum HP
Method: healByPercent(25.0F)

The incoming attack is completely negated
Method returns: false

Result:

No damage is taken

No secondary effects from the attack occur

The move is treated as fully blocked/absorbed

Non-trigger case

If the incoming move is not Dark or Ghost type:

The ability does nothing

The attack proceeds normally
Return value: true

Healing rules

Healing amount: 25% of max HP

Uses standard healByPercent engine logic

Cannot exceed maximum HP

Still triggers even if already at full HP (no overflow)

Healing occurs immediately when the attack would connect.

Scope

Applies to:

Physical and special moves

Status-category moves if they are Dark or Ghost type and treated as incoming attacks by the engine

All sources of Dark/Ghost damage

Does not depend on:

Contact

Move power

Attacker

Team alignment