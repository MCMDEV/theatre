package de.mcmdev.theatre.paper.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.function.BiConsumer;

public class ScriptCommand extends Command implements PluginIdentifiableCommand, Closeable {

    private final JavaPlugin plugin;
    private final BiConsumer<CommandSender, String[]> consumer;

    public ScriptCommand(JavaPlugin plugin, @NotNull String name, String permission, BiConsumer<CommandSender, String[]> consumer) {
        super(name);
        this.plugin = plugin;
        setPermission(permission);

        this.consumer = consumer;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!testPermission(sender)) return true;
        consumer.accept(sender, args);
        return true;
    }

    @Override
    public void close() {
        unregister(Bukkit.getCommandMap());
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
