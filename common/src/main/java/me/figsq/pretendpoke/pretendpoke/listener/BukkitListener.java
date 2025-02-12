package me.figsq.pretendpoke.pretendpoke.listener;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.api.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class BukkitListener<POKEMON, POKE_ENTITY> implements Listener {
    public PretendPokePlugin<?, POKEMON, POKE_ENTITY> plugin;

    public BukkitListener(PretendPokePlugin<?, POKEMON, POKE_ENTITY> plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        POKEMON poke = this.plugin.getPlayerController().getPretendPoke(player);
        if (poke != null && event.getFrom().getWorld() != event.getTo().getWorld()) {
            PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
            POKE_ENTITY pokeEntity;
            if ((pokeEntity = pokeController.getPokeEntity(poke)) == null) return;
            pokeController.asBukkitEntity(pokeEntity).teleport(player);
            pokeController.hideEntityForPlayer(pokeEntity,player);
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        // TODO: 取消伪装
        this.plugin.getPlayerController().cancelPretendPoke(event.getPlayer(),false);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        for (Player player : plugin.getPlayerController().getPretendPlayers().keySet()) {
            p.hidePlayer(plugin,player);
        }
    }

    @EventHandler
    public void playerFly(PlayerToggleFlightEvent event){
        PlayerController<POKEMON, POKE_ENTITY> playerController = this.plugin.getPlayerController();
        PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
        POKEMON poke = playerController.getPretendPoke(event.getPlayer());
        if (poke != null) {
            POKE_ENTITY pokeEntity = pokeController.getPokeEntity(poke);
            if (pokeEntity != null) {
                Entity entity = pokeController.asBukkitEntity(pokeEntity);
                entity.setGravity(!event.isFlying());
            }
        }
    }
}
