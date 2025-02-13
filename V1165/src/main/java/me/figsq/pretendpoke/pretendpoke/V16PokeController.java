package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PixelmonPokemonProxy;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class V16PokeController extends PokeController<Species, Pokemon, PixelmonEntity> {
    public static final V16PokeController INSTANCE = new V16PokeController();

    @Override
    public PixelmonEntity getPokeEntity(Pokemon pokemon) {
        return pokemon.getPixelmonEntity().orElse(null);
    }

    @Override
    public @NonNull PixelmonEntity getOrSpawnPokeEntity(Pokemon pokemon, Entity parent) {
        return pokemon.getOrSpawnPixelmon(minecraftEntity(parent));
    }

    @Override
    public Pokemon getPokemon(PixelmonEntity pixelmonEntity) {
        return pixelmonEntity.getPokemon();
    }

    @Override
    public Entity asBukkitEntity(PixelmonEntity pixelmonEntity) {
        return bukkitEntity(pixelmonEntity);
    }

    @Override
    public PixelmonEntity asPokeEntity(Entity entity) {
        return ((PixelmonEntity) minecraftEntity(entity));
    }

    @Override
    public void hideEntityForPlayer(Entity entity, Player target) {
        SDestroyEntitiesPacket packet = new SDestroyEntitiesPacket(entity.getEntityId());
        ((ServerPlayerEntity) minecraftEntity(target)).field_71135_a.func_147359_a(packet);
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return minecraftEntity(entity) instanceof PixelmonEntity;
    }

    @Override
    public Species getSpecies(String speciesName) {
        return PixelmonSpecies.fromName(speciesName).orElse((Species) null);
    }

    @Override
    public Pokemon createPokemon(Species species) {
        return PokemonFactory.create(species);
    }

    @Override
    public void removeAI(PixelmonEntity pixelmonEntity) {
        ((LivingEntity) bukkitEntity(pixelmonEntity)).setAI(false);
    }

    @Override
    public UUID getUUID(Pokemon pokemon) {
        return pokemon.getUUID();
    }

    @Override
    public List<Species> getAllSpecies() {
        return PixelmonSpecies.getAll();
    }

    @Override
    public String getSpeciesName(Species species) {
        return species.getName();
    }

    @Override
    public OfflinePlayer getOwner(Pokemon pokemon) {
        UUID ownerPlayerUUID = pokemon.getOwnerPlayerUUID();
        if (ownerPlayerUUID == null) {
            return null;
        }
        return Bukkit.getOfflinePlayer(ownerPlayerUUID);
    }

    public static net.minecraft.entity.Entity minecraftEntity(Entity entity){
        if (entity == null) {
            return null;
        }
        return (net.minecraft.entity.Entity) (Object) ((CraftEntity) entity).getHandle();
    }

    public static Entity bukkitEntity(net.minecraft.entity.Entity entity){
        if (entity == null) return null;
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()), (((net.minecraft.server.v1_16_R3.Entity) (Object) entity)));
    }
}
