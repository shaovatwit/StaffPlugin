package com.ventedmc.staff.mode.ui;

import com.ventedmc.staff.mode.StaffModeModule;
import com.ventedmc.staff.utils.ItemUtils;
import com.ventedmc.staff.utils.RomanNumeralUtils;
import l2.envy.gui.icon.DefaultIcon;
import l2.envy.gui.screen.DefaultScreen;
import l2.envy.gui.screen.Screen;
import l2.envy.gui.ui.UserInteface;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class InventoryViewInterface extends UserInteface {
    private Screen screen; // The screen object.
    private Player targetPlayer;
    private StaffModeModule staffModeModule;

    public InventoryViewInterface(StaffModeModule staffModeModule, Player targetPlayer) {
        this.screen = new DefaultScreen()
                .setName(ItemUtils.colorize(staffModeModule.getConfig().getString("inventoryViewInterface.name").replace("<player>", targetPlayer.getName())))
                .setSize(staffModeModule.getConfig().getInt("inventoryViewInterface.size"));

        this.targetPlayer = targetPlayer;
    }

    @Override
    public Screen getScreen(Player player) {
        return screen;
    }

    @Override
    public boolean onJoin(Player player) {
        return true; // Allow the player to join the interface.
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onLeave(OfflinePlayer offlinePlayer) {

    }

    @Override
    public void onInitialize() {
        getL2UI().setCanceledByDefault(true);
        setupScreen();
    }

    public void setupScreen() {
        int inventoryIndex = 0;
        for(ItemStack itemStack : targetPlayer.getInventory().getContents()) {
            if(itemStack == null)continue;
            screen.addIcon(inventoryIndex, new DefaultIcon(itemStack));
            inventoryIndex++;
        }
        int contentIndex = 3;
        for(int slot : staffModeModule.getConfig().getIntegerList("inventoryViewInterface.armorSlots")) {
            ItemStack itemStack = targetPlayer.getInventory().getArmorContents()[contentIndex];
            if(itemStack == null)continue;
            screen.addIcon(slot, new DefaultIcon(targetPlayer.getInventory().getArmorContents()[contentIndex]));
            contentIndex--;
        }
        List<String> activePotionEffects = new ArrayList<>();
        for(PotionEffect potionEffect : targetPlayer.getActivePotionEffects()) {
            activePotionEffects.add(ItemUtils.colorize("&f- " + potionEffect.getType().getName() + " " + RomanNumeralUtils.IntegerToRomanNumeral(potionEffect.getAmplifier() + 1)));
        }
        String playerHealth = targetPlayer.getHealth() + "/" + targetPlayer.getMaxHealth();
        String foodLevel = String.valueOf(targetPlayer.getFoodLevel());

        screen.addIcon(staffModeModule.getConfig().getInt("inventoryViewInterface.slots.healthIcon.item.slot"), new DefaultIcon(ItemUtils.getItemStack(staffModeModule.getConfig().getString("inventoryViewInterface.slots.healthIcon.item.material"),
                        staffModeModule.getConfig().getString("inventoryViewInterface.slots.healthIcon.item.name").replace("<health>", playerHealth))));

        screen.addIcon(staffModeModule.getConfig().getInt("inventoryViewInterface.slots.foodIcon.item.slot"), new DefaultIcon(ItemUtils.getItemStack(staffModeModule.getConfig().getString("inventoryViewInterface.slots.foodIcon.item.material"),
                staffModeModule.getConfig().getString("inventoryViewInterface.slots.foodIcon.item.name").replace("<food>", foodLevel))));

        screen.addIcon(staffModeModule.getConfig().getInt("inventoryViewInterface.slots.potionIcon.item.slot"), new DefaultIcon(ItemUtils.getItemStack(staffModeModule.getConfig().getString("inventoryViewInterface.slots.potionIcon.item.material"),
                staffModeModule.getConfig().getString("inventoryViewInterface.slots.potionIcon.item.name"), activePotionEffects, (short) 0)));
    }
}
