package me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class StartPretendPokeEvent extends Event implements Cancellable {
    private final Player player;
    @Setter
    private Object pokemon;
    public StartPretendPokeEvent(Player player, Object pokemon){
        this.player = player;
        this.pokemon = pokemon;
    }

    @Getter
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
}
