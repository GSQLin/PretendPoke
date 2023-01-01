package me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PokeBallIEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private final Object event;

    public PokeBallIEvent(Object event) {
        this.event = event;
    }

    public Object getEvent() {
        return this.event;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
