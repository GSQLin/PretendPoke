package me.figsq.pretendpoke.pretendpoke.api.player;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerController<POKEMON,POKE_ENTITY> {
    public PretendPokePlugin<?,POKEMON,POKE_ENTITY> plugin;
    private final Map<Player, POKEMON> map = new HashMap<>();


    public PlayerController(PretendPokePlugin<?,POKEMON,POKE_ENTITY> plugin) {
        this.plugin = plugin;
    }

    public POKEMON setPretendPoke(Player player, POKEMON pokemon) {
        if (pokemon == null) {
            return this.cancelPretendPoke(player);
        }
        this.hidePlayer(player);
        return this.map.put(player, pokemon);
    }

    public POKEMON getPretendPoke(Player player) {
        return this.map.get(player);
    }

    public POKEMON cancelPretendPoke(Player player) {
        this.showPlayer(player);
        POKEMON remove = this.map.remove(player);
        PokeController<?,POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
        POKE_ENTITY pokeEntity = pokeController.getPokeEntity(remove);
        if (pokeEntity != null) {
            Entity entity = pokeController.asBukkitEntity(pokeEntity);
            entity.remove();
        }
        return remove;
    }

    public Map<Player, POKEMON> getPretendPlayers() {
        return new HashMap<>(this.map);
    }


    /**
     * 向所有在线玩家隐藏指定玩家
     *
     * @param player 被隐藏的玩家
     */
    public void hidePlayer(Player player){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin,onlinePlayer);
        }
    }

    /**
     * 同上向相反
     * @param player 被显示的玩家
     */
    public void showPlayer(Player player){
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin,onlinePlayer);
        }
    }

    public Player getPretendPlayer(POKEMON pokemon){
        for (Map.Entry<Player, POKEMON> entry : this.map.entrySet()) {
            if (entry.getValue().equals(pokemon)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
