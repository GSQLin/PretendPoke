package me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class EndPretendPokeEvent extends Event implements Cancellable {
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    boolean cancelled = false;
    private final Player player;
    @Setter
    private Entity pokemon;

    public EndPretendPokeEvent(Player player, Entity pokemon){
        this.player = player;
        this.pokemon = pokemon;
    }

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
}
