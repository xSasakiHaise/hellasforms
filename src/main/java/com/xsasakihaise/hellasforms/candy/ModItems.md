HellasForms Candy & Attribute Items

File: ModItems.java
Module: hellasforms.candy
Purpose: Registers all HellasForms candy casts, pellets, and related crafting items used by the EXP and attribute infusion systems.

All items are registered through Forge DeferredRegister and must be registered on the mod event bus during initialization.

Overview

This registry defines the physical item layer for the HellasForms progression system.

Items here represent:

Candy casting molds (EXP sizes)

Pellet casting mold

Attribute pellets (STR/DEX/CON/INT/WIS/CHA)

These items are primarily used in crafting, infusion, and conversion systems tied to HellasForms stat and EXP mechanics.

They are not consumables by default and have no inherent logic beyond registration unless extended elsewhere.

Registration system

All items are registered via:

DeferredRegister<Item> → ForgeRegistries.ITEMS

Registration must occur through:

ModItems.register(IEventBus eventBus)

If this is not called during mod construction:

No candy or pellet items will exist in-game

Recipes referencing them will fail

Creative tab

All items use:

ItemGroup.TAB_MISC

This ensures:

Centralized grouping

Minimal creative tab clutter

Consistent placement for development/debug access

Candy casts (EXP molds)

Registered sizes:

candy_cast_xs

candy_cast_s

candy_cast_m

candy_cast_l

candy_cast_xl

candy_cast_xxl

candy_cast_xxxl

These represent casting molds for EXP candy creation across multiple size tiers.

They are intended to:

Act as crafting components

Gate EXP output size

Serve as machine inputs for candy production

They do not contain intrinsic EXP values themselves.

Pellet casting mold

pellet_cast

Used as the base mold for attribute pellet production.

Intended for:

Crafting recipes

Machine casting processes

Attribute infusion pipelines

Does not grant stats by itself.

Attribute pellets

Registered attribute pellets:

pellet_str

pellet_dex

pellet_con

pellet_int

pellet_wis

pellet_cha

Each pellet corresponds to a single attribute channel.

Intended use:

Stat infusion

Machine processing

Attribute storage

Conversion systems tied to HellasForms progression

These items represent physical stat units, not direct stat application unless handled elsewhere.

Properties

All items use:

Default properties:

Creative tab: MISC

No durability

No stack override

No built-in behavior

No container logic

Behavior is defined externally via:

recipes

machines

handlers

progression systems

Non-negotiable contract (do not change)

All items must remain registered via DeferredRegister.

register(eventBus) must be called during mod init.

Registry names must remain stable (used in recipes, NBT, machines).

Candy cast size names must not change.

Attribute pellet registry IDs must not change.

Items must remain logic-neutral base items unless intentionally extended.

Creative tab placement should remain consistent unless UI restructure is intentional.

Intended system role

These items form the physical crafting and storage layer of the HellasForms EXP and attribute ecosystem.

They are designed to interact with:

liquid EXP fluids

attribute fluids

machines

casting systems

progression mechanics

They must remain stable identifiers across the entire HellasForms progression pipeline.