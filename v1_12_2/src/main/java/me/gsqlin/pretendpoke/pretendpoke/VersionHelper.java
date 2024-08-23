package me.gsqlin.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.gsqlin.pretendpoke.pretendpoke.api.IVersionHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class VersionHelper implements IVersionHelper {
    @Override
    public Object createPokemon(String name) {
        return Pixelmon.pokemonFactory.create(EnumSpecies.getFromNameAnyCase(name));
    }

    @Override
    public Entity getOrSpawnPoke(Object object, Entity parent) {
        if (!(object instanceof Pokemon)) {
            throw new IllegalArgumentException("object is not a pokemon");
        }
        return getBukkitEntity(((Pokemon) object).getOrSpawnPixelmon((
                        ((net.minecraft.entity.Entity) (Object)
                                ((CraftEntity) parent).getHandle()))));
    }

    @Override
    public Entity getBukkitEntity(Object object) {
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), (net.minecraft.server.v1_12_R1.Entity) object);
    }

    @Override
    public void hideEntityFromPlayer(Player player, Entity entity) {
        SPacketDestroyEntities packet = new SPacketDestroyEntities(entity.getEntityId());
        ((EntityPlayerMP) ((Object) ((CraftPlayer) player).getHandle()))
                .field_71135_a.func_147359_a(packet);
    }

    @Override
    public List<String> getCanPretendPokeNameList() {
        return EnumSpecies.getNameList();
    }
}
