package me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents;

import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import me.gsqlin.pretendpoke.forgeEvents.MyForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventO implements Listener {
    @EventHandler
    public void eventO(MyForgeEvent event){
        if (event.getForgeEvent() instanceof PokeballImpactEvent){
            PokeBallIEvent pokeBallIEvent = new PokeBallIEvent(event.getForgeEvent());
            Bukkit.getPluginManager().callEvent(pokeBallIEvent);
        }
    }
}
