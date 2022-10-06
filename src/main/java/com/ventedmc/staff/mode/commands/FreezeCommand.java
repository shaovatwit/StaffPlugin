package com.ventedmc.staff.mode.commands;

import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    private StaffModeModule staffModeModule;

    public FreezeCommand(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ItemUtils.colorize("&4&l(!) &cYou must be a player."));
            return false;
        }
        if (!sender.hasPermission("staffplugin.staff")) {
            sender.sendMessage(ItemUtils.colorize("&4&l(!) &cYou do not have permissions."));
            return false;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(ItemUtils.colorize("&4&l[!] &cThe specified player is not online!"));
                return false;
            }
            if (staffModeModule.getFrozenPlayers().contains(target.getUniqueId())) {
                staffModeModule.getFrozenPlayers().remove(target.getUniqueId());
                target.sendMessage(ItemUtils.colorize("&4&l(!) &cYou have been unfrozen."));
                return true;
            }
            staffModeModule.getFrozenPlayers().add(target.getUniqueId());
            sendFreezeMessage(target);
            return true;
        }
        sender.sendMessage(ItemUtils.colorize("&cIncorrect Usage: /freeze <player>"));
        return false;
    }

    public void sendFreezeMessage(Player p) {
        p.sendMessage("§f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        p.sendMessage("§f\u2588\u2588\u2588\u2588§c\u2588§f\u2588\u2588\u2588\u2588");
        p.sendMessage("§f\u2588\u2588\u2588§c\u2588§0\u2588§c\u2588§f\u2588\u2588\u2588");
        p.sendMessage("§f\u2588\u2588§c\u2588§6\u2588§0\u2588§6\u2588§c\u2588§f\u2588\u2588");
        p.sendMessage("§f\u2588\u2588§c\u2588§6\u2588§0\u2588§6\u2588§c\u2588§f\u2588\u2588 §7You have been frozen by a staff member.");
        p.sendMessage("§f\u2588\u2588§c\u2588§6\u2588§0\u2588§6\u2588§c\u2588§f\u2588\u2588  §7If you disconnect you will be §c§l§nBANNED");
        p.sendMessage("§f\u2588§c\u2588§6\u2588\u2588\u2588\u2588\u2588§c\u2588§f\u2588       §7Please connect to our Discord.");
        p.sendMessage("§c\u2588§6\u2588\u2588\u2588§0\u2588§6\u2588\u2588\u2588§c\u2588            §c§ndiscord.ventedmc.com");
        p.sendMessage("§c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        p.sendMessage("§f\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
    }
}
