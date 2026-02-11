# HellasForms Interaction Compatibility Layer (Bottle Caps)

## What this is

This class implements a **compatibility layer for Pixelmon interactions**, originally written to ensure that the **Bottle Cap interaction** can be registered across **multiple Pixelmon API versions** without maintaining separate code paths or builds.

In short:  
**One HellasForms JAR → works with several Pixelmon interaction registries.**

---

## Why this exists

Pixelmon has changed how interactions are registered over time:

- Different registry classes:
    - `InteractionRegistry`
    - `InteractionController`
- Different method names:
    - `register(...)`
    - `registerInteraction(...)`
- Different parameter types:
    - `IInteraction`
    - `Supplier<IInteraction>`

Hard-coding against one of these would break compatibility with other versions.  
This class avoids that by using **reflection with fallbacks**.

---

## How it works

### Registration timing

The interaction is registered during `FMLCommonSetupEvent` using `enqueueWork` to ensure Pixelmon’s registries are fully initialized before registration occurs.

---

### Fallback-based registration

The registration logic attempts the following in order:

1. `InteractionRegistry.register(...)`
2. `InteractionController.register(...)`
3. `InteractionController.registerInteraction(...)`

The first successful call stops further attempts.  
If none succeed, an error is logged.

---

### Signature adaptation via reflection

For each candidate method, the code:

- Dynamically loads the registry class
- Searches for a public method with the expected name and **exactly one parameter**
- Adapts the argument based on parameter type:
    - `IInteraction` → pass the interaction directly
    - `Supplier<IInteraction>` → pass a lambda returning the interaction

This supports both eager and lazy registration APIs.

---

## Logging behavior

- **Debug (success):** logs which registry and method were used
- **Debug (failure):** logs reflection failures per attempt
- **Error (final):** emitted only if no compatible registry method is found

---

## Design intent

This code was written to:

- Keep **Bottle Cap interactions functional across Pixelmon versions**
- Avoid maintaining multiple Pixelmon-specific builds
- Gracefully handle API refactors
- Fail safely (log instead of crashing)

Although originally implemented for **Bottle Caps**, the compatibility logic can be reused for any future Pixelmon interaction in HellasForms.

---

## Summary

- Pixelmon interaction compatibility shim
- Reflection-based fallback registration
- Supports multiple registry locations and method signatures
- Designed for long-term maintenance and port preparation (e.g. NeoForge)
