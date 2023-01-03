package me.gsqlin.pretendpoke;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.gsqlin.pretendpoke.PretendPoke.plugin;

public class Commands implements CommandExecutor , TabCompleter {
    String[] help = new String[]{
            "§7["+ plugin.getDescription().getPrefix() +"]      命令头↓",
            "§7子命令↓                §r ptendp",
            "§7--reload           §r重载配置",
            "§7--pretend [名字]    §r显示可伪装列表",
            "§7--quitpretend      §r退出伪装",
    };
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
        if (!plugin.getConfig().getBoolean("开启原功能")){
            sender.sendMessage("§c本插件原功能已关闭");
            return false;
        }
        if (args.length>=1){
            Player p = sender instanceof Player? (Player) sender :null;
            if (args[0].equalsIgnoreCase("reload")){
                if (!sender.hasPermission("pretendpoke.reload")) {
                    sender.sendMessage("§c你没有权限使用该命令");
                    return false;
                }
                plugin.reload();
                sender.sendMessage("§a配置已重载!");
            }else if (args[0].equalsIgnoreCase("pretend")){
                if (p == null){sender.sendMessage("§c你不是玩家");return false;}
                if (!sender.hasPermission("pretendpoke.pretend")) {
                    sender.sendMessage("§c你没有权限使用该命令");
                    return false;
                }
                String name = p.getName();
                List<String> list = plugin.getConfig().getStringList("玩家数据."+name+".可伪装");
                if (args.length >= 2){
                    String pokeName = args[1];
                    if (list.contains(pokeName)){
                        if (plugin.getConfig().getBoolean("世界配置.启用")&&
                        !plugin.getConfig().getStringList("世界配置.worlds").contains(p.getWorld().getName())){
                            p.sendMessage("§7该世界不能进行伪装");
                            return false;
                        }
                        try {
                            if (GSQUtil.startPretend(p,pokeName)){
                                p.sendMessage("§7开始伪装");
                            }else{
                                p.sendMessage("§7你已经在伪装了");
                            }
                        } catch (Exception e) {
                            p.sendMessage("§c错误!编号:003");
                            plugin.getLogger().info("§c错误!编号:003");
                            e.printStackTrace();
                        }
                    }else{
                        p.sendMessage("§7没有这种精灵的权限");
                    }
                    return false;
                }else{
                    p.sendMessage("§7以下是你可以伪装的宝可梦");
                    for (String pokeName:list){
                        /*发送可点击执行命令*/
                        p.spigot().sendMessage(new ComponentBuilder("§7[§r"+pokeName+"§7]").
                                bold(true).
                                event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder("§7点击伪装成§r"+pokeName).create())).
                                event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ptendp pretend "+pokeName)).
                                create());
                    }
                    p.sendMessage("§7请在上面选择");
                }
            }else if (args[0].equalsIgnoreCase("quitpretend")){
                if (p == null){sender.sendMessage("§c你不是玩家");return false;}
                if (!sender.hasPermission("pretendpoke.pretend")) {
                    sender.sendMessage("§c你没有权限使用该命令");
                    return false;
                }
                if (!GSQUtil.runnableMap.containsKey(p.getName())){
                    p.sendMessage("§7你没有伪装呢~");
                    return false;
                }
                if (GSQUtil.stopPretend(p)){
                    p.sendMessage("§7你已结束伪装");
                }else{
                    p.sendMessage("§7你没有在伪装,结束失败,如果你在那么请提示腐竹");
                }
            }else{
                sendHelp(sender);
            }
        }else {
            sendHelp(sender);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender,Command command,String label,String[] args) {
        String[] sts = new String[]{
                "help",
                "reload",
                "pretend",
                "quitpretend"
        };

        if (!sender.hasPermission("pretendpoke.help")) return new ArrayList<>();

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("pretend")){
                List<String> canPre = plugin.getConfig().getStringList("玩家数据."+((Player)sender).getName()+".可伪装");
                if (args.length == 2)return canPre.stream().filter(s->s.startsWith(args[1])).collect(Collectors.toList());
            }
            if (args.length == 1)return Arrays.stream(sts).filter(s->s.startsWith(args[0])).collect(Collectors.toList());;
        }
        if (args.length == 0) return Arrays.asList(sts);

        return null;
    }

    public void sendHelp(CommandSender sender){
        if (sender.hasPermission("pretendpoke.help")) {
            sender.sendMessage(help);
            return;
        }
        sender.sendMessage("§c你没有权限使用该命令");
    }
}
