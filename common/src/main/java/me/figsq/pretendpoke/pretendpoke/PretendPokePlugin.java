package me.figsq.pretendpoke.pretendpoke;

import lombok.Getter;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.BukkitListener;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class PretendPokePlugin<SPECIES, POKEMON, POKE_ENTITY> extends JavaPlugin {
    public static <T extends PretendPokePlugin<?, ?, ?>> T getInstance() {
        return (T) JavaPlugin.getPlugin(PretendPokePlugin.class);
    }

    private BukkitListener<POKEMON, POKE_ENTITY> bukkitListener;

    private PlayerController<POKEMON, POKE_ENTITY> playerController;

    private CommandBase<SPECIES, POKEMON> commandBase;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        this.bukkitListener = new BukkitListener<>(this);
        this.playerController = new PlayerController<>(this);
        this.commandBase = new CommandBase<>();

        this.reloadConfig();

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(bukkitListener, this);
        ForgeListener<POKEMON, POKE_ENTITY, ?, ?, ?> forgeListener = this.getForgeListener();
        if (forgeListener != null) {
            pluginManager.registerEvents(forgeListener, this);
            this.getLogger().info("§aForgeListener registered!");
        } else {
            this.getLogger().warning("ForgeListener is null");
        }

        PluginCommand command = this.getCommand("pretendpoke");
        command.setExecutor(commandBase);
        command.setTabCompleter(commandBase);
    }

    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();
    }

    @NonNull
    public abstract PokeController<SPECIES, POKEMON, POKE_ENTITY> getPokeController();

    public abstract ForgeListener<POKEMON, POKE_ENTITY, ?, ?, ?> getForgeListener();

    @Override
    public void onDisable() {
        for (Player player : this.getPlayerController().getPretendPlayers().keySet()) {
            this.getPlayerController().cancelPretendPoke(player,true);
        }
    }
}
