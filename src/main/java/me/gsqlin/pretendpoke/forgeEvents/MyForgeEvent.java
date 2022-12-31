package me.gsqlin.pretendpoke.forgeEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MyForgeEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private final Object event;

    public MyForgeEvent(Object event) {
        this.event = event;
    }

    public Object getForgeEvent() {
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