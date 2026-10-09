package me.biquaternions.benchmark.profiler.impl;

import me.biquaternions.benchmark.area.AreaType;
import me.biquaternions.benchmark.profiler.AbstractProfiler;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class NoopProfiler extends AbstractProfiler {

    public NoopProfiler() {
        super(AreaType.NONE);
    }

    @Override
    public void push(final long diff) {
    }

    @Override
    public void dump(final ServerPlayer player) {
    }

    @Override
    public void setCaller(final ServerPlayer player) {
    }

    @Override
    public void terminate() {
    }

}
