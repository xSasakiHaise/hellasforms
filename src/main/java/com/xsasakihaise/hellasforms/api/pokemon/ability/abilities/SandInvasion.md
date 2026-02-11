Sand Invasion (Ability)

Type: Passive weather + stat modifier ability
Base class: AbstractAbility
Intent: On switch-in, attempt to start Sandstorm. While Sandstorm is active, the user’s Speed is doubled.

Switch-in effect

Triggered when the Pokémon with Sand Invasion enters battle via:

applySwitchInEffect(PixelmonWrapper newPokemon)

Preconditions

If newPokemon.bc is null, the ability does nothing.

Weather placement

On switch-in, the ability creates a new Sandstorm instance and attempts to apply it as the global weather.

Sandstorm is started only if both conditions are true:

Current weather (ignoring ability overrides) is NOT already Sandstorm.
Condition: globalStatusController.getWeatherIgnoreAbility() is not an instance of Sandstorm

The battle rules allow weather to change to Sandstorm.
Condition: globalStatusController.canWeatherChange(sandstorm) returns true

If Sandstorm starts successfully

The Sandstorm duration is initialized using sandstorm.setStartTurns(newPokemon).

Sandstorm is added as a global status via newPokemon.addGlobalStatus(sandstorm).

A broadcast message is sent:
Key: pixelmon.abilities.sandstream
Args: newPokemon nickname

Notes:

If Sandstorm is already active, this ability will not refresh or overwrite it.

If weather change is blocked by engine rules, nothing happens.

Stat modification

Triggered via:

modifyStats(PixelmonWrapper user, int[] stats)

Condition

Speed modification applies only if:

user.bc is not null
AND

Current weather (ignoring ability overrides) is Sandstorm.
Condition: globalStatusController.getWeatherIgnoreAbility() is an instance of Sandstorm

Effect

The user’s Speed stat is multiplied by 2.0.

Implementation detail:

The Speed index is obtained through BattleStatsType.SPEED.getStatIndex().

Speed is cast back to int after multiplication.