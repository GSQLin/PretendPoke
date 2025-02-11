package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class V12PokeController extends PokeController<Pokemon, EntityPixelmon> {
    public static final V12PokeController INSTANCE = new V12PokeController();

    @Override
    public EntityPixelmon getPokeEntity(Pokemon pokemon) {
        return pokemon.getPixelmonIfExists();
    }

    @Override
    public @NonNull EntityPixelmon getOrSpawnPokeEntity(Pokemon pokemon, Entity parent) {
        return pokemon.getOrSpawnPixelmon(minecraftEntity(parent));
    }

    @Override
    public Pokemon getPokemon(EntityPixelmon entityPixelmon) {
        return entityPixelmon.getPokemonData();
    }

    @Override
    public Entity asBukkitEntity(EntityPixelmon entityPixelmon) {
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), (((net.minecraft.server.v1_12_R1.Entity) (Object) entityPixelmon)));
    }

    @Override
    public EntityPixelmon asPokeEntity(Entity entity) {
        return (EntityPixelmon) minecraftEntity(entity);
    }

    @Override
    public void hideEntityForPlayer(Entity entity, Player target) {
        SPacketDestroyEntities packet = new SPacketDestroyEntities(entity.getEntityId());
        EntityPlayerMP handle = (EntityPlayerMP) (Object) ((CraftPlayer) target).getHandle();
        assert handle != null;
        handle.field_71135_a.func_147359_a(packet);
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return minecraftEntity(entity) instanceof EntityPixelmon;
    }

    public net.minecraft.entity.Entity minecraftEntity(Entity entity) {
        return ((net.minecraft.entity.Entity) (Object) ((CraftEntity) entity).getHandle());
    }
}
