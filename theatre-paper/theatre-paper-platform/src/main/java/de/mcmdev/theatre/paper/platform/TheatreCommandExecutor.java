package de.mcmdev.theatre.paper.platform;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TheatreCommandExecutor implements TabExecutor {

    private static final TextColor PRIMARY_COLOR = TextColor.fromHexString("#e36414");
    private static final Component TITLE = Component.text(
            "Theatre",
            PRIMARY_COLOR,
            TextDecoration.BOLD
    );
    private static final Component PREFIX = Component.text()
            .append(TITLE)
            .append(Component.text(" \u00BB ", NamedTextColor.DARK_GRAY))
            .build();

    private static final Component HELP_PAGE = Component.text()
            .append(PREFIX)
            .append(Component.text("Help Page", PRIMARY_COLOR, TextDecoration.BOLD))
            .append(Component.newline())
            .append(PREFIX)
            .append(Component.text("/theatre reload", PRIMARY_COLOR))
            .append(Component.text(" - ", NamedTextColor.DARK_GRAY))
            .append(Component.text("Reload all scripts", PRIMARY_COLOR))
            .build();

    private static final Component UNKNOWN_SUBCOMMAND = Component.text()
            .append(PREFIX)
            .append(Component.text("Unknown subcommand! Use /theatre to view help", PRIMARY_COLOR))
            .build();

    private static final Component RELOAD_SUCCESS = Component.text()
            .append(PREFIX)
            .append(Component.text("Reload successful!", NamedTextColor.GREEN))
            .build();

    private static final Component RELOAD_ERROR = Component.text()
            .append(PREFIX)
            .append(Component.text("An error occured during the reload. Please check the console for more information.", NamedTextColor.RED))
            .build();

    private final PaperTheatre paperTheatre;

    public TheatreCommandExecutor(PaperTheatre paperTheatre) {
        this.paperTheatre = paperTheatre;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(HELP_PAGE);
            return true;
        }
        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("reload")) {
            try {
                paperTheatre.reload();
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(RELOAD_ERROR);
            }
            sender.sendMessage(RELOAD_SUCCESS);
        } else {
            sender.sendMessage(UNKNOWN_SUBCOMMAND);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
