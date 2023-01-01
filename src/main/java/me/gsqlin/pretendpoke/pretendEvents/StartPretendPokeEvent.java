package me.gsqlin.pretendpoke.pretendEvents;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartPretendPokeEvent extends Event implements Cancellable {
    private Player player;
    private Pokemon pokemon;
    public StartPretendPokeEvent(Player player, Pokemon pokemon){
        this.player = player;
        this.pokemon = pokemon;
    }

    private static final HandlerList handlerList = new HandlerList();
    boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public Player getPlayer() {
        return player;
    }
    public Pokemon getPokemon() {
        return pokemon;
    }
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
