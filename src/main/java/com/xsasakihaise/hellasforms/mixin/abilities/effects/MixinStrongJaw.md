MixinStrongJaw (Strong Jaw Ability Extension)

File: MixinStrongJaw.java
Package: com.xsasakihaise.hellasforms.mixin.abilities.effects
Type: Sponge Mixin overwrite
Target: StrongJaw ability (Pixelmon)
Purpose: Replaces Pixelmon’s built-in Strong Jaw move whitelist with an expanded HellasForms fang/bite move list, allowing Strong Jaw to correctly boost custom Hellas biting moves.

This mixin fully overwrites Strong Jaw’s internal power calculation logic.

Overview

Pixelmon hardcodes the Strong Jaw move list internally.
This mixin replaces that list with a HellasForms-controlled whitelist that includes:

All standard fang/bite moves

Additional Hellas-exclusive jaw/fang attacks

Selected non-standard bite-themed moves

The Strong Jaw multiplier remains unchanged:

1.5× power

Accuracy is not modified.

Injection type

Mixin target:
StrongJaw.class

Overwritten method:
modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a)

Annotation:
@Overwrite(remap = false)

Meaning:

Completely replaces Pixelmon’s default Strong Jaw implementation.

No vanilla move whitelist is preserved.

HellasForms assumes full control over Strong Jaw behavior.

Trigger condition

The Strong Jaw boost applies when:

a.getMove().getAttackName()

matches any entry in the custom biting-move whitelist.

Matching is:

exact string match

case-sensitive

name-based (not tag/type-based)

Strong Jaw multiplier

If the move name is in the whitelist:

power = floor(power × 1.5)

Accuracy is unchanged.

Return:
new int[]{power, accuracy}

If not in the list:

power unchanged

accuracy unchanged

Custom Strong Jaw whitelist

The following moves receive the Strong Jaw bonus:

Bite
Bleeding Jaw
Crunch
Draconic Jaw
Fire Fang
Hyper Fang
Ice Fang
Poison Fang
Psychic Fangs
Thunder Fang
Fishious Rend
Jaw Lock

Includes:

Standard fang/bite moves (Bite, Crunch, elemental fangs)

Vanilla jaw-themed moves (Jaw Lock, Fishious Rend)

Hellas-exclusive moves (Bleeding Jaw, Draconic Jaw)

Behavioral consequences

Because this is a full overwrite:

Pixelmon’s internal Strong Jaw list is completely replaced.

Any new fang/bite moves added by Pixelmon will NOT receive Strong Jaw automatically.

All recognition must be maintained manually in this whitelist.

This ensures:

deterministic Hellas behavior

consistent balance across custom moves

Non-negotiable contract (do not change)

Must remain a full overwrite of StrongJaw.modifyPowerAndAccuracyUser.

Must apply exactly 1.5× power multiplier to all listed moves.

Must not modify accuracy.

Must rely on exact move-name matching.

Must not call original StrongJaw logic.

Must include Hellas-exclusive biting moves.

Must not remove existing entries without explicit balance decision.

Must not convert to tag/type detection unless the entire system is redesigned.

Maintenance notes

Future Pixelmon updates:

New bite/fang moves must be added manually to this list.

Localization:

Uses internal move registry names, not translated names.

Mod conflicts:

Any other mod overwriting Strong Jaw will conflict.

This mixin assumes exclusive control of Strong Jaw behavior.

Summary

MixinStrongJaw fully replaces Pixelmon’s Strong Jaw whitelist with a HellasForms-controlled move list and applies the standard 1.5× biting-move damage boost to all listed moves, ensuring custom Hellas fang/jaw attacks properly benefit from the ability.