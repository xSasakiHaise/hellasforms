ReturnItemsListener (Battle Held-Item Preservation)

File: ReturnItemsListener.java
Package: com.xsasakihaise.hellasforms.listener
Type: Forge event listener (Pixelmon battle event)
Purpose: Ensures that in non-wild (staff-organised / player-vs-player / trainer) battles, participants’ Pokémon are flagged to return held items after the battle. If either side includes a wild participant, the listener does nothing so vanilla wild-battle behavior remains unchanged.

Overview

Pixelmon battles can involve mechanics that temporarily remove, steal, or otherwise affect held items. This listener enforces a rule for “organised” duels:

If the battle is NOT a wild encounter, all Pokémon in both teams are configured to return their held items after the battle.

Wild encounters are explicitly excluded so standard gameplay (including item loss/steal rules for wild scenarios) is preserved.

Activation

Triggered on:

BattleStartedEvent

This runs at battle initialization, before combat resolution, so flags are applied in time for the battle system to respect them.

Wild battle exclusion logic

The listener first checks both teams:

Iterate over event.getTeamOne()

If any participant is a WildPixelmonParticipant → return immediately

Iterate over event.getTeamTwo()

If any participant is a WildPixelmonParticipant → return immediately

Result:

If either team contains any wild participant, no held-item return flags are set for anyone in the battle.

This “early return” design means wild battles remain completely untouched.

Held-item return enforcement (non-wild battles only)

If neither team contains a WildPixelmonParticipant:

For every BattleParticipant in Team One:

For every PixelmonWrapper in participant.allPokemon:

call pixelmonWrapper.enableReturnHeldItem()

Repeat the same process for Team Two.

Effect:

All Pokémon in the battle (both teams) are marked such that held items should be returned after the battle ends.

Scope and intent

Applies to:

Player vs Player battles

Trainer fights

Staff-run duels

Any battle without wild participants

Does not apply to:

Any battle containing a wild participant on either side

This preserves the intended separation:

Organised battles are “safe” for held items

Wild encounters keep vanilla risk/behavior

Non-negotiable contract (do not change)

Must trigger on BattleStartedEvent.

Must exit early (do nothing) if any participant on either team is WildPixelmonParticipant.

Must apply enableReturnHeldItem() to every PixelmonWrapper in allPokemon for both teams when not wild.

Must not partially apply flags (either the battle is eligible and all wrappers are flagged, or it is wild and none are).

Must not alter wild encounter behavior.

Summary

ReturnItemsListener enforces held-item safety for non-wild battles by enabling Pixelmon’s “return held item” behavior for every participant Pokémon at battle start, while explicitly skipping any battle that involves a wild participant to preserve vanilla wild mechanics.