package io.github.redwallhp.villagerutils.commands.villager;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import io.github.redwallhp.villagerutils.VillagerUtils;
import io.github.redwallhp.villagerutils.commands.AbstractCommand;
import io.github.redwallhp.villagerutils.helpers.VillagerHelper;

public class SpawnCommand extends AbstractCommand implements TabCompleter {

    public SpawnCommand(VillagerUtils plugin) {
        super(plugin, "villagerutils.editvillager");
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getUsage() {
        return "/villager spawn [<profession>]";
    }

    @Override
    public boolean action(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot spawn villagers.");
            return false;
        }

        if (args.length > 1) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments. Usage: " + getUsage());
            return false;
        }

        Profession profession = null;
        if (args.length == 1) {
            profession = VillagerHelper.getProfessionFromString(args[0]);
            if (profession == null) {
                sender.sendMessage(ChatColor.RED + "That's not a valid profession.");
                return false;
            }
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        Villager villager = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        if (profession != null) {
            villager.setProfession(profession);
        }

        plugin.getLogger().info(String.format("%s spawned villager at %d, %d, %d", player.getName(),
                                              loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        player.sendMessage(ChatColor.DARK_AQUA + "Spawned villager.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            return VillagerHelper.getProfessionNames().stream()
            .filter(completion -> completion.startsWith(args[1].toLowerCase()))
            .sorted().collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
