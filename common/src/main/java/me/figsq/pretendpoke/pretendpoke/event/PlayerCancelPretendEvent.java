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
public class PlayerCancelPretendEvent extends Event implements Cancellable {
    private boolean cancelled;
    public final Player player;

    @Getter
    public static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public PlayerCancelPretendEvent(Player player) {
        this.player = player;
    }

    public void call(){
        Bukkit.getServer().getPluginManager().callEvent(this);
    }
}
