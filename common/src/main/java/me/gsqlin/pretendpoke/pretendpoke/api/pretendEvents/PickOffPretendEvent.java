package me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PickOffPretendEvent extends Event {
    private final Player player;
    private final Player other;
    private final Entity pokemon;
    @Setter
    private String[] messages;
    public PickOffPretendEvent(Player player, Player other, Entity pokemon, String[] messages){
        this.player = player;
        this.pokemon = pokemon;
        this.other = other;
        this.messages = messages;
    }

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}