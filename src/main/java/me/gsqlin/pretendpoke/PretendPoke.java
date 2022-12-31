package me.gsqlin.pretendpoke;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class PretendPoke extends JavaPlugin {
    static PretendPoke plugin;

    @Override
    public void onEnable() {
        plugin = this;
        reload();
        PluginCommand command = getCommand("pretendpoke");
        Commands cmd = new Commands();
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);
        PixelUtil.registerForgeEvent(plugin);

        getServer().getPluginManager().registerEvents(new GSQListener(),this);
        getLogger().info("§aPlugin loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("§3Plugin disabled");
        GSQUtil.stopAllPretend();
    }

    public void reload(){
        saveDefaultConfig();
        reloadConfig();
    }
}
