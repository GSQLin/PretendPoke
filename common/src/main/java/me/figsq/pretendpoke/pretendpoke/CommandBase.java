package me.figsq.pretendpoke.pretendpoke;

import com.google.common.collect.Lists;
import me.figsq.pretendpoke.pretendpoke.api.player.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.pokemon.PokeController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandBase<SPECIES, POKEMON> implements TabExecutor {
    public static final String[] helpMsg = {
            "HELP",
            "reload",
            "pretendpoke <Pokémon>",
            "cancelpretend"
    };
    public static final ArrayList<String> subCmd = Lists.newArrayList(
            "help", "reload", "pretendpoke", "cancelpretend"
    );

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String lowerCase = args[0].toLowerCase();
            if (!lowerCase.equals("help") && subCmd.contains(lowerCase)) {
                String permission = "pretendpoke.cmd." + lowerCase;
                if (!sender.hasPermission(permission)) {
                    sender.sendMessage("§cYou do not have permission to this command!");
                    return false;
                }
                switch (lowerCase) {
                    case "reload": {
                        PretendPokePlugin.getInstance().reloadConfig();
                        sender.sendMessage("§aConfig reloaded!");
                        return false;
                    }
                    case "pretendpoke":
                    case "cancelpretend": {
                        boolean isPretend = lowerCase.equals("pretendpoke");
                        if (!(sender instanceof Player)) {
                            sender.sendMessage("§cYou are not a player!");
                            return false;
                        }
                        if (isPretend && args.length < 2) {
                            sender.sendMessage("§cPlease enter the Pokémon you want to disguise!");
                            return false;
                        }

                        String name = args[1];
                        PretendPokePlugin<SPECIES, POKEMON, ?> instance = PretendPokePlugin.getInstance();
                        PokeController<SPECIES, POKEMON, ?> pokeController = instance.getPokeController();
                        POKEMON pokemon = pokeController.createPokemon(pokeController.getSpecies(name));
                        Player player = (Player) sender;
                        PlayerController<POKEMON, ?> playerController = instance.getPlayerController();

                        if (isPretend) {
                            playerController.setPretendPoke(player, pokemon);
                            sender.sendMessage("§aYou are now disguised as §b" + name);
                        } else {
                            playerController.cancelPretendPoke(player);
                            sender.sendMessage("§aYou are no longer disguised!");
                        }
                        return false;
                    }
                }
            }
        }
        sender.sendMessage(helpMsg);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
