package me.gsqlin.pretendpoke.pretendpoke;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.PokeBallImpactEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import me.fullidle.ficore.ficore.common.api.event.ForgeEvent;
import me.gsqlin.pretendpoke.pretendpoke.api.CommonData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

import static me.gsqlin.pretendpoke.pretendpoke.api.CommonData.plugin;
import static me.gsqlin.pretendpoke.pretendpoke.PretendHelper.*;

public class PlayerListener implements Listener {
    @EventHandler
    public void onForge(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BattleStartedEvent) {
            BattleStartedEvent e = (BattleStartedEvent) event.getForgeEvent();
            for (BattleParticipant participant : e.getBattleController().participants) {
                for (PixelmonWrapper wrapper : participant.allPokemon) {
                    if (entityMap.containsValue(CommonData.versionHelper.getBukkitEntity(wrapper.entity))) {
                        e.getBattleController().endBattle();
                        return;
                    }
                }
            }
        }
        if (event.getForgeEvent() instanceof PokeBallImpactEvent) {
            PokeBallImpactEvent e = (PokeBallImpactEvent) event.getForgeEvent();
            if (e.getEntityHit().isPresent()) {
                if (e.getEntityHit().get() instanceof PixelmonEntity) {
                    if (entityMap.containsValue(CommonData.versionHelper.getBukkitEntity(e.getEntityHit().get())))
                        e.setCanceled(true);
                }
            }
        }
        if (event.getForgeEvent() instanceof CaptureEvent.SuccessfulCapture) {
            CaptureEvent.SuccessfulCapture e = (CaptureEvent.SuccessfulCapture) event.getForgeEvent();
            Player player = ((Player) CommonData.versionHelper.getBukkitEntity(e.getPlayer()));
            String path = "玩家数据." + player.getName() + ".记录";
            String cpPath = "玩家数据." + player.getName() + ".可伪装";
            FileConfiguration config = plugin.getConfig();
            String pokemonName = e.getPokemon().getPokemonName();
            List<String> canPre = config.getStringList(cpPath);
            List<String> pokelist = config.getStringList(path);
            if (canPre.contains(pokemonName)) return;
            pokelist.add(pokemonName);
            if (pokelist.stream().filter(s -> s.equals(pokemonName)).count() == config.getInt("每种精灵抓多少次才能伪装")) {
                canPre.add(pokemonName);
                player.sendMessage("§7新添加可伪装宝可梦:§a" + pokemonName);
                pokelist.removeIf(s -> s.equals(pokemonName));
            }
            plugin.getConfig().set(path, pokelist);
            plugin.getConfig().set(cpPath, canPre);
            plugin.saveConfig();
        }
    }
}
