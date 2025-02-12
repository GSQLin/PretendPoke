package me.figsq.pretendpoke.pretendpoke;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class V12PokeController extends PokeController<EnumSpecies, Pokemon, EntityPixelmon> {
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
        return bukkitEntity(entityPixelmon);
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

    @Override
    public EnumSpecies getSpecies(String speciesName) {
        return EnumSpecies.getFromNameAnyCase(speciesName);
    }

    @Override
    public Pokemon createPokemon(EnumSpecies species) {
        return Pixelmon.pokemonFactory.create(species);
    }

    @Override
    public void removeAI(EntityPixelmon entityPixelmon) {
        LivingEntity en = (LivingEntity) asBukkitEntity(entityPixelmon);
        en.setAI(false);
    }

    @Override
    public UUID getUUID(Pokemon pokemon) {
        return pokemon.getUUID();
    }

    @Override
    public List<EnumSpecies> getAllSpecies() {
        return Lists.newArrayList(EnumSpecies.values());
    }

    @Override
    public String getSpeciesName(EnumSpecies species) {
        return species.getPokemonName();
    }

    @Override
    public OfflinePlayer getOwner(Pokemon pokemon) {
        return Bukkit.getOfflinePlayer(pokemon.getOwnerPlayerUUID());
    }

    public static net.minecraft.entity.Entity minecraftEntity(Entity entity) {
        if (entity == null) {
            return null;
        }
        return ((net.minecraft.entity.Entity) (Object) ((CraftEntity) entity).getHandle());
    }

    public static Entity bukkitEntity(net.minecraft.entity.Entity entity) {
        if (entity == null) {
            return null;
        }
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), ((net.minecraft.server.v1_12_R1.Entity) (Object) entity));
    }
}
