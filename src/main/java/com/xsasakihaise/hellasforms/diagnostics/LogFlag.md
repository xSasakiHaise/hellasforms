LogFlag (Diagnostics Logging Categories)

File: LogFlag.java
Module: hellasforms.diagnostics
Type: Enum
Purpose: Provides standardized logging category flags used throughout HellasForms debugging and tracing systems.

Each flag identifies the subsystem that produced a log message, allowing fast filtering and structured debugging when reviewing logs from users or servers.

Overview

LogFlag defines a fixed set of logging categories.
Each category renders as a formatted uppercase token that is appended to the global debug prefix.

Used primarily by:

DebuggingHooks

tracing wrappers

subsystem logging

crash diagnostics

These flags are purely for log structure and do not affect runtime logic.

Available flags

CORE
Primary mod logic and initialization flow.

API
External-facing API hooks and integrations.

CONFIG
Configuration loading, parsing, validation, and migration.

COMMANDS
Command registration and execution.

ITEMS
Item registration, candy systems, and related mechanics.

BATTLES
Battle logic, abilities, and combat interactions.

LISTENERS
Forge or Pixelmon event listeners.

DIAGNOSTICS
Debugging system output, lifecycle tracing, and runtime environment logging.

Formatting behavior

Each flag renders as:

[FLAG]

Example:
[CORE]
[ITEMS]
[BATTLES]

Formatting is produced via:
format()

Which returns:
"[" + token + "]"

toString() returns the same formatted value.

Usage with debug prefix

When used with DebuggingHooks, final log output follows:

[HellasForms Debug][FLAG]

Example:
[HellasForms Debug][CORE] >>> Entering registry setup
[HellasForms Debug][DIAGNOSTICS] Event fired: FMLCommonSetupEvent

This consistent structure allows:

easy grep/filtering

subsystem isolation

readable crash logs

fast identification of failure origin

Stability contract (do not change)

Enum names must remain stable (used across logging calls).

Token strings must remain uppercase.

format() must return bracketed token form.

toString() must return format().

Existing flags must not be renamed or removed.

New flags may be added only if a new subsystem requires distinct filtering.

Intended role

LogFlag exists solely to provide:

Structured, filterable, subsystem-aware logging across HellasForms.

It ensures crash logs and debug output can be traced back to the exact system that produced them without ambiguity.