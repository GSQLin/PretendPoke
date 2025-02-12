package me.figsq.pretendpoke.pretendpoke.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class PlayerPretendEvent<POKEMON> extends Event implements Cancellable {
    private boolean cancelled;
    private final Player player;
    private POKEMON pokemon;

    @Getter
    public static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public PlayerPretendEvent(Player player,POKEMON pokemon) {
        this.player = player;
        this.pokemon = pokemon;
    }

    public void call(){
        Bukkit.getServer().getPluginManager().callEvent(this);
    }
}
