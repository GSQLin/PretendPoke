package me.gsqlin.pretendpoke;

import me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents.EventM;
import me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents.EventO;
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
        registerListener();

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

    public void registerListener(){
        PixelUtil.registerForgeEvent(plugin);
        getServer().getPluginManager().registerEvents(new GSQListener(),this);
        //特殊事件单独转发
        if (PixelUtil.bukkitVersion.equalsIgnoreCase("1.12.2")){
            getServer().getPluginManager().registerEvents(new EventO(),this);
        }else{
            getServer().getPluginManager().registerEvents(new EventM(),this);
        }
    }
}
