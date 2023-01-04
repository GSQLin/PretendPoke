package me.gsqlin.pretendpoke.pretendEvents;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PickOffPretend extends Event {
    private Player player;
    private Player other;
    private Entity pokemon;
    private String[] messages;
    public PickOffPretend(Player player,Player other,Entity pokemon,String[] messages){
        this.player = player;
        this.pokemon = pokemon;
        this.other = other;
        this.messages = messages;
    }

    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public Player getPlayer() {
        return player;
    }
    public Entity getPokemon() {
        return pokemon;
    }

    public Player getOther() {
        return other;
    }

    public String[] getMessages() {
        return messages;
    }
    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}