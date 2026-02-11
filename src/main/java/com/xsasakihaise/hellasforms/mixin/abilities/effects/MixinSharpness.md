MixinSharpness (Sharpness Ability Extension)

File: MixinSharpness.java
Package: com.xsasakihaise.hellasforms.mixin.abilities.effects
Type: Sponge Mixin overwrite
Target: Sharpness ability (Pixelmon)
Purpose: Replaces Pixelmon’s default Sharpness move whitelist with an expanded HellasForms slicing-move list and applies the Sharpness damage bonus to all listed moves.

This mixin fully overwrites Sharpness’ power modification logic.

Overview

This mixin modifies the behavior of the Sharpness ability by replacing its internal move whitelist with a custom list that includes:

All intended slicing moves

Additional Hellas-exclusive slicing moves

Several moves not included in default Pixelmon Sharpness handling

The Sharpness multiplier remains:
1.5× power

No accuracy modification is applied.

Injection type

Mixin target:
Sharpness.class

Method overwritten:
modifyPowerAndAccuracyUser(int power, int accuracy, PixelmonWrapper user, PixelmonWrapper target, Attack a)

Annotation:
@Overwrite(remap = false)

Meaning:

Completely replaces Pixelmon’s original Sharpness implementation.

No original whitelist or logic is preserved.

This is a full behavioral override, not an extension.

Trigger condition

The Sharpness bonus applies when:

a.getMove().getAttackName()

matches any entry in the custom whitelist.

Matching is:

exact string match

case-sensitive

name-based (not type/category/tag based)

Sharpness multiplier

If the move name is present in the whitelist:

power = floor(power × 1.5)

Accuracy remains unchanged.

Returned result:
new int[]{power, accuracy}

If the move is not in the whitelist:

power unchanged

accuracy unchanged

Custom Sharpness whitelist

The following moves are treated as Sharpness-eligible:

Aerial Ace
Aegis Driver
Air Cutter
Air Slash
Aqua Cutter
Behemoth Blade
Bitter Blade
Ceaseless Edge
Cross Poison
Cut
Cutting Edge
Fury Cutter
Isbrand
Kowtow Cleave
Leaf Blade
Night Slash
Petal Blade
Population Bomb
Psyblade
Psycho Cut
Razor Leaf
Razor Shell
Sacred Sword
Slash
Snowstorm Fury
Solar Blade
Stone Axe
X-Scissor

Includes:

Vanilla slicing moves

Extended interpretation slicing moves

Hellas-exclusive moves (e.g. Aegis Driver, Isbrand, Snowstorm Fury, Cutting Edge)

Behavioral consequences

Because this is a full overwrite:

Any updates to Pixelmon’s internal Sharpness logic are ignored.

Any new official slicing moves added by Pixelmon will NOT receive Sharpness unless added here.

Tag-based slicing systems (if added upstream later) are bypassed.

This is intentional to keep HellasForms slicing logic deterministic and fully controlled.

Non-negotiable contract (do not change)

Must remain a full overwrite of Sharpness.modifyPowerAndAccuracyUser.

Must apply 1.5× power multiplier to all listed moves.

Must not change accuracy values.

Must rely on exact move-name matching.

Must not call super or original Sharpness logic.

Must include Hellas-exclusive slicing moves.

Must not remove existing entries without explicit balance decision.

Must not convert to tag/type-based detection unless the entire system is redesigned.

Risks / maintenance notes

Future Pixelmon updates:

New slicing moves will not automatically receive Sharpness.

Must be manually added to whitelist.

Localization:

Must use internal move names, not translated names.

Conflicts:

Any other mod overwriting Sharpness may conflict.

This mixin assumes exclusive control over Sharpness behavior.

Summary

MixinSharpness fully replaces Pixelmon’s Sharpness logic with a HellasForms-controlled whitelist of slicing moves and applies a flat 1.5× power multiplier to those moves, ensuring consistent behavior across vanilla and custom Hellas move sets.