package me.gsqlin.pretendpoke.pretendpoke.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public interface IVersionHelper {
    Object createPokemon(String name);
    Entity getOrSpawnPoke(Object object,Entity parent);
    Entity getBukkitEntity(Object object);
    void hideEntityFromPlayer(Player player, Entity entity);
    List<String> getCanPretendPokeNameList();
}
