package me.gsqlin.pretendpoke.pretendpoke;

import me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents.PickOffPretendEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

import static me.gsqlin.pretendpoke.pretendpoke.api.CommonData.plugin;
import static me.gsqlin.pretendpoke.pretendpoke.PretendHelper.*;

public class CommonListener implements Listener {
    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (runnableMap.containsKey(player.getName())) {
                p.hidePlayer(plugin, player);
                continue;
            }
            p.showPlayer(plugin, player);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if (!plugin.getConfig().getBoolean("扒拉伪装")) return;
        Player p = event.getPlayer();
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
            if (entry.getValue().equals(event.getRightClicked())) {
                event.setCancelled(true);
                Player player = Bukkit.getPlayer(entry.getKey());
                PickOffPretendEvent pickEvent;
                String[] messages = new String[2];
                if (p.equals(player)) {
                    messages[0] = "§7不要对自己的伪装皮套做奇奇怪怪的事情";
                    pickEvent = new PickOffPretendEvent(p, null, entry.getValue(), messages);
                    Bukkit.getPluginManager().callEvent(pickEvent);
                    p.sendMessage(pickEvent.getMessages()[0]);
                } else {
                    messages[0] = "§7你揭穿了玩家:§3" + player.getName() + "§7的伪装";
                    messages[1] = "§6你被掀了皮套————";
                    pickEvent = new PickOffPretendEvent(p, player, entry.getValue(), messages);
                    Bukkit.getPluginManager().callEvent(pickEvent);
                    p.sendMessage(pickEvent.getMessages()[0]);
                    stopPretend(player);
                    player.sendMessage(pickEvent.getMessages()[1]);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        hide(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        hide(event.getPlayer());
    }

    public void hide(Player player) {
        if (entityMap.containsKey(player.getName())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                Entity entity = entityMap.get(player.getName());
                try {
                    hideEntity(player, entity);
                } catch (Exception e) {
                    plugin.getLogger().info("§c错误!编号:006");
                    throw new RuntimeException(e);
                }
            }, 20L);
        }
    }
}
