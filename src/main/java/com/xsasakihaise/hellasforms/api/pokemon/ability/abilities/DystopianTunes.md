Effects

Incoming sound-based moves

Condition: Attack.isSoundBased() == true

Result: Final incoming damage is multiplied by 0.5 (50% reduction).

Applies to all sound-tagged moves regardless of type, category, or source.

User claw-based moves

Condition: Move name matches one of the following:

Dragon Claw

Shadow Claw

Metal Claw

Crush Claw

Dire Claw

Rabid Claw

Result: Base power is multiplied by 1.5 before damage calculation.

User sound-based moves

Condition: Attack.isSoundBased() == true

Result: Base power is multiplied by 1.3 before damage calculation.

Stacking / Interaction Rules

Claw and sound checks are independent.

If a move is both sound-based and listed as a claw move, both multipliers apply (1.5 × 1.3).

Power modifiers apply at the power calculation stage, not as final damage multipliers.

Incoming sound reduction applies at the final damage stage.