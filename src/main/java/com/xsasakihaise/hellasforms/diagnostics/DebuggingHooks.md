DebuggingHooks (Diagnostics Utility)

File: DebuggingHooks.java
Module: hellasforms.diagnostics
Purpose: Central debugging utility that logs runtime details, lifecycle phase markers, and provides a safe wrapper for tracing initialization stages with explicit enter/exit timing and fatal crash context.

This class is designed to make mod-load crashes self-explanatory by showing:

which Forge lifecycle events fired

what stage wrapper failed

JVM/runtime launch context

uncaught exceptions with thread information

Initialization

Entry point:
initialize(Logger logger)

Behavior:

Requires a non-null logger.

Runs only once per JVM session (guarded by an AtomicBoolean).

On first call, performs three actions:

logs runtime details

installs a global uncaught exception handler

installs Forge lifecycle logging hooks

Subsequent calls are ignored.

Runtime details logging

Executed during initialize:

logRuntimeDetails(LogFlag.DIAGNOSTICS, logger)

Logs the following at INFO level:

Java version (System property "java.version")

JVM name

JVM vendor

JVM launch arguments (RuntimeMXBean input arguments)

This exists to capture environment context for crash reports (Java mismatch, bad JVM flags, etc.).

Global uncaught exception handler

Installed during initialize:

installExceptionHandler(LogFlag.DIAGNOSTICS, logger)

Behavior:

Overrides the JVM default uncaught exception handler.

On any uncaught exception:

logs a FATAL entry containing:

prefix (HellasForms Debug + flag)

thread name

exception summary string

full stack trace

If a previous handler existed, it is called afterward to preserve upstream handling.

This is intentionally global to catch errors outside normal Forge callback boundaries.

Lifecycle event logging

Installed during initialize:

installLifecycleLogging(Logger logger)

Hooks into both:

Mod event bus (FMLJavaModLoadingContext mod bus)

MinecraftForge.EVENT_BUS (server lifecycle)

Logged markers (INFO) include:

Mod lifecycle:

FMLCommonSetupEvent

FMLClientSetupEvent

FMLLoadCompleteEvent

Server lifecycle:

FMLServerStartingEvent

FMLServerStartedEvent

FMLServerStoppingEvent

FMLServerStoppedEvent

Each marker logs:
"Event fired: <EventName>"

Purpose:

Provide a breadcrumb trail of lifecycle progression.

Identify which phase was last reached before a crash.

Stage tracing wrapper

Primary utility method:

runWithTracing(LogFlag flag, String stageName, Logger logger, ThrowingRunnable runnable)

Convenience overload:
runWithTracing(String stageName, Logger logger, ThrowingRunnable runnable)
(default flag is LogFlag.CORE)

Behavior

Logs stage entry at INFO:
">>> Entering <stageName>"

Executes the runnable

Logs stage completion at INFO with elapsed time:
"<<< Finished <stageName> in <ms> ms"

If any exception or error occurs:

logs a FATAL entry identifying the stage and full throwable

rethrows the exception:

RuntimeException is rethrown as-is

otherwise it is wrapped in a new RuntimeException

Purpose

This is intended for guarding high-risk init sections (registries, config IO, data migrations, Pixelmon hooks) so failures are pinned to a named stage with timing and stack trace.

Logging prefix format

All logging uses a consistent prefix:

"[HellasForms Debug]" + flag.format()

The LogFlag controls category formatting and filtering (flag definitions are external to this class).

Thread safety and stability

initialize() is idempotent due to AtomicBoolean guard.

The class is static-only and cannot be instantiated.

The uncaught exception handler is global and must remain stable to avoid interfering with Forge’s own crash pipeline.