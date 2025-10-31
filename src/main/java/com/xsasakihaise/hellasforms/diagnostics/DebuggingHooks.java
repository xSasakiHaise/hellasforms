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

        logRuntimeDetails(logger);
        installExceptionHandler(logger);
        installLifecycleLogging(logger);
    }

    public static void runWithTracing(String stageName, Logger logger, ThrowingRunnable runnable) {
        Objects.requireNonNull(stageName, "stageName");
        Objects.requireNonNull(logger, "logger");
        Objects.requireNonNull(runnable, "runnable");

        logger.info("{} >>> Entering {}", DEBUG_PREFIX, stageName);
        Instant start = Instant.now();
        try {
            runnable.run();
            Duration elapsed = Duration.between(start, Instant.now());
            logger.info("{} <<< Finished {} in {} ms", DEBUG_PREFIX, stageName, elapsed.toMillis());
        } catch (Throwable throwable) {
            logStageFailure(stageName, logger, throwable);
            throw rethrow(throwable);
        }
    }

    private static void logStageFailure(String stageName, Logger logger, Throwable throwable) {
        logger.fatal("{} !!! Failure inside {}: {}", DEBUG_PREFIX, stageName, throwable.toString(), throwable);
    }

    private static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(throwable);
    }

    private static void installExceptionHandler(Logger logger) {
        Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            logger.fatal("{} Uncaught exception on thread '{}': {}", DEBUG_PREFIX, thread.getName(), throwable.toString(), throwable);
            if (previous != null) {
                previous.uncaughtException(thread, throwable);
            }
        });
    }

    private static void installLifecycleLogging(Logger logger) {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener((FMLCommonSetupEvent event) -> logLifecycleMarker("FMLCommonSetupEvent", logger));
        modEventBus.addListener((FMLClientSetupEvent event) -> logLifecycleMarker("FMLClientSetupEvent", logger));
        modEventBus.addListener((FMLLoadCompleteEvent event) -> logLifecycleMarker("FMLLoadCompleteEvent", logger));

        MinecraftForge.EVENT_BUS.addListener((FMLServerStartingEvent event) -> logLifecycleMarker("FMLServerStartingEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStartedEvent event) -> logLifecycleMarker("FMLServerStartedEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStoppingEvent event) -> logLifecycleMarker("FMLServerStoppingEvent", logger));
        MinecraftForge.EVENT_BUS.addListener((FMLServerStoppedEvent event) -> logLifecycleMarker("FMLServerStoppedEvent", logger));
    }

    private static void logLifecycleMarker(String stage, Logger logger) {
        logger.info("{} Event fired: {}", DEBUG_PREFIX, stage);
    }

    private static void logRuntimeDetails(Logger logger) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArguments = runtimeMXBean.getInputArguments();
        logger.info("{} Java version: {}", DEBUG_PREFIX, System.getProperty("java.version"));
        logger.info("{} JVM name: {}", DEBUG_PREFIX, runtimeMXBean.getVmName());
        logger.info("{} JVM vendor: {}", DEBUG_PREFIX, runtimeMXBean.getVmVendor());
        logger.info("{} Launch arguments: {}", DEBUG_PREFIX, inputArguments);
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
