# PokemonEggItem – Mystery Egg Quest Item (Egg Modifier Update)

## What this class is

`PokemonEggItem` is a **custom Pixelmon QuestItem** representing the “mystery eggs” used on Hellas servers.

When used, the item:
- Loads a **JSON-defined loot table**
- Randomly selects a Pokémon spec
- Executes a **Pixelmon `/pokegive` command**
- **Always grants the Pokémon as an egg** (via the `egg` modifier)
- Optionally grants a **shiny egg**
- Consumes the item on success

The system is **data-driven**: Java defines behavior, JSON defines content.

---

## Key change (important)

**All Pokémon are now always given with the `egg` modifier.**

Command format:

pokegive <player> <pokemon> egg [shiny]

## Design intent (summary)

One reusable QuestItem for all egg types

All reward pools defined in JSON

Pokémon are always granted as eggs

Shiny behavior is deterministic and item-based

Uses Pixelmon commands for maximum cross-version stability