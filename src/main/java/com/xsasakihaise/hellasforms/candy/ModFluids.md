HellasForms Fluid System (Candy / Attribute Fluids)

File: ModFluids.java
Module: hellasforms.candy
Purpose: Registers and exposes all HellasForms custom fluid sets used for EXP and attribute systems (EXP + STR/DEX/CON/INT/WIS/CHA).

This system defines a complete Forge fluid pipeline per fluid:

source fluid

flowing fluid

world block

bucket item

All fluids are registered via DeferredRegister and must be registered on the mod event bus.

Overview

Each HellasForms fluid is represented by a FluidSet containing:

Still/source fluid instance

Flowing fluid instance

World block representation

Bucket item

Shared fluid properties

These fluids are intended for:

EXP transfer systems

Attribute infusion systems

Machine/storage interaction

World placement (optional)

They are not vanilla water replacements and are fully isolated mod fluids.

Registered fluids

The following fluid sets are created:

liquid_exp_candy

liquid_str

liquid_dex

liquid_con

liquid_int

liquid_wis

liquid_cha

Each uses identical physics and behavior, differing only by registry name and textures.

Fluid properties

All fluids use a shared configuration pattern.

Physics and behavior

levelDecreasePerBlock: 1

slopeFindDistance: 4

explosionResistance: 100

behaves similar to water

high blast resistance

no drops from fluid blocks

These values must remain consistent across all attribute fluids unless intentionally rebalanced.

Fluid block properties

Fluid blocks use:

Material: WATER
Collision: none
Strength: 100
Drops: none

Meaning:

Entities can pass through

Not intended for mining drops

Extremely blast resistant

Treated as a stable system fluid

Bucket properties

All fluids provide a bucket item:

Stack size: 1

Creative tab: MISC

Craft remainder: empty bucket

Buckets are tied directly to their source fluid and must remain linked.

Registration architecture

Uses three DeferredRegisters:

FLUIDS → ForgeRegistries.FLUIDS
BLOCKS → ForgeRegistries.BLOCKS
ITEMS → ForgeRegistries.ITEMS

All must be registered via:

ModFluids.register(IEventBus eventBus)

This must be called during mod initialization or nothing will exist in-game.

FluidSet structure

Each FluidSet contains:

name → registry base name
source → still fluid
flowing → flowing variant
block → world block
bucket → bucket item
properties → ForgeFlowingFluid.Properties

Getter methods expose all components for use by:

machines

recipes

pipelines

world placement

attribute systems

Texture requirements

Each fluid requires two textures:

block/<name>_still
block/<name>_flow

Located under:
assets/hellasforms/textures/

Missing textures will result in:

purple/black fluid

render errors

Non-negotiable contract (do not change)

Every fluid must register:

source

flowing

block

bucket

All registrations must occur through DeferredRegister.

register(eventBus) must register FLUIDS, BLOCKS, and ITEMS.

Buckets must remain linked to their fluid.

Explosion resistance must remain 100 unless intentionally rebalanced.

Fluids must remain water-like (Material.WATER + no collision).

Getter methods must continue returning registry-backed instances.

Intended system role

These fluids function as:

Liquid stat/EXP carriers for HellasForms progression systems.

They are designed for:

storage in tanks

transfer through machines/pipes

conversion into EXP or attributes

controlled world placement

They are not decorative-only fluids and must remain compatible with Forge fluid systems and automation.