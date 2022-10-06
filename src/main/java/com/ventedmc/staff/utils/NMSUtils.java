package com.ventedmc.staff.utils;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSUtils {

    public static void setKey(ItemStack itemStack, String key, String value) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag comp = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        comp.putString(key, value);
        nmsItem.setTag(comp);
        itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItem).getItemMeta());
    }

    public static void setKey(ItemStack itemStack, String key, int value) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag comp = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        comp.putInt(key, value);
        nmsItem.setTag(comp);
        itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItem).getItemMeta());
    }

    public static void setKey(ItemStack itemStack, String key, double value) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag comp = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        comp.putDouble(key, value);
        nmsItem.setTag(comp);
        itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItem).getItemMeta());
    }

    public static boolean hasKey(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        try {
            return nmsItem.getTag() != null && nmsItem.getTag().contains(key);
        } catch (Exception e) {
            return false;
        }
    }

    public static void removeKey(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag comp = nmsItem.getTag();
        comp.remove(key);
        nmsItem.setTag(comp);
        itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItem).getItemMeta());
    }

    public static int getIntValue(ItemStack itemStack, String key) {
        try {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag comp = nmsItem.getTag();
            return comp.getInt(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getStringValue(ItemStack itemStack, String key) {
        try {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag comp = nmsItem.getTag();
            String value = comp.getString(key);
            return value.isEmpty() ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    public static double getDoubleValue(ItemStack itemStack, String key) {
        try {
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
            CompoundTag comp = nmsItem.getTag();
            return comp.getDouble(key);
        } catch (Exception e) {
            return 0D;
        }
    }
}
