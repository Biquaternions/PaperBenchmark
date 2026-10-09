package me.biquaternions.benchmark.profiler.impl;

import me.biquaternions.benchmark.area.AreaType;
import me.biquaternions.benchmark.profiler.AbstractProfiler;
import me.biquaternions.benchmark.util.RollingAverage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class AreaProfiler extends AbstractProfiler {

    private static final int WINDOW_SIZE = 500;

    private final RollingAverage rollingAverage = new RollingAverage(WINDOW_SIZE);
    private final AtomicInteger counter = new AtomicInteger(0);

    private @Nullable ServerPlayer caller = null;

    public AreaProfiler(final AreaType type) {
        super(type);
    }

    @Override
    public void push(final long diff) {
        this.rollingAverage.add(diff);
        if (this.counter.incrementAndGet() % WINDOW_SIZE == 0) {
            final ServerPlayer player = this.caller;
            if (player != null) {
                this.dump(player);
            }
        }
    }

    @Override
    public void dump(final ServerPlayer player) {
        final double average = this.rollingAverage.getAverage();
        final double min = this.rollingAverage.getMin();
        final double max = this.rollingAverage.getMax();
        final double percentile90 = this.rollingAverage.getPercentile(0.90);
        final double percentile95 = this.rollingAverage.getPercentile(0.95);
        final double percentile99 = this.rollingAverage.getPercentile(0.99);
        player.sendSystemMessage(Component.literal(""));
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
