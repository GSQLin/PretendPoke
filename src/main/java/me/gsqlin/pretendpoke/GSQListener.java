package me.gsqlin.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.ThrowPokeballEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import me.gsqlin.pretendpoke.forgeEvents.MyForgeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static me.gsqlin.pretendpoke.GSQUtil.*;
import static me.gsqlin.pretendpoke.PixelUtil.*;
import static me.gsqlin.pretendpoke.PretendPoke.plugin;

public class GSQListener implements Listener {
    @EventHandler
    public void play(MyForgeEvent event) throws Exception {
        if (event.getForgeEvent() instanceof BattleStartedEvent){
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            Object bc = getField(e.getClass(),e,"bc");
            List<PlayerParticipant> playerParticipants = (List<PlayerParticipant>) getMethod(bc.getClass(),"getPlayers").invoke(bc);
            if (bc == null) return;
            List<Entity> entities = new ArrayList<>();
            for (BattleParticipant participant : (List<BattleParticipant>) getField(bc.getClass(),bc,"participants")) {
                PixelmonWrapper[] wrappers = participant.allPokemon;
                for (PixelmonWrapper wrapper : wrappers) {
                    if (wrapper != null){
                        Object pEntity = getField(wrapper.getClass(),wrapper,"entity");
                        if (pEntity != null){
                            entities.add(getBukkitEntity(pEntity));
                        }
                    }
                }
            }
            for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
                if (entities.contains(entry.getValue())){
                    e.setCanceled(true);
                    if (!playerParticipants.isEmpty()){
                        List<Player> players = new ArrayList<>();
                        for (PlayerParticipant participant : playerParticipants) {
                            players.add((Player) getBukkitEntity(getField(participant.getClass(),participant,"player")));
                        }
                        Player p = Bukkit.getPlayer(entry.getKey());
                        if (players.contains(p)){
                            p.sendMessage("§7宝可梦不能丢球进行对战的哦");
                        }else{
                            players.remove(p);
                            p.sendMessage("§7你的伪装被人识破");
                            players.get(0).sendMessage("§7你成功识破玩家:§a"+p.getName()+"§7的伪装");
                            stopPretend(p);
                        }
                    }
                    break;
                }
            }
        }
        if (event.getForgeEvent() instanceof BeatWildPixelmonEvent){
            BeatWildPixelmonEvent e = (BeatWildPixelmonEvent) event.getForgeEvent();
            Entity entity = getBukkitEntity(getMethod(e.wpp.getClass(),"getEntity").invoke(e.wpp));
            for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
                if (entry.getValue().equals(entity)){
                    Player player = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
                    Player p = Bukkit.getPlayer(entry.getKey());
                    stopPretend(p);
                    if (player == p){
                        player.sendMessage("§7你把自己的伪装皮套破坏了");
                    }else{
                        player.sendMessage("§7你的坏了某人的伪装皮套");
                        p.sendMessage("§7你的伪装皮套被弄坏了");
                    }
                    break;
                }
            }
        }
        if (event.getForgeEvent() instanceof ThrowPokeballEvent){
            ThrowPokeballEvent e = (ThrowPokeballEvent) event.getForgeEvent();
            Player p = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
            if (entityMap.containsKey(p.getName())) {
                e.setCanceled(true);
                p.sendMessage("§7催眠: 你是一个宝可梦,所以你不能丢球");
            }
        }
        if (event.getForgeEvent() instanceof CaptureEvent.StartCapture){
            CaptureEvent.StartCapture e = (CaptureEvent.StartCapture) event.getForgeEvent();
            Player p = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
            Object oPoke = getField(e.getClass(),e,"pokemon");
            for (Map.Entry<String, Entity> map:entityMap.entrySet()){
                if (map.getValue().equals(getBukkitEntity(oPoke))){
                    e.setCanceled(true);
                    Player player = Bukkit.getPlayer(map.getKey());
                    if (player.equals(p)){
                        p.sendMessage("§7这只是一个伪装皮套");
                    }else{
                        p.sendMessage("§7这个宝可梦是玩家伪装的揭穿他!");
                        player.sendMessage("§7你被精灵球砸了一下,可能已被人发现是伪装的了");
                    }
                    break;
                }
            }
        }
        if (event.getForgeEvent() instanceof CaptureEvent.SuccessfulCapture){
            CaptureEvent.SuccessfulCapture e = (CaptureEvent.SuccessfulCapture) event.getForgeEvent();
            Player p = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
            p.sendMessage("hi");
            Object poke = getField(e.getClass(),e,"pokemon");
            String path = "玩家数据."+p.getName()+".记录";
            String cpPath = "玩家数据."+p.getName()+".可伪装";
            int b = plugin.getConfig().getInt("每种精灵抓多少次才能伪装");
            String pokemonName = (String) getMethod(poke.getClass(),"getPokemonName").invoke(poke);
            List<String> canPre = plugin.getConfig().getStringList(cpPath);
            List<String> pokelist = plugin.getConfig().getStringList(path);
            if (canPre == null) canPre = new ArrayList<>();
            if (canPre.contains(pokemonName)) return;
            if (pokelist == null) pokelist = new ArrayList<>();
            pokelist.add(pokemonName);
            if (pokelist.stream().filter(x->x.equals(pokemonName)).count() == b) {
                canPre.add(pokemonName);
                p.sendMessage("§7新添加可伪装宝可梦:§a"+pokemonName);
                pokelist.removeIf(s -> s.equals(pokemonName));
            }
            plugin.getConfig().set(path,pokelist);
            plugin.getConfig().set(cpPath,canPre);
            plugin.saveConfig();
            plugin.reload();
        }
    }
    @EventHandler
    public void onJoinServer(PlayerJoinEvent event){
        Player p = event.getPlayer();
        for (Player player:Bukkit.getOnlinePlayers()){
            if (runnableMap.containsKey(player.getName())){
                p.hidePlayer(plugin,player);
            }else{
                p.showPlayer(plugin,player);
            }
        }
    }
    @EventHandler
    public void onAttackEntity(PlayerInteractEntityEvent event){
        Player p = event.getPlayer();
        for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
            if (entry.getValue().equals(event.getRightClicked())){
                event.setCancelled(true);
                Player player = Bukkit.getPlayer(entry.getKey());
                if (p.equals(player)){
                    p.sendMessage("§7不要对自己的伪装皮套做奇奇怪怪的事情");
                }else{
                    p.sendMessage("§7你扒拉了一下这个宝可梦,发现好像是皮套");
                    player.sendMessage("§7你的伪装皮套被扒拉了一下");
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
    public void hide(Player player){
        if (entityMap.containsKey(player.getName())){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,()->{
                Entity entity = entityMap.get(player.getName());
                try {
                    hideEntity(player,entity);
                } catch (Exception e) {
                    plugin.getLogger().info("§c错误!编号:006");
                    throw new RuntimeException(e);
                }
            },20L);
        }
    }
}