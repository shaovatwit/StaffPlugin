package com.ventedmc.staff.mode.listeners;

import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.mode.objects.StaffPlayer;
import com.ventedmc.staff.mode.ui.InventoryViewInterface;
import com.ventedmc.staff.utils.ItemUtils;
import com.ventedmc.staff.utils.NMSUtils;
import l2.envy.gui.L2UI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class StaffModeListener implements Listener {

    /**
     * EVENTS TO CANCEL.
     * - PlayerDropItemEvent
     * - PlayerPickupItemEvent
     * - Permission Based: BlockBreak, BlockPlace
     * - EntityDamageByEntityEvent
     * - InventoryClickEvent (Permission Based).
     */

    private StaffModeModule staffModeModule;

    public StaffModeListener(StaffModeModule staffModeModule) {
        this.staffModeModule = staffModeModule;
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if(staffModeModule.getStaffPlayerMap().containsKey(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        if(staffModeModule.getStaffPlayerMap().containsKey(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    // staffplugin.staff.admin

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(e.getPlayer().hasPermission("staffplugin.staff.admin")) return;
        if(staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.getPlayer().hasPermission("staffplugin.staff.admin")) return;
        if(staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player || e.getDamager() instanceof Player) {
            Player entity = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if(staffModeModule.getStaffPlayerMap().containsKey(entity.getUniqueId())) {
                e.setCancelled(true);
            }
            if(staffModeModule.getStaffPlayerMap().containsKey(damager.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        if(e.getWhoClicked().hasPermission("staffplugin.staff.admin")) return;
        if(staffModeModule.getStaffPlayerMap().containsKey(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if(!(e.getRightClicked() instanceof Player)) return;
        Player rightClicked = (Player) e.getRightClicked();
        if(!staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
            return;
        }
        if(e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
            ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
            if(NMSUtils.hasKey(itemInHand, "CPS_CHECK")) {
                e.getPlayer().performCommand("cpscheck " + rightClicked.getName());
            } else if(NMSUtils.hasKey(itemInHand, "INVENTORY_VIEW")) {
                L2UI.getInstance().getRegistrar().getStaticL2UserInterface(new InventoryViewInterface(staffModeModule, rightClicked)).join(e.getPlayer());
            } else if(NMSUtils.hasKey(itemInHand, "FREEZE")) {
                e.getPlayer().performCommand("freeze " + rightClicked.getName());
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getItem() != null && e.getItem().getType() != Material.AIR) {
                if(!staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
                    return;
                }
                if(NMSUtils.hasKey(e.getItem(), "ANTI_CHEAT_VIOLATIONS")) {
                    e.setCancelled(true);
                    // Open UI
                } else if(NMSUtils.hasKey(e.getItem(), "RANDOM_TELEPORT")) {
                    if(Bukkit.getOnlinePlayers().size() <= 1) {
                        e.getPlayer().sendMessage(ItemUtils.colorize("&c&l(!) &cYou are the only player online."));
                        e.setCancelled(true);
                        return;
                    }
                    e.setCancelled(true);
                    e.getPlayer().teleport(getRandomPlayer());
                }
            }
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
            StaffPlayer staffPlayer = staffModeModule.getStaffPlayerMap().get(e.getPlayer().getUniqueId());
            staffPlayer.disableModMode(e.getPlayer());
            staffModeModule.getStaffPlayerMap().remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if(staffModeModule.getStaffPlayerMap().containsKey(e.getPlayer().getUniqueId())) {
            StaffPlayer staffPlayer = staffModeModule.getStaffPlayerMap().get(e.getPlayer().getUniqueId());
            staffPlayer.disableModMode(e.getPlayer());
            staffModeModule.getStaffPlayerMap().remove(e.getPlayer().getUniqueId());
        }
    }

    public Player getRandomPlayer() {
        Random rand = new Random();
        ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        int i = rand.nextInt(Bukkit.getOnlinePlayers().size());
        Player selectedPlayer = onlinePlayers.get(i);
        onlinePlayers.clear();
        return selectedPlayer;
    }
}
