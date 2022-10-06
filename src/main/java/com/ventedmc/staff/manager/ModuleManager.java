package com.ventedmc.staff.manager;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.interfaces.ConfigurableModule;
import com.ventedmc.staff.interfaces.Module;
import com.ventedmc.staff.mode.StaffModeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<Module> modules = new ArrayList<Module>(){{
        add(new StaffModeModule(StaffPlugin.getInstance()));
    }};

    public ModuleManager() {
        modules.forEach(Module::enableModule);
    }

    public void disableModules() {
        modules.forEach(Module::disableModule);
    }

    public void reloadConfigs() {
        modules.stream().filter(module -> module instanceof ConfigurableModule)
                .map(module -> (ConfigurableModule) module)
                .forEach(ConfigurableModule::reloadConfig);
    }

    public void disableModule(String moduleName) {
        modules.stream().filter(module -> module.getName().equalsIgnoreCase(moduleName))
                .forEach(Module::disableModule);
    }

    public void enableModule(String moduleName) {
        modules.stream().filter(module -> module.getName().equalsIgnoreCase(moduleName))
                .forEach(Module::enableModule);
    }

    public List<String> getAllModules() {
        return modules.stream().map(Module::getName)
                .collect(Collectors.toList());
    }

    public boolean doesModuleExist(String moduleName) {
        for (Module module : modules) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getModules(boolean enabled) {
        return modules.stream().filter(module -> module.isEnabled() == enabled)
                .map(Module::getName)
                .collect(Collectors.toList());
    }

    public Module getModule(String moduleName) {
        for (Module module : modules) {
            if(module.getName().equalsIgnoreCase(moduleName)) {
                return module;
            }
        }
        return null;
    }
}
