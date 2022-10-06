package com.ventedmc.staff.mode.commands;

import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.mode.objects.StaffPlayer;
import com.ventedmc.staff.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class ModModeCommand implements CommandExecutor {
    private StaffModeModule staffModeModule;

    public ModModeCommand(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ItemUtils.colorize("&4&l(!) &cThis is a player only command."));
            return false;
        }
        if (!sender.hasPermission("staffplugin.staff")) {
            sender.sendMessage(ItemUtils.colorize("&4&l(!) &cYou don't have permissions."));
            return false;
        }

        Player player = (Player) sender;
        if (staffModeModule.getStaffPlayerMap().containsKey(player.getUniqueId())) {
            StaffPlayer staffPlayer = staffModeModule.getStaffPlayerMap().get(player.getUniqueId());
            staffPlayer.disableModMode(player);
            staffModeModule.getStaffPlayerMap().remove(player.getUniqueId());
            player.sendMessage(ItemUtils.colorize("&4&l(!) &cDisabled Mod Mode."));
            return true;
        }
        StaffPlayer staffPlayer = new StaffPlayer(player, staffModeModule.getStaffItemsList());
        staffModeModule.getStaffPlayerMap().put(player.getUniqueId(), staffPlayer);
        player.sendMessage(ItemUtils.colorize("&4&l(!) &cEnabled Mod Mode."));
        return true;
    }
}
