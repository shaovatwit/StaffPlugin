package com.ventedmc.staff.mode.objects;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class StaffItems {
    private ItemStack itemStack;
    private int slot;

    public StaffItems(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }
}
