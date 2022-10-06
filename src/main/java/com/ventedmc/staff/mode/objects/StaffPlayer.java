package com.ventedmc.staff.mode.objects;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class StaffPlayer {
    private UUID playerUUID;
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Location previousLocation;
    private GameMode previousGamemode;

    public StaffPlayer(Player player, List<StaffItems> staffItems) {
        playerUUID = player.getUniqueId();
        inventoryContents = player.getInventory().getContents();
        armorContents = player.getInventory().getArmorContents();
        previousLocation = player.getLocation();
        previousGamemode = player.getGameMode();

        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        for (StaffItems staffItem : staffItems) {
            player.getInventory().setItem(staffItem.getSlot(), staffItem.getItemStack());
        }
    }

    public void disableModMode(Player player) {
        player.setGameMode(previousGamemode);
        player.teleport(previousLocation);
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);
    }
}
