package com.ventedmc.staff.tickets;

import com.ventedmc.staff.StaffPlugin;
import com.ventedmc.staff.interfaces.ConfigurableModule;
import com.ventedmc.staff.interfaces.Module;
import com.ventedmc.staff.tickets.commands.TicketCommand;
import com.ventedmc.staff.tickets.interfaces.Ticket;
import com.ventedmc.staff.tickets.listener.ResponseListener;
import com.ventedmc.staff.tickets.sql.ResponseDataSource;
import com.ventedmc.staff.tickets.sql.TicketDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class TicketModule implements ConfigurableModule {
    private boolean enabled;

    @Override
    public void enableModule() {
        this.enabled = true;
        createTicketConfig();

        this.responseDataSource = new ResponseDataSource(StaffPlugin.getInstance().getDatabaseManager());
        this.ticketDataSource = new TicketDataSource(StaffPlugin.getInstance().getDatabaseManager(), responseDataSource);

        ticketDataSource.createDefaultTables();
        responseDataSource.createDefaultTables();

        StaffPlugin.getInstance().getCommand("ticket").setExecutor(new TicketCommand(this));
        Bukkit.getPluginManager().registerEvents(new ResponseListener(this), StaffPlugin.getInstance());
    }

    @Override
    public void disableModule() {
        this.enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return "tickets";
    }

    @Getter
    private HashMap<UUID, Ticket> ticketHashMap = new HashMap<>();

    @Getter
    private TicketDataSource ticketDataSource;
    @Getter
    private ResponseDataSource responseDataSource;

    private void createTicketConfig() {
        ticketConfigFile = new File(StaffPlugin.getInstance().getDataFolder(), "tickets.yml");
        if (!ticketConfigFile.exists()) {
            ticketConfigFile.getParentFile().mkdirs();
            StaffPlugin.getInstance().saveResource("tickets.yml", false);
        }

        ticketConfig = new YamlConfiguration();
        try {
            ticketConfig.load(ticketConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return ticketConfig;
    }

    @Override
    public void reloadConfig() {
        try {
            ticketConfig.load(ticketConfigFile);
        }catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        try {
            ticketConfig.save(ticketConfigFile);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File ticketConfigFile;
    private FileConfiguration ticketConfig;
}
