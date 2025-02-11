package me.figsq.pretendpoke.pretendpoke.listener;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener<POKEMON, POKE_ENTITY> implements Listener {
    public PretendPokePlugin<?,POKEMON, POKE_ENTITY> plugin;

    public BukkitListener(PretendPokePlugin<?,POKEMON, POKE_ENTITY> plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        POKEMON poke = this.plugin.getPlayerController().getPretendPoke(event.getPlayer());
        if (poke != null) {
            PokeController<?,POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
            // TODO: 是伪装玩家
            Player player = event.getPlayer();
            POKE_ENTITY pokeEntity = pokeController.getOrSpawnPokeEntity(poke, player);
            pokeController.asBukkitEntity(pokeEntity).teleport(player);

            if (event.getFrom().getWorld() != event.getTo().getWorld()) {
                //TODO: 跨世界传送再一次隐藏宝可梦实体
                pokeController.hideEntityForPlayer(pokeEntity, player);
            }
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        // TODO: 取消伪装
        this.plugin.getPlayerController().cancelPretendPoke(event.getPlayer());
    }
}
