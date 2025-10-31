package com.xsasakihaise.hellasforms.diagnostics;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Central location for all debugging helpers. This class tries to give as much
 * context as possible when the mod loads so that a crash immediately points to
 * the lifecycle phase that triggered it.
 */
public final class DebuggingHooks {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final String DEBUG_PREFIX = "[HellasForms Debug]";

    private DebuggingHooks() {
        // Utility class
    }

    public static void initialize(Logger logger) {
        Objects.requireNonNull(logger, "logger");
        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        logRuntimeDetails(LogFlag.DIAGNOSTICS, logger);
        installExceptionHandler(LogFlag.DIAGNOSTICS, logger);
        installLifecycleLogging(logger);
    }

    public static void runWithTracing(String stageName, Logger logger, ThrowingRunnable runnable) {
        runWithTracing(LogFlag.CORE, stageName, logger, runnable);
    }

    public static void runWithTracing(LogFlag flag, String stageName, Logger logger, ThrowingRunnable runnable) {
        Objects.requireNonNull(stageName, "stageName");
        Objects.requireNonNull(logger, "logger");
        Objects.requireNonNull(runnable, "runnable");

        String prefix = formatPrefix(flag);
        logger.info("{} >>> Entering {}", prefix, stageName);
        Instant start = Instant.now();
        try {
            runnable.run();
            Duration elapsed = Duration.between(start, Instant.now());
            logger.info("{} <<< Finished {} in {} ms", prefix, stageName, elapsed.toMillis());
        } catch (Throwable throwable) {
            logStageFailure(flag, stageName, logger, throwable);
            throw rethrow(throwable);
        }
    }

    private static void logStageFailure(LogFlag flag, String stageName, Logger logger, Throwable throwable) {
        logger.fatal("{} !!! Failure inside {}: {}", formatPrefix(flag), stageName, throwable.toString(), throwable);
    }

    private static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(throwable);
    }

    private static void installExceptionHandler(LogFlag flag, Logger logger) {
        Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.fatal("{} Uncaught exception on thread '{}': {}", formatPrefix(flag), thread.getName(), throwable.toString(), throwable);
            if (previous != null) {
                previous.uncaughtException(thread, throwable);
            }
        });
    }

    private static void installLifecycleLogging(Logger logger) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener((FMLCommonSetupEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLCommonSetupEvent", logger));
        modEventBus.addListener((FMLClientSetupEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLClientSetupEvent", logger));
        modEventBus.addListener((FMLLoadCompleteEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLLoadCompleteEvent", logger));

        MinecraftForge.EVENT_BUS.addListener((FMLServerStartingEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLServerStartingEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStartedEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLServerStartedEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStoppingEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLServerStoppingEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStoppedEvent event) -> logLifecycleMarker(LogFlag.DIAGNOSTICS, "FMLServerStoppedEvent", logger));
    }

    private static void logLifecycleMarker(LogFlag flag, String stage, Logger logger) {
        logger.info("{} Event fired: {}", formatPrefix(flag), stage);
    }

    private static void logRuntimeDetails(LogFlag flag, Logger logger) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArguments = runtimeMXBean.getInputArguments();
        String prefix = formatPrefix(flag);
        logger.info("{} Java version: {}", prefix, System.getProperty("java.version"));
        logger.info("{} JVM name: {}", prefix, runtimeMXBean.getVmName());
        logger.info("{} JVM vendor: {}", prefix, runtimeMXBean.getVmVendor());
        logger.info("{} Launch arguments: {}", prefix, inputArguments);
    }

    private static String formatPrefix(LogFlag flag) {
        return DEBUG_PREFIX + flag.format();
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
