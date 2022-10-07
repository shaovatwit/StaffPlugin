package com.ventedmc.staff.mode.listeners;

import com.ventedmc.staff.mode.StaffModeModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CPSListener implements Listener {
    private StaffModeModule staffModeModule;

    public CPSListener(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent e) {
        if(e.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            if(staffModeModule.getCpsCheckMap().containsKey(e.getPlayer().getUniqueId())) {
                staffModeModule.getCpsCheckMap().replace(e.getPlayer().getUniqueId(), staffModeModule.getCpsCheckMap().get(e.getPlayer().getUniqueId()) + 1);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        staffModeModule.getCpsCheckMap().remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        staffModeModule.getCpsCheckMap().remove(e.getPlayer().getUniqueId());
    }
}
