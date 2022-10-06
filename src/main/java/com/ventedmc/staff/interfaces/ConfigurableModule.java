package com.ventedmc.staff.interfaces;

import org.bukkit.configuration.file.FileConfiguration;

public interface ConfigurableModule extends Module {
    FileConfiguration getConfig();
    void saveConfig();
    void reloadConfig();
}
