package me.gsqlin.pretendpoke.pretendpoke;

import me.gsqlin.pretendpoke.pretendpoke.api.IVersionHelper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

//用来占用得(子项目覆盖)
public class VersionHelper implements IVersionHelper {
    @Override
    public Object createPokemon(String name) {
        return null;
    }

    @Override
    public Entity getOrSpawnPoke(Object object, Entity parent) {
        return null;
    }

    @Override
    public Entity getBukkitEntity(Object object) {
        return null;
    }

    @Override
    public void hideEntityFromPlayer(Player player, Entity entity) {
    }

    @Override
    public List<String> getCanPretendPokeNameList() {
        return Collections.emptyList();
    }
}
