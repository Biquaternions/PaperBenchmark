package me.biquaternions.benchmark.util;

import java.math.BigDecimal;

/**
 * Utility class to avoid as much JVM overhead as possible.
 * Also, Mojang's Profiler has lower performance impact in Paper compared to Fabric,
 *   this will allow us to have the same overhead on both platforms.
 */
public class ProfilerUtils {

    private static long HOLDER_START_TICK_BLOCK_ENTITIES;
    private static RollingAverage AVERAGE_TICK_BLOCK_ENTITIES;

    public static void recordStartTickBlockEntities() {
        HOLDER_START_TICK_BLOCK_ENTITIES = System.nanoTime();
    }

    public static void recordStopTickBlockEntities() {
        long diff = HOLDER_START_TICK_BLOCK_ENTITIES - System.nanoTime();
        AVERAGE_TICK_BLOCK_ENTITIES.add(new BigDecimal(diff));
    }


    public static void recordStartTickEntities() {
    }

    public static void recordStopTickEntities() {
    }


    public static void recordStartTickChunkSources() {
    }

    public static void recordStopTickChunkSources() {
    }


    public static void recordStartTickChunks() {
    }

    public static void recordStopTickChunks() {
    }


    public static void recordStartTickBlocks() {
    }

    public static void recordStopTickBlocks() {
    }


    public static void recordStartTickFluids() {
    }

    public static void recordStopTickFluids() {
    }


    public static void recordStartTickVillagerBrains() {
    }

    public static void recordStopTickVillagerBrains() {
    }


    public static void recordStartTickEntityTracking() {
    }

    public static void recordStopTickEntityTracking() {
    }

}
