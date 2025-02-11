package me.figsq.pretendpoke.pretendpoke.api.pokemon;

import lombok.NonNull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class PokeController<POKEMON, POKE_ENTITY> {
    public abstract POKE_ENTITY getPokeEntity(POKEMON pokemon);
    @NonNull
    public abstract POKE_ENTITY getOrSpawnPokeEntity(POKEMON pokemon, Entity parent);
    public abstract POKEMON getPokemon(POKE_ENTITY pokeEntity);
    public abstract Entity asBukkitEntity(POKE_ENTITY pokeEntity);
    public abstract POKE_ENTITY asPokeEntity(Entity entity);
    public void hideEntityForPlayer(POKE_ENTITY pokeEntity, Player target){
        this.hideEntityForPlayer(this.asBukkitEntity(pokeEntity),target);
    }
    public abstract void hideEntityForPlayer(Entity entity, Player target);
    public abstract boolean isPokeEntity(Entity entity);
}
