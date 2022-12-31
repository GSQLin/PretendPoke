package me.gsqlin.pretendpoke.forgeEvents;

import catserver.api.bukkit.ForgeEventV2;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CatEventM implements Listener {
    @EventHandler
    public void onForge(ForgeEventV2 event){
        MyForgeEvent forgeEvent = new MyForgeEvent(event.getForgeEvent());
        if (forgeEvent.getForgeEvent() == null) return;
        try {
            Bukkit.getPluginManager().callEvent(forgeEvent);
        }catch (Exception e){}
    }
}