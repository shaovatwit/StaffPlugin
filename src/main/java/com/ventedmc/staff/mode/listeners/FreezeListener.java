package com.ventedmc.staff.mode.listeners;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.mode.commands.FreezeCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener implements Listener {
    private StaffModeModule staffModeModule;

    public FreezeListener(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (staffModeModule.getFrozenPlayers().contains(e.getPlayer().getUniqueId())) {
            e.setTo(e.getFrom());
            sendFreezeMessage(e.getPlayer());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (staffModeModule.getFrozenPlayers().contains(e.getPlayer().getUniqueId())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + e.getPlayer().getName() + " Logged out whilst frozen.");
        }
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
