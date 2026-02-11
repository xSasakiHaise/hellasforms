Sword of Justice (Ability)

Type: Passive offensive modifier
Base class: Sharpness
Intent: Retain all standard Sharpness behavior while increasing damage dealt to specific target types.

Base behavior

Sword of Justice inherits from Sharpness.

This means:

All default Sharpness mechanics remain active.

Any slicing-based move bonuses from Sharpness must remain unchanged.

Sword of Justice adds an additional damage modifier on top of Sharpness.

It does not replace Sharpness.

Damage modifier vs specific target types

Triggered via:
modifyDamageTarget(int damage, PixelmonWrapper user, PixelmonWrapper target, Attack attack)

Condition

If the target has at least one of the following types:

Dark

Dragon

Ghost

Poison

Fairy

Condition is evaluated using:
target.hasType(Element.X)

If the target matches any listed type, the bonus applies.

Typing is inclusive:

Dual-type targets only need one matching type.

If a target has multiple matching types, the multiplier still applies only once.

Effect

If condition is met:

Outgoing damage is multiplied by 1.2× (20% increase).

Formula:
finalDamage = floor(damage × 1.2)

Applied at the damage modification stage, after base damage is calculated.

Scope

Applies to:

All damaging moves

Physical and special

Slicing and non-slicing moves

Any type used by the attacker

This modifier is based solely on the target’s typing, not the move’s type.

Stacks with:

Sharpness slicing bonuses

STAB

Items

Terrain

Other multipliers

according to normal Pixelmon damage order.