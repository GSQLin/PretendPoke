package me.figsq.pretendpoke.pretendpoke.api;

import lombok.NonNull;
import me.figsq.pretendpoke.pretendpoke.PretendPokePlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class PokeController<SPECIES,POKEMON, POKE_ENTITY> {
    public abstract POKE_ENTITY getPokeEntity(POKEMON pokemon);
    @NonNull
    public abstract POKE_ENTITY getOrSpawnPokeEntity(POKEMON pokemon, Entity parent);
    public abstract POKEMON getPokemon(POKE_ENTITY pokeEntity);
    public abstract Entity asBukkitEntity(POKE_ENTITY pokeEntity);
    public abstract POKE_ENTITY asPokeEntity(Entity entity);
    public void hideEntityForPlayer(POKE_ENTITY pokeEntity, Player target){
        this.hideEntityForPlayer(this.asBukkitEntity(pokeEntity),target);
    }
    public void pretendEntityInit(POKE_ENTITY pokeEntity, Player owner){
        removeAI(pokeEntity);
        Entity entity = asBukkitEntity(pokeEntity);
        entity.setInvulnerable(true);
        entity.setGravity(!owner.isFlying());
    }

    public abstract void hideEntityForPlayer(Entity entity, Player target);
    public abstract boolean isPokeEntity(Entity entity);
    public abstract SPECIES getSpecies(String speciesName);
    public abstract POKEMON createPokemon(SPECIES species);
    public abstract void removeAI(POKE_ENTITY pokeEntity);
    public abstract UUID getUUID(POKEMON pokemon);
    public abstract List<SPECIES> getAllSpecies();
    public abstract String getSpeciesName(SPECIES species);
    public abstract OfflinePlayer getOwner(POKEMON pokemon);

    public Player getPretendPlayer(POKEMON pokemon){
        PlayerController<POKEMON, ?> playerController = (PlayerController<POKEMON, ?>) PretendPokePlugin.getInstance().getPlayerController();
        return playerController.getPretendPlayer(pokemon);
    }

    public boolean isPretendPoke(POKEMON pokemon){
        return getPretendPlayer(pokemon) != null;
    }
}
