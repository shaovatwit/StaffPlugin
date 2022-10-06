package com.ventedmc.staff.mode;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.interfaces.ConfigurableModule;
import com.ventedmc.staff.mode.commands.CPSCheckCommand;
import com.ventedmc.staff.mode.commands.FreezeCommand;
import com.ventedmc.staff.mode.commands.ModModeCommand;
import com.ventedmc.staff.mode.listeners.CPSListener;
import com.ventedmc.staff.mode.listeners.FreezeListener;
import com.ventedmc.staff.mode.listeners.StaffModeListener;
import com.ventedmc.staff.mode.objects.StaffItems;
import com.ventedmc.staff.mode.objects.StaffPlayer;
import com.ventedmc.staff.utils.ItemUtils;
import com.ventedmc.staff.utils.NMSUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StaffModeModule implements ConfigurableModule {
    private StaffPlugin staffPlugin;
    private boolean enabled;
    private List<Listener> registeredListeners = new ArrayList<>();

    public StaffModeModule(StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;
        registeredListeners.add(new StaffModeListener(this));
        registeredListeners.add(new CPSListener(this));
        registeredListeners.add(new FreezeListener(this));
    }

    @Override
    public void enableModule() {
        this.enabled = true;
        createStaffConfig();

        setupStaffItems();

        staffPlugin.getCommand("mod").setExecutor(new ModModeCommand(this));
        staffPlugin.getCommand("freeze").setExecutor(new FreezeCommand(this));
        staffPlugin.getCommand("cpscheck").setExecutor(new CPSCheckCommand(this));

        PluginManager pluginManager = Bukkit.getPluginManager();
        registeredListeners.forEach(listener -> pluginManager.registerEvents(listener, staffPlugin));
    }

    @Override
    public void disableModule() {
        this.enabled = false;

        registeredListeners.forEach(HandlerList::unregisterAll);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "mode";
    }

    private void setupStaffItems() {
        for(String s : getConfig().getConfigurationSection("staffInventoryItems").getKeys(false)) {
            String baseStr = "staffInventoryItems." + s + ".";
            String material = getConfig().getString(baseStr + "material");
            String name = getConfig().getString(baseStr + "name");
            String nbtTag = getConfig().getString(baseStr + "nbtTag");
            List<String> lore = getConfig().getStringList(baseStr + "lore");
            int slot = getConfig().getInt(baseStr + "slot");

            ItemStack itemStack = ItemUtils.getItemStack(material, name, lore, (short) 0);
            NMSUtils.setKey(itemStack, nbtTag, nbtTag);

            staffItemsList.add(new StaffItems(itemStack, slot));
        }
    }

    @Getter
    private List<StaffItems> staffItemsList = new ArrayList<>();
    @Getter
    private Object2ObjectOpenHashMap<UUID, StaffPlayer> staffPlayerMap = new Object2ObjectOpenHashMap<>();
    @Getter
    private ObjectOpenHashSet<UUID> frozenPlayers = new ObjectOpenHashSet<>();
    @Getter
    private Object2ObjectOpenHashMap<UUID, Integer> cpsCheckMap = new Object2ObjectOpenHashMap<>();

    /**
     * Configuration Setup
     */

    private void createStaffConfig() {
        staffConfigFile = new File(StaffPlugin.getInstance().getDataFolder(), "staff.yml");
        if (!staffConfigFile.exists()) {
            staffConfigFile.getParentFile().mkdirs();
            StaffPlugin.getInstance().saveResource("staff.yml", false);
        }

        staffConfig = new YamlConfiguration();
        try {
            staffConfig.load(staffConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return staffConfig;
    }

    @Override
    public void reloadConfig() {
        try {
            staffConfig.load(staffConfigFile);
        }catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        try {
            staffConfig.save(staffConfigFile);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File staffConfigFile;
    private FileConfiguration staffConfig;
}
