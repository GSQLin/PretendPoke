package me.gsqlin.pretendpoke.pretendpoke;

import me.gsqlin.pretendpoke.pretendpoke.api.CommonData;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        CommonData.plugin = this;
        CommonData.versionHelper = new VersionHelper();

        this.reloadConfig();
        PluginCommand command = getCommand("pretendpoke");
        Commands cmd = new Commands();
        command.setExecutor(cmd);
        command.setTabCompleter(cmd);
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new CommonListener(),this);
        manager.registerEvents(new PlayerListener(),this);

        getLogger().info("§aPlugin loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("§3Plugin disabled");
        PretendHelper.stopAllPretend();
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();
    }
}
