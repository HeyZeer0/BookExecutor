package net.heyzeer0.be;

import net.heyzeer0.be.commands.BookExecutorCommand;
import net.heyzeer0.be.listeners.BookEvents;
import net.heyzeer0.be.listeners.InteractEvents;
import net.heyzeer0.be.manager.CoolLogoManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main plugin;

    public void onEnable() {
        plugin = this;

        CoolLogoManager.printLogo();

        registerEvents();

        getCommand("bookexecutor").setExecutor(new BookExecutorCommand());
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BookEvents(), this);
        pm.registerEvents(new InteractEvents(), this);
    }

    public static Main getPlugin() {
        return plugin;
    }
}
