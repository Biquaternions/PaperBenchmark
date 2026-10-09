package me.biquaternions.benchmark.profiler;

import me.biquaternions.benchmark.area.AreaType;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractProfiler {

    private final AreaType type;

    protected AbstractProfiler(final AreaType type) {
        this.type = type;
    }

    public abstract void push(final long diff);

    public abstract void dump(final ServerPlayer player);

    public abstract void setCaller(final ServerPlayer player);

    public abstract void terminate();

    protected final String getTypeName() {
        return this.type.name();
    }

}
