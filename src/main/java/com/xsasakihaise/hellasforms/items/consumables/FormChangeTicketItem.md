FormChangeTicketItem (Consumable)

File: FormChangeTicketItem.java
Package: com.xsasakihaise.hellasforms.items.consumables
Extends: PokemonInteractItem
Purpose: Consumable “ticket” that swaps a Pokémon into a configured target form, but only if that form exists for the species and the Pokémon is not already in it. Fails safely without consuming when the change is impossible.

Overview

This item represents a controlled form-swap mechanism for HellasForms content (and any Pixelmon species/forms that exist in the registry).

Each item instance is parameterized by:

targetForm: the exact form name to switch into

successTranslation: translation key shown on successful change

The item does not attempt to create missing forms or force invalid form IDs. It only performs a valid form swap.

Effect logic

Entry point:
applyEffect(PlayerEntity player, Pokemon pokemon, PixelmonEntity entity, ItemStack stack)

Step 1: Validate target form exists for the species

Checks whether the Pokémon’s species defines the requested form:
pokemon.getSpecies().getForm(targetForm) != null

If the form does not exist:

return false (no change)

Step 2: Validate Pokémon is not already in that form

Checks current form:
pokemon.getForm().isForm(targetForm)

If already in target form:

return false (no change)

Step 3: Apply form change

If both checks pass:

pokemon.setForm(targetForm)

pokemon.markDirty()

return true

This is a direct form swap and does not run additional battle-evolution logic.

Persistence / sync

On success:

pokemon.markDirty() is called to persist and propagate the form change.

No additional sync steps are performed here.

Messaging
Success message

Translation key: successTranslation
Arguments: Pokémon display name

Failure messages

Failure messaging is specialized to provide actionable feedback:

If target form does not exist for the species:

item.hellasforms.form_change.missing
Arguments: Pokémon display name, targetForm

If Pokémon is already in the target form:

item.hellasforms.form_change.already
Arguments: Pokémon display name, targetForm

Otherwise:

falls back to the parent (PokemonInteractItem) failure message.

Note:

These failure cases correspond exactly to the conditions checked in applyEffect.

Non-negotiable contract (do not change)

The item must only succeed if the target form exists for the Pokémon’s species.

The item must fail if the Pokémon is already in the target form.

The item must not consume or apply partial changes on failure (applyEffect returns false).

On success, it must:

setForm(targetForm)

markDirty()

Failure messages must remain consistent:

missing form → item.hellasforms.form_change.missing

already in form → item.hellasforms.form_change.already

targetForm matching must remain exact string-based form lookup.

Summary

FormChangeTicketItem is a safe, parameterized form-swap ticket:

Swaps to a configured target form when valid

Fails cleanly when the form is missing or already active

Persists changes with markDirty

Provides explicit failure messages so players understand why the swap did not happen