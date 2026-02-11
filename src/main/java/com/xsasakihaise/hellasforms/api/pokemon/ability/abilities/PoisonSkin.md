Poison Skin (Ability)

Type: Contact-trigger ability
Base class: ContactDamage (Rough Skin framework)
Intent: Combine standard Rough Skin retaliation damage with an additional poison-on-contact chance.

Core behavior

Poison Skin must always function as:

Rough Skin damage on contact
PLUS
30% chance to poison the attacker

It is not a replacement for Rough Skin — it is an extension.

Trigger

Activates whenever a contact event occurs and the engine calls:

applyEffectOnContactUser(user, target)

Where:

user = Pokémon with Poison Skin

target = Pokémon that made contact with the user

Effects
1. Rough Skin retaliation (mandatory)

On contact:

Standard ContactDamage / Rough Skin retaliation damage must be applied.

This comes from the base ContactDamage implementation.

This call must always execute before any Poison Skin logic.

If this behavior stops occurring, the ability is broken.

2. Poison application

After Rough Skin damage is applied:

A poison roll is performed.

Chance: 30%

Status: standard poison (not toxic)

Target: the contact attacker

If poison is successfully applied, a battle message is broadcast indicating Poison Skin activation.

Poison application must still respect all engine rules:

type immunities

ability immunities

status restrictions

If poison fails to apply, no message is shown.

Order of operations (must remain exact)

Contact occurs

Rough Skin retaliation damage is applied

30% poison roll is performed

If poison succeeds → broadcast message

Poison must never replace or prevent Rough Skin damage.

Messaging

Broadcast key:
pixelmon.abilities.poisonskin

Arguments:

user nickname

target nickname

This message only triggers if poison is successfully applied.