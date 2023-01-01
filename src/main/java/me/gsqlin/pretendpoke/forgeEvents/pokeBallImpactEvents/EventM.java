package me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents;

import me.gsqlin.pretendpoke.forgeEvents.MyForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventM implements Listener {
    @EventHandler
    public void eventO(MyForgeEvent event){
        if (event.getForgeEvent() instanceof com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent){
            PokeBallIEvent pokeBallIEvent = new PokeBallIEvent(event.getForgeEvent());
            Bukkit.getPluginManager().callEvent(pokeBallIEvent);
        }
    }
}
