Drop Table JSON (Pokémon-Specific Loot Definitions)

File type: JSON array
Purpose: Defines per-Pokémon loot tables where each entry maps a specific Pokémon identifier (including palette qualifiers) to a list of item drops with quantity ranges and independent drop chances.

This format is intended for a HellasForms/Pixelmon drop system that rolls each listed item independently when the matching Pokémon is defeated / harvested / processed (depending on the consumer of this JSON).

Top-level structure

The document is a JSON array of objects.

Each object represents one “drop profile”:

pokemon: String identifier for the Pokémon target

items: Array of item drop definitions

Pokémon identifier format

Field: pokemon

Examples in this file:

"Dragonite palette:ashen"

"Salamence palette:alter"

"Hydreigon palette:ashen"

"Carbink"

Meaning:

The system expects a free-form identifier string that likely matches:

species name, and optionally

palette qualifier in the form palette:<name>

Contract:

Matching is assumed to be exact string-based unless the consuming system normalizes it.

If palette qualifiers are used, the consumer must treat them as part of the match key.

Item drop definition format

Each entry in items contains:

item: String item registry ID (namespace:id)

min: integer minimum quantity

max: integer maximum quantity

chance: decimal probability (0.0 to 1.0)

Examples:

pixelmon:amethyst

minecraft:dragon_head

hellasforms:hellas_amethyst

Roll semantics

Each item in the items list is intended to be rolled independently.

For a given Pokémon drop profile:

For each item entry:

Roll random chance against chance

If roll succeeds:

Generate a quantity between min and max (inclusive)

Add generated stack to drops

Important implications:

Multiple items can drop together from the same Pokémon (not mutually exclusive).

Items with chance 1.0 always drop.

Items with chance below 1.0 are bonus/rare drops.

Quantity rules

min and max define the inclusive range.

min must be >= 1 for meaningful drops.

max must be >= min.

If the consumer does validation, invalid ranges should reject or clamp.

Profiles contained in this file
Dragonite palette:ashen

Guaranteed:

pixelmon:amethyst (1–3)

Optional:

pixelmon:haban_berry (1–2) @ 50%

pixelmon:dragon_fang (1) @ 10%

minecraft:dragon_head (1) @ 30%

Salamence palette:alter

Guaranteed:

minecraft:gold_ingot (1–3)

Optional:

pixelmon:yache_berry (1–2) @ 50%

pixelmon:haban_berry (1–2) @ 30%

pixelmon:dragon_fang (1) @ 10%

minecraft:dragon_head (1) @ 30%

Hydreigon palette:ashen

Guaranteed:

minecraft:basalt (2–4)

Optional:

minecraft:leather (1–3) @ 50%

minecraft:gold_ingot (1–3) @ 30%

pixelmon:dragon_fang (1) @ 10%

minecraft:dragon_head (1) @ 30%

Carbink

Carbink is configured as a “gem pinata” profile:

Every listed hellasforms:* gem/mineral item is set to:

min 1, max 3, chance 1.0

Meaning:

Carbink will drop 1–3 of every listed mineral item simultaneously if the system truly rolls each entry independently.

This is likely intentional for testing or for a special event/loot mode, but it is extremely high output in normal gameplay.

Maintenance and balance warnings

Carbink profile currently guarantees dozens of stacks per kill. If this is meant to be “one of many” rather than “all of them”, the consumer needs a different schema (e.g., weighted pool + roll count) instead of independent rolls.

Duplicate item definitions (same item repeated) would cause multiple independent drops; none are present here in the shown snippet.

The consumer should validate namespaces exist (minecraft:, pixelmon:, hellasforms:).

Non-negotiable contract (if you keep this schema)

pokemon is the key used for matching the target Pokémon (including palette qualifiers).

Each item entry is an independent roll governed by chance.

Quantity is generated within [min, max] inclusive upon success.

chance is interpreted as a probability from 0.0 to 1.0.

If you tell me which loader consumes this (Pixelmon drops, HellasMineralogy, a custom loot injector, etc.), I can also write the exact “schema contract” section you should put at the top of the JSON folder (including whether pokemon is case-sensitive and how palette matching is parsed).