package me.gsqlin.pretendpoke.pretendpoke;

import me.gsqlin.pretendpoke.pretendpoke.api.CommonData;
import me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents.EndPretendPokeEvent;
import me.gsqlin.pretendpoke.pretendpoke.api.pretendEvents.StartPretendPokeEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PretendHelper {
    public static Map<String,BukkitRunnable> runnableMap = new HashMap<>();
    public static Map<String,Entity> entityMap = new HashMap<>();
    public static BossBar bossBar = Bukkit.createBossBar("§3你已处于伪装状态", BarColor.BLUE, BarStyle.SEGMENTED_20);
    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static boolean startPretend(Player p,String name) {
        if (runnableMap.containsKey(p.getName())) return false;
        entityMap.remove(p.getName());
        bossBar.addPlayer(p);
        bossBar.setVisible(true);
        StartPretendPokeEvent startEvent = new StartPretendPokeEvent(p, CommonData.versionHelper.createPokemon(name));
        Bukkit.getPluginManager().callEvent(startEvent);
        if (startEvent.isCancelled())return false;
        Player eventPlayer = startEvent.getPlayer();
        Object pokemon = startEvent.getPokemon();
        Team team;
        try {
            team = scoreboard.registerNewTeam(eventPlayer.getName()+"伪装");
        }catch (IllegalArgumentException e){
            eventPlayer.sendMessage("§c错误!编号:004");
            CommonData.plugin.getLogger().info("§c错误!编号:004");
            e.printStackTrace();
            return false;
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.addEntry(eventPlayer.getUniqueId().toString());
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Team te = scoreboard.getTeam(eventPlayer.getName()+"伪装");
                if (te == null){
                    cancel();
                    stopPretend(eventPlayer);
                    return;
                }
                Player player = Bukkit.getPlayer(eventPlayer.getName());
                if (player == null){
                    cancel();
                    stopPretend(eventPlayer);
                }else{
                    LivingEntity entity;
                    try {
                        entity = (LivingEntity) CommonData.versionHelper.getOrSpawnPoke(pokemon,player);
                        entity.teleport(player.getLocation());
                        entity.getLocation().setYaw(player.getEyeLocation().getYaw());
                        if (!entity.isInvulnerable()) entity.setInvulnerable(true);
                        if (entity.hasAI())entity.setAI(false);
                        if (!entityMap.containsKey(player.getName())){
                            entityMap.put(player.getName(), entity);
                            hideEntity(player,entity);
                        }else {entityMap.replace(player.getName(),entity);}
                    } catch (Exception e) {
                        cancel();
                        stopPretend(player);
                        player.sendMessage("§c错误!编号:002");
                        CommonData.plugin.getLogger().info("§c错误!编号:002");
                        e.printStackTrace();
                        return;
                    }
                    if (!te.hasEntry(entity.getUniqueId().toString())) te.addEntry(entity.getUniqueId().toString());
                }
            }
        };
        runnable.runTaskTimer(CommonData.plugin,0,CommonData.plugin.getConfig().getInt("伪装更新"));
        runnableMap.put(eventPlayer.getName(),runnable);
        for (Player onlineP:Bukkit.getOnlinePlayers()) onlineP.hidePlayer(CommonData.plugin,eventPlayer);
        return true;
    }
    public static boolean stopPretend(Player player) {
        EndPretendPokeEvent event = new EndPretendPokeEvent(player,entityMap.get(player.getName()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;
        Player p = event.getPlayer();
        bossBar.removePlayer(p);
        if (!runnableMap.containsKey(p.getName()))return false;
        if (runnableMap.containsKey(p.getName())) {
            if (!runnableMap.get(p.getName()).isCancelled()) runnableMap.get(p.getName()).cancel();
            runnableMap.remove(p.getName());
        }
        Team team = scoreboard.getTeam(p.getName()+"伪装");
        if (team != null) team.unregister();
        try {
            ((LivingEntity) event.getPokemon()).setHealth(0);
            event.getPokemon().remove();
        } catch (Exception e) {
            CommonData.plugin.getLogger().info("§7杀死伪装剩下的模型失败,不用在乎");
            e.printStackTrace();
        }
        entityMap.remove(p.getName());
        for (Player onlineplayer:Bukkit.getOnlinePlayers()){
            onlineplayer.showPlayer(CommonData.plugin,p);
        }
        return true;
    }
    static void stopAllPretend(){
        Set<String> ns = entityMap.keySet();
        for (String pName:ns){
            stopPretend(Bukkit.getPlayer(pName));
        }
    }
    static void hideEntity(Player player,Entity entity) throws Exception{
        CommonData.versionHelper.hideEntityFromPlayer(player,entity);
    }
/*    static void showEntity(Player player,Entity entity) throws Exception{
        Class<?> packetClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                Class.forName("net.minecraft.network.play.server.SPacketSpawnMob"):
                Class.forName("net.minecraft.network.play.server.SSpawnMobPacket");
        Class<?> livingClass = bukkitVersion.equalsIgnoreCase("1.12.2")?
                Class.forName("net.minecraft.entity.EntityLivingBase"):
                Class.forName("net.minecraft.entity.LivingEntity");
        Constructor constructor = getConstructor(packetClass,livingClass);
        net.minecraft.entity.Entity en = getMinecraftEntity(entity);
        Object packet = constructor.newInstance(livingClass.cast(en));
        PixelUtil.sendPacket(player,packet);
    }*/
}
