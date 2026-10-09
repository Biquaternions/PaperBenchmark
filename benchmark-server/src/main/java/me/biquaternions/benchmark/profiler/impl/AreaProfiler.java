package me.biquaternions.benchmark.profiler.impl;

import me.biquaternions.benchmark.area.AreaType;
import me.biquaternions.benchmark.profiler.AbstractProfiler;
import me.biquaternions.benchmark.util.RollingAverage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class AreaProfiler extends AbstractProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaProfiler.class.getSimpleName());
    private static final int WINDOW_SIZE = 500;

    private final RollingAverage rollingAverage = new RollingAverage(WINDOW_SIZE);
    private final AtomicInteger counter = new AtomicInteger(0);

    private @Nullable ServerPlayer caller = null;

    public AreaProfiler(final AreaType type) {
        super(type);
    }

    @Override
    public void push(final long diff) { // Nanoseconds
        this.rollingAverage.add(diff / 1E-6); // Milliseconds
        if (this.counter.incrementAndGet() % WINDOW_SIZE == 0) {
            final ServerPlayer player = this.caller;
            if (player != null) {
                this.dump(player);
            } else {
                this.dump();
            }
        }
    }

    @Override
    protected void dump(final ServerPlayer player) {
        for (String string : this.generateStrings()) {
            player.sendSystemMessage(Component.literal(string));
        }
    }

    @Override
    protected void dump() {
        LOGGER.error("The profiler is trying to dump data without a player present (this shouldn't happen)");
        for (String string : this.generateStrings()) {
            LOGGER.warn(string);
        }
    }

    private Collection<String> generateStrings() {
        final double average = this.rollingAverage.getAverage();
        final double min = this.rollingAverage.getMin();
        final double max = this.rollingAverage.getMax();
        final double percentile90 = this.rollingAverage.getPercentile(0.90);
        final double percentile95 = this.rollingAverage.getPercentile(0.95);
        final double percentile99 = this.rollingAverage.getPercentile(0.99);
        return List.of(
            String.format("=============[ Sample at: %d ]=============", System.nanoTime() / 1000),
            String.format("[%s] avg: %.8fms   -   min: %.8fms   -   max: %.8fms", this.getTypeName(), average, min, max),
            String.format("[%s] 90%%ile: %.8fms   -   95%%ile: %.8fms   -   99%%ile: %.8fms", this.getTypeName(), percentile90, percentile95, percentile99)
        );
    }

    @Override
    public void setCaller(final ServerPlayer player) {
        this.caller = player;
    }

    @Override
    public void terminate() {
        this.caller = null;
        this.rollingAverage.clear();
    }

}
