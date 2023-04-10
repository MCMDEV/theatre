package de.mcmdev.theatre.paper.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PaperContext {

    private final JavaPlugin plugin;
    private final List<Closeable> closeables = new ArrayList<>();

    public PaperContext(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void createCommand(String label, String permission, BiConsumer<CommandSender, String[]> consumer)    {
        ScriptCommand scriptCommand = new ScriptCommand(plugin, label, permission, consumer);
        Bukkit.getCommandMap().register("theatre", scriptCommand);
        ((CraftServer)Bukkit.getServer()).syncCommands();

        this.closeables.add(scriptCommand);
    }

    public void scheduleAsyncTimer(BukkitRunnable bukkitRunnable, int delay, int period) {
        BukkitTask task = bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
        this.closeables.add(task::cancel);
    }

    public void scheduleTimer(BukkitRunnable bukkitRunnable, int delay, int period) {
        BukkitTask task = bukkitRunnable.runTaskTimer(plugin, delay, period);
        this.closeables.add(task::cancel);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        this.closeables.add(() -> HandlerList.unregisterAll(listener));
    }

    public void disable() {
        this.closeables.forEach(closeable -> {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.closeables.clear();
    }
}
