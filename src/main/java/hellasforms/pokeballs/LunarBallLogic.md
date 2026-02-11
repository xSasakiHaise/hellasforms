# LunarBallLogic – Current Behavior

`LunarBallLogic` calculates a **catch-rate multiplier** based on the **time of day** and the **current moon phase**.

- If the `World` reference is `null`, it returns `1.0`.
- During **daytime**, it always returns `1.0` (no bonus).
- During **nighttime**, it:
    - Reads the Minecraft moon phase (`0–7`, where `0` is full moon and `4` is new moon).
    - Computes the symmetric distance from the full moon (`d` in `0–4`).
    - Calculates a multiplier using the formula:
      ```
      mult = 6.0 × 0.5^(d^1.394)
      ```
    - Clamps the result so it is **never below `0.05`** and **never above `6.0`**.

The final value returned is the capture multiplier to apply at the player’s location for the current night and moon phase.
