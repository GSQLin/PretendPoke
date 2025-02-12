package me.figsq.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.pokemon.species.Species;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class V20PokeController extends PokeController<Species, Pokemon, PixelmonEntity> {
    public static final V20PokeController INSTANCE = new V20PokeController();

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
        return (PixelmonEntity) minecraftEntity(entity);
    }

    @Override
    public void hideEntityForPlayer(Entity entity, Player target) {
        target.hideEntity(PretendPokePlugin.getInstance(), entity);
    }

    @Override
    public boolean isPokeEntity(Entity entity) {
        return minecraftEntity(entity) instanceof PixelmonEntity;
    }

    @Override
    public Species getSpecies(String speciesName) {
        return PixelmonSpecies.fromName(speciesName).orElse(null);
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
        return Bukkit.getOfflinePlayer(pokemon.getOwnerPlayerUUID());
    }

    public static Entity bukkitEntity(net.minecraft.world.entity.Entity entity){
        return CraftEntity.getEntity(((CraftServer) Bukkit.getServer()),entity);
    }

    public static net.minecraft.world.entity.Entity minecraftEntity(Entity entity){
        return ((CraftEntity)entity).getHandle();
    }
}
