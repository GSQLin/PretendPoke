package me.figsq.pretendpoke.pretendpoke.listener;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.api.player.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public abstract class ForgeListener<
        POKEMON,POKE_ENTITY,
        POKEBALL_IMPACT_EVENT
        > implements Listener {
    public PretendPokePlugin<?,POKEMON,POKE_ENTITY> plugin;

    public ForgeListener(PretendPokePlugin<?,POKEMON,POKE_ENTITY> plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void on(ForgeEvent event){
        if (this.getPokeballImpactEventClass().isInstance(event.getForgeEvent())) {
            POKEBALL_IMPACT_EVENT e = (POKEBALL_IMPACT_EVENT) event.getForgeEvent();
            Entity entity = getBallHitEntity(e);
            //TODO: 丢球撞到了伪装玩家的伪装实体宝可梦 进行取消伪装
            if (entity != null) {
                PokeController<?,POKEMON, POKE_ENTITY> pokeController = plugin.getPokeController();
                if (pokeController.isPokeEntity(entity)) {
                    PlayerController<POKEMON,POKE_ENTITY> playerController = plugin.getPlayerController();
                    POKEMON pokemon = pokeController.getPokemon(pokeController.asPokeEntity(entity));
                    Player player = playerController.getPretendPlayer(pokemon);
                    if (player != null) {
                        playerController.cancelPretendPoke(player);
                    }
                }
            }
        }
    }

    public abstract Class<POKEBALL_IMPACT_EVENT> getPokeballImpactEventClass();
    public abstract Entity getBallHitEntity(POKEBALL_IMPACT_EVENT event);
}
