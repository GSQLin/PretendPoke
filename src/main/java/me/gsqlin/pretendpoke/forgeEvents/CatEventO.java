package me.gsqlin.pretendpoke.forgeEvents;

import catserver.api.bukkit.event.ForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CatEventO implements Listener {
    @EventHandler
    public void onListener(ForgeEvent event) {
        MyForgeEvent forgeEvent = new MyForgeEvent(event.getForgeEvent());
        if (forgeEvent.getForgeEvent() == null) return;
        try {
            Bukkit.getPluginManager().callEvent(forgeEvent);
        }catch (Exception e){}
    }
}
