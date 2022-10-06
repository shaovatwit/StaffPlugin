package com.ventedmc.staff.mode.commands;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CPSCheckCommand implements CommandExecutor {
    private StaffModeModule staffModeModule;

    public CPSCheckCommand(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ItemUtils.colorize("&4&l[!] &cOnly players can execute that command."));
            return false;
        }
        if(!commandSender.hasPermission("staffplugin.staff")) {
            commandSender.sendMessage(ItemUtils.colorize("&4&l[!] &cYou do not have permission to execute that command."));
            return false;
        }
        Player staffMember = (Player) commandSender;
        if(args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                commandSender.sendMessage(ItemUtils.colorize("&4&l[!] &cThe specified player is not online!"));
                return false;
            }
            if(staffModeModule.getCpsCheckMap().containsKey(player.getUniqueId())) {
                commandSender.sendMessage(ItemUtils.colorize("&4&l[!] &cThe specified player is already being CPS Checked."));
                return false;
            }
            startTest(staffMember, player);
            staffMember.sendMessage(ItemUtils.colorize(ItemUtils.colorize("&a&l(!) &aYou have started a CPS Check on " + player.getName() + ".")));
            return true;
        }
        commandSender.sendMessage(ItemUtils.colorize("&cIncorrect Usage: /cpscheck <player>"));
        return false;
    }

    public void startTest(Player staffMember, Player targetPlayer) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetPlayer.getUniqueId());
        staffModeModule.getCpsCheckMap().put(targetPlayer.getUniqueId(), 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(StaffPlugin.getInstance(), ()-> {
            if(staffMember != null) {
                if(!staffModeModule.getCpsCheckMap().containsKey(offlinePlayer.getUniqueId())) {
                    staffMember.sendMessage(ItemUtils.colorize("&4&l[!] &cThe player you were CPS Checking has logged off."));
                }
                int actualCPS = staffModeModule.getCpsCheckMap().get(offlinePlayer.getUniqueId()) / (200/20);
                staffMember.sendMessage(ItemUtils.colorize("&a&l(!) &a" + offlinePlayer.getName() + " has clicked an average of " + actualCPS + " CPS."));
            }
            staffModeModule.getCpsCheckMap().remove(offlinePlayer.getUniqueId());
        }, 200L);
    }
}
