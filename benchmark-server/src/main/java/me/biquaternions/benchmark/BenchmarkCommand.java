package me.biquaternions.benchmark;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.PaperCommands;
import me.biquaternions.benchmark.area.AreaType;
import me.biquaternions.benchmark.profiler.impl.AreaProfiler;
import me.biquaternions.benchmark.profiler.impl.NoopProfiler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;
import java.util.Set;

@NullMarked
public class BenchmarkCommand {

    private static final Component PREFIX = MiniMessage.miniMessage().deserialize("<white><gradient:#F56827:#F2F527:#F56827><bold>Benchmark</bold></gradient> <color:#FF4A4A>⮞</color> </white>");
    private static @Nullable Component FEEDBACK_CURRENT_VERSION = null;

    public static void init() {

        LiteralCommandNode<CommandSourceStack> command = Commands.literal("benchmark")
            .requires(s -> s.getSender().hasPermission("bukkit.command.benchmark"))
            .then(Commands.literal("version")
                .executes(ctx -> {
                    if (FEEDBACK_CURRENT_VERSION == null) {
                        FEEDBACK_CURRENT_VERSION = PREFIX.append(Component.text("This server is running " + Bukkit.getName() + " version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")", NamedTextColor.WHITE));
                    }
                    ctx.getSource().getSender().sendMessage(FEEDBACK_CURRENT_VERSION);
                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.literal("start")
                .requires(s -> s.getSender() instanceof CraftPlayer) // Force player to be online to prevent someone from running the benchmark into an empty or sleeping server
                .then(Commands.argument("profiler", StringArgumentType.word())
                    .suggests((_, builder) -> {
                        for (AreaType type : AreaType.values()) {
                            builder.suggest(type.name());
                        }
                        return builder.buildFuture();
                    })
                    .then(Commands.argument("window", IntegerArgumentType.integer(500))
                        .executes(ctx -> {
                            final int window = ctx.getArgument("window", int.class);
                            final String typeString = ctx.getArgument("profiler", String.class);
                            final AreaType areaType;
                            try {
                                areaType = AreaType.valueOf(typeString);
                            } catch (IllegalArgumentException e) {
                                ctx.getSource().getSender().sendMessage("THIS PROFILER TYPE DOES NOT EXIST");
                                return Command.SINGLE_SUCCESS;
                            }

                            for (AreaType type : AreaType.values()) {
                                type.setProfiler(NoopProfiler::new);
                            }
                            areaType.setProfiler(AreaProfiler::new, window);
                            if (ctx.getSource().getSender() instanceof CraftPlayer player) {
                                areaType.getProfiler().setCaller(player.getHandle());
                            }
                            ctx.getSource().getSender().sendMessage("Profiling started");

                            return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
            .then(Commands.literal("stop")
                .requires(s -> s.getSender() instanceof CraftPlayer)
                .executes(ctx -> {
                    for (AreaType type : AreaType.values()) {
                        type.setProfiler(NoopProfiler::new);
                    }
                    ctx.getSource().getSender().sendMessage("Profiling stopped");

                    return Command.SINGLE_SUCCESS;
                })
            )
            .build();

        PaperCommands.INSTANCE.registerWithFlagsInternal(null, "biquaternions", "Benchmark", command, "Benchmark areas of the server", List.of(), Set.of());
    }

}
