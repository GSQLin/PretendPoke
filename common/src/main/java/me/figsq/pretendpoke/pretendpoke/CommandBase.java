package me.figsq.pretendpoke.pretendpoke;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public final class CommandBase implements TabExecutor {
    public static final CommandBase INSTANCE = new CommandBase();
    public static final String[] helpMsg = {
            "HELP",
            ""
    };

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        sender.sendMessage(helpMsg);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
