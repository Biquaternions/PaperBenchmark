package me.biquaternions.benchmark.area;

import me.biquaternions.benchmark.profiler.AbstractProfiler;
import me.biquaternions.benchmark.profiler.impl.NoopProfiler;
import org.jspecify.annotations.NullMarked;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@NullMarked
public enum AreaType {
    TICK_CHUNK_SOURCE_FULL(NoopProfiler::new), // ServerLevel
    TICK_CHUNKS(NoopProfiler::new), // ServerChunkCache
    TICK_ENTITY_TRACKING(NoopProfiler::new), // ServerChunkCache
    TICK_CHUNK_UNLOADS(NoopProfiler::new), // ServerChunkCache

    TICK_BLOCKS(NoopProfiler::new), // ServerLevel
    TICK_FLUIDS(NoopProfiler::new), // ServerLevel
    TICK_ENTITIES(NoopProfiler::new), // ServerLevel
    TICK_BLOCK_ENTITIES(NoopProfiler::new), // ServerLevel

    TICK_CONNECTION(NoopProfiler::new), // MinecraftServer

    TICK_SERVER(NoopProfiler::new),  // MinecraftServer - measures the entire tick loop

    NONE(NoopProfiler::new);

    private AbstractProfiler profiler;

    AreaType(final Supplier<? extends AbstractProfiler> supplier) {
        this.profiler = supplier.get();
    }

    public void setProfiler(final Supplier<? extends AbstractProfiler> supplier) {
        if (this == NONE) {
            return;
        }
        this.profiler = supplier.get();
    }

    public void setProfiler(final BiFunction<AreaType, Integer, ? extends AbstractProfiler> function, final int window) {
        if (this == NONE) {
            throw new IllegalStateException("Cannot profile NONE area type");
        }
        this.profiler = function.apply(this, window);
    }

    public AbstractProfiler getProfiler() {
        return this.profiler;
    }

}
