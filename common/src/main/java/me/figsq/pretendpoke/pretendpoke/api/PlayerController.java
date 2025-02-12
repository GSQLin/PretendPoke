package me.figsq.pretendpoke.pretendpoke.api;

import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import me.figsq.pretendpoke.pretendpoke.event.PlayerCancelPretendEvent;
import me.figsq.pretendpoke.pretendpoke.event.PlayerPretendEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerController<POKEMON, POKE_ENTITY> {
    public PretendPokePlugin<?, POKEMON, POKE_ENTITY> plugin;
    private final Map<Player, POKEMON> map = new HashMap<>();
    public final BukkitTask bukkitTask;
    public final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();


    public PlayerController(PretendPokePlugin<?, POKEMON, POKE_ENTITY> plugin) {
        this.plugin = plugin;
        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Player player;
            POKEMON pokemon;
            PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
            POKE_ENTITY pokeEntity;
            for (Map.Entry<Player, POKEMON> entry : map.entrySet()) {
                player = entry.getKey();
                if ((pokeEntity = pokeController.getPokeEntity(pokemon = entry.getValue())) == null) {
                    pokeEntity = pokeController.getOrSpawnPokeEntity(pokemon, player);
                    pokeController.pretendEntityInit(pokeEntity,player);
                    pokeController.hideEntityForPlayer(pokeEntity, player);
                }

                pokeController.asBukkitEntity(pokeEntity).teleport(player);
            }
        }, 1, 1);
    }

    public POKEMON setPretendPoke(Player player, POKEMON pokemon, boolean callEvent) {
        if (callEvent) {
            PlayerPretendEvent<POKEMON> event = new PlayerPretendEvent<>(player, pokemon);
            Bukkit.getPluginManager().callEvent(event);
            pokemon = event.getPokemon();
            if (event.isCancelled()) {
                return null;
            }
        }

        POKEMON old = this.cancelPretendPoke(player,false);

        //额外执行
        PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
        registerTeam(player, pokeController.getUUID(pokemon));
        //=

        if (pokemon != null) {
            this.hidePlayer(player);
            this.map.put(player, pokemon);
        }

        return old;
    }

    public Team registerTeam(Player player, UUID pokeUUID) {
        String name = player.getName();
        String teamName = name.substring(0, Math.min(name.length(), 10)) + "|;pp;";
        Team team = scoreboard.registerNewTeam(teamName);
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.addEntry(player.getUniqueId().toString());
        team.addEntry(pokeUUID.toString());
        return team;
    }

    public void unregisterTeam(Player player) {
        String uidStr = player.getUniqueId().toString();
        for (Team team : this.scoreboard.getTeams()) {
            if (team.hasEntry(uidStr)) {
                team.unregister();
            }
        }
    }

    public POKEMON getPretendPoke(Player player) {
        return this.map.get(player);
    }

    public POKEMON cancelPretendPoke(Player player, boolean callEvent) {
        if (callEvent) {
            PlayerCancelPretendEvent event = new PlayerCancelPretendEvent(player);
            event.call();
            if (event.isCancelled()) {
                return null;
            }
        }

        POKEMON remove = this.map.remove(player);
        if (remove == null) return null;
        this.showPlayer(player);

        //额外执行
        this.unregisterTeam(player);
        //=


        PokeController<?, POKEMON, POKE_ENTITY> pokeController = this.plugin.getPokeController();
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
    public void hidePlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(plugin, player);
        }
    }

    /**
     * 同上向相反
     *
     * @param player 被显示的玩家
     */
    public void showPlayer(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(plugin, player);
        }
    }

    public Player getPretendPlayer(POKEMON pokemon) {
        for (Map.Entry<Player, POKEMON> entry : this.map.entrySet()) {
            if (entry.getValue().equals(pokemon)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
