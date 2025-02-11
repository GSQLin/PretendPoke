package me.figsq.pretendpoke.pretendpoke;

import lombok.Getter;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.player.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import me.figsq.pretendpoke.pretendpoke.listener.BukkitListener;
import me.figsq.pretendpoke.pretendpoke.listener.ForgeListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class PretendPokePlugin<SPECIES, POKEMON, POKE_ENTITY> extends JavaPlugin {
    public static <T extends PretendPokePlugin<?, ?, ?>> T getInstance() {
        return (T) JavaPlugin.getPlugin(PretendPokePlugin.class);
    }

    private final BukkitListener<POKEMON, POKE_ENTITY> bukkitListener = new BukkitListener<>(this);

    private final PlayerController<POKEMON, POKE_ENTITY> playerController = new PlayerController<>(this);

    private final CommandBase<SPECIES, POKEMON> commandBase = new CommandBase<>();

    @Override

    public void onLoad() {
    }

    @Override
    public void onEnable() {
        this.reloadConfig();

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(bukkitListener, this);
        pluginManager.registerEvents(this.getForgeListener(), this);

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

    @NonNull
    public abstract ForgeListener<POKEMON, POKE_ENTITY, ?> getForgeListener();
}
