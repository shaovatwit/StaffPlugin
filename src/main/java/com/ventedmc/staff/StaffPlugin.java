package com.ventedmc.staff;

import com.ventedmc.staff.manager.ModuleManager;
import com.ventedmc.staff.manager.database.DatabaseManager;
import l2.envy.gui.L2UI;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        instance = this;
        L2UI.initialize(this);
        this.databaseManager = new DatabaseManager();
        databaseManager.init();

        this.moduleManager = new ModuleManager();

    }

    @Override
    public void onDisable() {
        databaseManager.terminate();
    }

    @Getter
    private ModuleManager moduleManager;
    @Getter
    private DatabaseManager databaseManager;
    @Getter
    private static StaffPlugin instance;
}
