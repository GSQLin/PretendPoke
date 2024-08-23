package me.gsqlin.pretendpoke.pretendpoke;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import me.gsqlin.pretendpoke.pretendpoke.api.IVersionHelper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class VersionHelper implements IVersionHelper {
    @Override
    public Object createPokemon(String name) {
        Optional<Species> species = PixelmonSpecies.fromNameOrDex(name);
        if (!species.isPresent()) {
            throw new IllegalArgumentException("Invalid pokemon name or dex number!");
        }
        return PokemonFactory.create(species.get());
    }

    @Override
    public Entity getOrSpawnPoke(Object object, Entity parent) {
        if (!(object instanceof Pokemon)) {
            throw new IllegalArgumentException("object is not a pokemon!");
        }
        return getBukkitEntity(((Pokemon) object).getOrSpawnPixelmon((((net.minecraft.entity.Entity)
                (Object) ((CraftEntity) parent).getHandle()))));
    }

    @Override
    public Entity getBukkitEntity(Object object) {
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), (net.minecraft.server.v1_16_R3.Entity) object);
    }

    @Override
    public void hideEntityFromPlayer(Player player, Entity entity) {
        SDestroyEntitiesPacket packet = new SDestroyEntitiesPacket(entity.getEntityId());
        ((ServerPlayerEntity) ((Object) ((CraftPlayer) player).getHandle()))
                .field_71135_a.func_147359_a(packet);
    }

    @Override
    public List<String> getCanPretendPokeNameList() {
        return Lists.newArrayList(PixelmonSpecies.getFormattedEnglishNameSet());
    }
}
