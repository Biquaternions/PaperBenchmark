package me.biquaternions.benchmark;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.PaperCommands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import java.util.List;
import java.util.Set;

public class BenchmarkCommand {

    private static final Component PREFIX = MiniMessage.miniMessage().deserialize("<white><gradient:#F56827:#F2F527:#F56827><bold>Benchmark</bold></gradient> <color:#FF4A4A>⮞</color> </white>");
    private static Component FEEDBACK_CURRENT_VERSION = null;

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
            .build();

        PaperCommands.INSTANCE.registerWithFlagsInternal(null, "biquaternions", "Benchmark", command, "Benchmark areas of the server", List.of(), Set.of());
    }

}
