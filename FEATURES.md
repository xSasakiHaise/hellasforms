# HellasForms

HellasForms is the content-heavy sidemod that powers the custom forms, items,
and battle rules that define the Hellas Pixelmon experience. It layers bespoke
abilities, moves, battle listeners and staff tools on top of Pixelmon so
builders can ship event lines, crate rewards and seasonal mechanics without
patching the base mod.

## Feature highlights
- **Custom battle content** – `battles.*` introduces move effects, multistage
  attacks and battlefield statuses referenced by Pixelmon JSON move configs, and
  `api.pokemon.ability.abilities` contains the bespoke abilities tied to the
  Hellasian forms.
- **Pokemon-facing consumables** – The `items.consumables` package provides
  reusable base logic (`PokemonInteractItem`) plus concrete items like EXP
  candies, EV maximisers and the ability patch remover that talk to Pokemon
  instances directly.
- **Quest and reward items** – Eggs, ores and battle pass vouchers (`BattlePassItem`,
  `PokemonEggItem`, `OreItem`) integrate with LuckPerms or `pokegive` to deliver
  server rewards while respecting Pixelmon's quest item semantics.
- **Staff-friendly interactions** – Interaction and listener packages register
  hooks like the bottle cap GUI, guaranteed item returns in staff battles, and
  growth rarity tuning so events behave predictably.
- **Candy crafting components** – `candy.ModItems` and `candy.ModFluids`
  register the casts, pellets, liquid EXP and attribute fluids used by the Hellas
  candy economy, complete with buckets and placeable fluid blocks.
- **Diagnostics and entitlement checks** – `diagnostics.DebuggingHooks` and the
  startup flow verify that HellasCore is present, gate features behind the
  "forms" entitlement key and emit verbose lifecycle logs to simplify support.

## Technical overview
The entry point is `com.xsasakihaise.hellasforms.HellasForms`, annotated with
`@Mod`, which installs the debugging hooks, registers all Forge/Pixelmon content
and wires listeners into the Forge and Pixelmon event buses. Pixelmon-specific
registrations (mega stones, badges, quest items) go through
`ItemRegistration.ITEMS` while standard Forge content uses deferred registers
(`ModItems`, `ModFluids`, `HellasForms.ITEMS`).

Domain-specific packages include:
- `battles.attacks` and `battles.status` for move logic and status effects.
- `api.pokemon.ability.abilities` for AbstractAbility implementations referenced
  by the custom forms.
- `items.consumables`/`items.heldItems`/`interactions` for reward items that
  manipulate Pokemon data via Pixelmon's API or command layer.
- `listener` for Forge/Pixelmon event handlers that tweak growth odds and battle
  behaviour.
- `diagnostics` for logging/tracing utilities invoked at every lifecycle stage.

Mixins under `mixin.abilities.effects` overwrite Pixelmon abilities such as
Strong Jaw and Sharpness so Hellas-exclusive moves are recognised without
maintaining a fork.

## Extension points
- **New Pokemon consumables** – Extend `PokemonInteractItem`, implement
  `applyEffect` and `getSuccessMessage`, then register the item via
  `HellasForms.ITEMS`. See `ExperienceCandyItem` for an example that works across
  multiple Pixelmon API versions.
- **Additional abilities or move effects** – Add classes under
  `api.pokemon.ability.abilities` or `battles.attacks.specialAttacks` and ensure
  the name is registered in `HellasForms` via `EffectTypeAdapter.EFFECTS`. The
  Pixelmon move JSONs can then reference the new identifiers.
- **Custom eggs or quest rewards** – Create a new item registration in
  `HellasForms` and supply a matching loot table JSON under
  `assets/hellasforms/eggs/<id>-data.json` (see `PokemonEggItem`).
- **Candy fluids** – Follow the pattern in `candy.ModFluids.registerFluid` to
  add a new `FluidSet`, then expose the `source`, `flowing`, `block` and
  `bucket` registry objects.

## Dependencies & environment
- Minecraft 1.16.5
- Forge 36.2.42 (configured via ForgeGradle 6 + Parchment mappings 2022.03.06)
- Pixelmon Reforged 9.1.12
- HellasControl (compile-time dependency, used for entitlement checks via
  `CoreCheck`)
- Java 8 toolchain

## Notes for future migration
- Battle move classes and mixins depend on Pixelmon internals like
  `EffectTypeAdapter`, `PixelmonWrapper` and concrete ability classes. Updating to
  a new Pixelmon major release will require auditing `battles.*` and
  `mixin.abilities.effects` to ensure method signatures and move IDs remain
  compatible.
- `items.consumables.ExperienceCandyItem` and `AbilityPatchRemoverItem` rely on
  reflection and command formats that may change between versions. Re-test these
  flows after every Pixelmon update.
- Bottle cap interactions (`interactions.InteractionBottleCap`) and the growth
  listener manipulate enums and GUIs that are tightly coupled to the 1.16.5
  client – any NeoForge/1.20 migration should revalidate those assumptions.
