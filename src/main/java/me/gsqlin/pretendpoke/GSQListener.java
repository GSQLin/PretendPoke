package me.gsqlin.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import me.gsqlin.pretendpoke.forgeEvents.MyForgeEvent;
import me.gsqlin.pretendpoke.forgeEvents.pokeBallImpactEvents.PokeBallIEvent;
import me.gsqlin.pretendpoke.pretendEvents.PickOffPretend;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static me.gsqlin.pretendpoke.GSQUtil.*;
import static me.gsqlin.pretendpoke.PixelUtil.*;
import static me.gsqlin.pretendpoke.PretendPoke.plugin;

public class GSQListener implements Listener {
    public static Class<?> pokeBallIClass;
    static {
        try {
            pokeBallIClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                    Class.forName("com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent"):
                    Class.forName("com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void pokeBallEvent(PokeBallIEvent event) throws InvocationTargetException, IllegalAccessException {
        Object e = pokeBallIClass.cast(event.getEvent());
        Object waitO = bukkitVersion.equalsIgnoreCase("1.12.2")?
                getMethod(e.getClass(),"getEntityHit").invoke(e):
                getMethod(e.getClass(),"getEntityHit").invoke(e) == null?null:((Optional<net.minecraft.entity.Entity>)getMethod(e.getClass(),"getEntityHit").invoke(e)).get();
        if (waitO == null) return;
        Entity entity = getBukkitEntity(waitO);
        if (entity == null) return;
        if (entityMap.containsValue(entity)){
            getMethod(e.getClass(),"setCanceled",boolean.class).invoke(e,true);
        }
    }

    @EventHandler
    public void play(MyForgeEvent event) throws Exception {
        //?????????????????????????????????????????????????????????
        if (event.getForgeEvent() instanceof BattleStartedEvent){
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            Object bc = getField(e.getClass(),e,"bc");
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
                }
            }
        }
        //???????????????????????????
/*        if (event.getForgeEvent() instanceof BeatWildPixelmonEvent){
            BeatWildPixelmonEvent e = (BeatWildPixelmonEvent) event.getForgeEvent();
            Entity entity = getBukkitEntity(getMethod(e.wpp.getClass(),"getEntity").invoke(e.wpp));
            for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
                if (entry.getValue().equals(entity)){
                    Player player = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
                    Player p = Bukkit.getPlayer(entry.getKey());
                    stopPretend(p);
                    if (player == p){
                        player.sendMessage("??7????????????????????????????????????");
                    }else{
                        player.sendMessage("??7?????????????????????????????????");
                        p.sendMessage("??7??????????????????????????????");
                    }
                    break;
                }
            }
        }*/
        if (event.getForgeEvent() instanceof CaptureEvent.SuccessfulCapture){
            CaptureEvent.SuccessfulCapture e = (CaptureEvent.SuccessfulCapture) event.getForgeEvent();
            Player p = (Player) getBukkitEntity(getField(e.getClass(),e,"player"));
            Object poke = getField(e.getClass(),e,"pokemon");
            String path = "????????????."+p.getName()+".??????";
            String cpPath = "????????????."+p.getName()+".?????????";
            int b = plugin.getConfig().getInt("????????????????????????????????????");
            String pokemonName = (String) getMethod(poke.getClass(),"getPokemonName").invoke(poke);
            List<String> canPre = plugin.getConfig().getStringList(cpPath);
            List<String> pokelist = plugin.getConfig().getStringList(path);
            if (canPre == null) canPre = new ArrayList<>();
            if (canPre.contains(pokemonName)) return;
            if (pokelist == null) pokelist = new ArrayList<>();
            pokelist.add(pokemonName);
            if (pokelist.stream().filter(x->x.equals(pokemonName)).count() == b) {
                canPre.add(pokemonName);
                p.sendMessage("??7???????????????????????????:??a"+pokemonName);
                pokelist.removeIf(s -> s.equals(pokemonName));
            }
            plugin.getConfig().set(path,pokelist);
            plugin.getConfig().set(cpPath,canPre);
            plugin.saveConfig();
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
    public void onInteractEntity(PlayerInteractEntityEvent event){
        if (!plugin.getConfig().getBoolean("????????????"))return;
        Player p = event.getPlayer();
        if (event.getHand().equals(EquipmentSlot.OFF_HAND))return;
        for (Map.Entry<String, Entity> entry : entityMap.entrySet()) {
            if (entry.getValue().equals(event.getRightClicked())){
                event.setCancelled(true);
                Player player = Bukkit.getPlayer(entry.getKey());
                PickOffPretend pickEvent;
                String[] messages = new String[2];
                if (p.equals(player)){
                    messages[0] = "??7??????????????????????????????????????????????????????";
                    pickEvent = new PickOffPretend(p,null,entry.getValue(),messages);
                    Bukkit.getPluginManager().callEvent(pickEvent);
                    p.sendMessage(pickEvent.getMessages()[0]);
                }else{
                    messages[0] = "??7??????????????????:??3"+player.getName()+"??7?????????";
                    messages[1] = "??6??????????????????????????????";
                    pickEvent = new PickOffPretend(p,player,entry.getValue(),messages);
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
    public void hide(Player player){
        if (entityMap.containsKey(player.getName())){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,()->{
                Entity entity = entityMap.get(player.getName());
                try {
                    hideEntity(player,entity);
                } catch (Exception e) {
                    plugin.getLogger().info("??c??????!??????:006");
                    throw new RuntimeException(e);
                }
            },20L);
        }
    }
}