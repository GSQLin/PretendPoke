package me.figsq.pretendpoke.pretendpoke;

import com.google.common.collect.Lists;
import me.figsq.pretendpoke.pretendpoke.api.PlayerController;
import me.figsq.pretendpoke.pretendpoke.api.PokeController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public final PretendPokePlugin<SPECIES,POKEMON,?> plugin;

    public CommandBase(){
        this.plugin = PretendPokePlugin.getInstance();
    }

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

                        PretendPokePlugin<SPECIES, POKEMON, ?> instance = PretendPokePlugin.getInstance();
                        PokeController<SPECIES, POKEMON, ?> pokeController = instance.getPokeController();
                        Player player = (Player) sender;
                        PlayerController<POKEMON, ?> playerController = instance.getPlayerController();

                        POKEMON old = playerController.getPretendPoke(player);
                        if (isPretend) {
                            if (old != null) {
                                sender.sendMessage("§cYou are already disguised!");
                                return false;
                            }

                            String name = args[1];
                            SPECIES species = pokeController.getSpecies(name);
                            if (species == null) {
                                sender.sendMessage("§cPokémon not found!");
                                return false;
                            }
                            POKEMON pokemon = pokeController.createPokemon(species);
                            playerController.setPretendPoke(player, pokemon,true);
                            sender.sendMessage("§aYou are now disguised as §b" + name);
                        } else {
                            if (old == null) {
                                sender.sendMessage("§cYou are not disguised!");
                                return false;
                            }
                            playerController.cancelPretendPoke(player,true);
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
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length < 1) return subCmd;
        if (args.length == 1) {
            return subCmd.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args[0].equalsIgnoreCase("pretendpoke")) {
            PokeController<SPECIES, POKEMON, ?> pokeController = this.plugin.getPokeController();
            List<String> collect = pokeController.getAllSpecies().stream().map(pokeController::getSpeciesName).collect(Collectors.toList());
            String arg = args[1].toLowerCase();
            if (arg.isEmpty()) return collect;
            return collect.stream().filter(s -> s.toLowerCase().startsWith(arg)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
