package com.ventedmc.staff.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {
    public static ItemStack getItemStack(String item){
        ItemStack itemStack;
        //Check Custom Items first!!!
        item = item.toUpperCase();
        short dmg = 0;
        Material material;
        if (item.contains(":")) {
            dmg = Short.parseShort(item.split(":")[1]);
            material = Material.getMaterial(item.split(":")[0]);
        } else if (item.contains("-")) {
            dmg = Short.parseShort(item.split("-")[1]);
            material = Material.getMaterial(item.split("-")[0]);
        } else {
            material = Material.getMaterial(item);
        }

        itemStack = new ItemStack(material, 1, dmg);
        return itemStack;
    }

    public static ItemStack getItemStack(String item, int amount){
        ItemStack itemStack =  getItemStack(item);
        itemStack.setAmount(amount);
        return itemStack;
    }
    public static ItemStack getItemStack(String item, String displayname){
        ItemStack itemStack =  getItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorize(displayname));
        itemMeta.setLore(new ArrayList<>());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItemStack(String item, String displayname, List<String> lore, short durability){
        ItemStack itemStack =  getItemStack(item);
        itemStack.setDurability(durability);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorize(displayname));
        itemMeta.setLore(color(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack getItemStack(String item, String displayname, String lore){
        ItemStack itemStack =  getItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorize(displayname));
        if(lore.contains("/")){
            String[] lorelines = lore.split("/");
            for(int i = 0; i < lorelines.length; i++){
                lorelines[i] = colorize(lorelines[i]);
            }
            itemMeta.setLore(Arrays.asList(lorelines));
        }else {
            itemMeta.setLore(Arrays.asList(colorize(lore)));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getItemStack(String item, String displayname, String lore, short durability){
        ItemStack itemStack =  getItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorize(displayname));
        if(lore.contains("/")){
            String[] lorelines = lore.split("/");
            for(int i = 0; i < lorelines.length; i++){
                lorelines[i] = colorize(lorelines[i]);
            }
            itemMeta.setLore(Arrays.asList(lorelines));
        }else {
            itemMeta.setLore(Arrays.asList(colorize(lore)));
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setDurability(durability);
        return itemStack;
    }
    public static ItemStack getItemStack(String item, String displayname, String lore, int amount){
        ItemStack itemStack =  getItemStack(item,displayname,lore);
        itemStack.setAmount(amount);
        return itemStack;
    }

    private static List<String> color(List<String> lore){
        List<String> clore = new ArrayList<>();
        for(String s : lore){
            clore.add(colorize(s));
        }
        return clore;
    }

    public static List<String> parsePlaceholders(List<String> list, String placeholder, String replacement) {
        int i = 0;
        for (String str : list) {
            list.set(i, str.replaceAll(placeholder, replacement));
            i++;
        }
        return list;
    }



    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
