package me.biquaternions.benchmark.area;

import me.biquaternions.benchmark.profiler.AbstractProfiler;
import me.biquaternions.benchmark.profiler.impl.AreaProfiler;
import me.biquaternions.benchmark.profiler.impl.NoopProfiler;
import org.jspecify.annotations.NullMarked;
import java.util.function.Function;
import java.util.function.Supplier;

@NullMarked
public enum AreaType {
    TICK_CHUNK_SOURCE_FULL(AreaProfiler::new), // ServerLevel
    TICK_CHUNK_LOADS(AreaProfiler::new), // ServerChunkCache
    TICK_ENTITY_TRACKING(AreaProfiler::new), // ServerChunkCache
    TICK_CHUNK_UNLOADS(AreaProfiler::new), // ServerChunkCache

    TICK_BLOCKS(AreaProfiler::new), // ServerLevel
    TICK_FLUIDS(AreaProfiler::new), // ServerLevel
    TICK_ENTITIES(AreaProfiler::new), // ServerLevel
    TICK_BLOCK_ENTITIES(AreaProfiler::new), // ServerLevel

    TICK_CONNECTION(AreaProfiler::new), // MinecraftServer

    TICK_SERVER(AreaProfiler::new),  // MinecraftServer - measures the entire tick loop

    NONE(NoopProfiler::new);

    private final AbstractProfiler profiler;

    AreaType(final Function<AreaType, ? extends AbstractProfiler> function) {
        this.profiler = function.apply(this);
    }

    AreaType(final Supplier<? extends AbstractProfiler> supplier) {
        this.profiler = supplier.get();
    }

    public AbstractProfiler getProfiler() {
        return this.profiler;
    }

}
