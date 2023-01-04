package me.gsqlin.pretendpoke;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

public class GSQUtil {
    public static Map<String,BukkitRunnable> runnableMap = new HashMap<>();
    public static Map<String,Entity> entityMap = new HashMap<>();
    public static BossBar bossBar = Bukkit.createBossBar("§3你已处于伪装状态", BarColor.BLUE, BarStyle.SEGMENTED_20);
    public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    static boolean startPretend(Player p, String name) throws Exception {
        return false;
    }

    static boolean stopPretend(Player player) {
        return false;
    }

    static void stopAllPretend() {

    }

    static void hideEntity(Player player, Entity entity) throws Exception {

    }

    static void showEntity(Player player, Entity entity) throws Exception {

    }
}
