package me.figsq.pretendpoke.pretendpoke.listener;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.api.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;

public abstract class ForgeListener<
        POKEMON,POKE_ENTITY, //data
        POKEBALL_IMPACT_EVENT, BATTLE_START_EVENT, //pokemonEntity
        FORGE_EVENT //ForgeEvent(Forge Not FICoreForgeEvent)Class
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
            Entity thrower = getThrower(e);
            //TODO: 丢球撞到了伪装玩家的伪装实体宝可梦 进行取消伪装
            if (entity != null && !entity.equals(thrower)) {
                PokeController<?,POKEMON, POKE_ENTITY> pokeController = plugin.getPokeController();
                PlayerController<POKEMON,POKE_ENTITY> playerController = plugin.getPlayerController();
                FORGE_EVENT EE = (FORGE_EVENT) event.getForgeEvent();
                Map<Player, POKEMON> pretendPlayers = playerController.getPretendPlayers();
                if (thrower instanceof Player) {
                    if (pretendPlayers.containsKey(thrower)) {
                        cancelForgeEvent(EE);
                        return;
                    }
                }

                if (pokeController.isPokeEntity(entity)) {
                    POKEMON pokemon = pokeController.getPokemon(pokeController.asPokeEntity(entity));
                    Player player = playerController.getPretendPlayer(pokemon);
                    if (player != null) {
                        cancelForgeEvent(EE);
                        return;
                    }
                    OfflinePlayer owner = pokeController.getOwner(pokemon);
                    if (owner == null || !owner.isOnline()) {
                        return;
                    }
                    entity = owner.getPlayer();
                }
                if (entity instanceof Player) {
                    if (playerController.getPretendPoke(((Player) entity)) != null) {
                        cancelForgeEvent(EE);
                    }
                }
            }
        }

        if (this.getBattleStartEventClass().isInstance(event.getForgeEvent())) {
            BATTLE_START_EVENT e = (BATTLE_START_EVENT) event.getForgeEvent();
            List<Entity> entities = getParticipants(e);
            PlayerController<POKEMON, POKE_ENTITY> playerController = this.plugin.getPlayerController();
            PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
            FORGE_EVENT EE = (FORGE_EVENT) event.getForgeEvent();
            Map<Player, POKEMON> pretendPlayers = playerController.getPretendPlayers();
            for (Entity entity : entities) {
                if (pokeController.isPokeEntity(entity)) {
                    POKEMON pokemon = pokeController.getPokemon(pokeController.asPokeEntity(entity));
                    if (pretendPlayers.containsValue(pokemon)) {
                        cancelForgeEvent(EE);
                    }
                    continue;
                }
                if (entity instanceof Player){
                    if (pretendPlayers.containsKey(((Player) entity))) {
                        cancelForgeEvent(EE);
                    }
                }
            }
        }
    }

    public abstract Class<POKEBALL_IMPACT_EVENT> getPokeballImpactEventClass();
    public abstract Class<BATTLE_START_EVENT> getBattleStartEventClass();
    public abstract Entity getBallHitEntity(POKEBALL_IMPACT_EVENT event);
    public abstract void cancelForgeEvent(FORGE_EVENT event);
    public abstract List<Entity> getParticipants(BATTLE_START_EVENT event);
    public abstract Entity getThrower(POKEBALL_IMPACT_EVENT event);
}
