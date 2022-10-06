package com.ventedmc.staff;

import com.ventedmc.staff.manager.ModuleManager;
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

        this.moduleManager = new ModuleManager();

    }

    @Override
    public void onDisable() {

    }
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private static StaffPlugin instance;
}
