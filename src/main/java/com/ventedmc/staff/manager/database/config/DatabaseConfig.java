package com.ventedmc.staff.manager.database.config;

import com.ventedmc.staff.StaffPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DatabaseConfig {
    private FileConfiguration config;
    private String SQL_IP;
    private String SQL_PORT;
    private String SQL_USER;
    private String SQL_PASS;
    private String SQL_DB;
    private String SQL_URL;

    public DatabaseConfig() {
        File configFile = new File(StaffPlugin.getInstance().getDataFolder(), "database.yml");
        if (!configFile.exists()) {
            StaffPlugin.getInstance().saveResource("database.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        load();
    }

    public void load() {
        this.SQL_IP = config.getString("SQL-IP");
        this.SQL_PORT = config.getString("SQL-PORT");
        this.SQL_USER = config.getString("SQL-USER");
        this.SQL_PASS = config.getString("SQL-PASS");
        this.SQL_DB = config.getString("SQL-DATABASE");
        this.SQL_URL = "jdbc:mysql://" + SQL_IP + ":" + SQL_PORT + "/" + SQL_DB;
    }

    public String getSQL_IP() {
        return SQL_IP;
    }

    public String getSQL_PORT() {
        return SQL_PORT;
    }

    public String getSQL_USER() {
        return SQL_USER;
    }

    public String getSQL_PASS() {
        return SQL_PASS;
    }

    public String getSQL_DB() {
        return SQL_DB;
    }

    public String getSQL_URL() {
        return SQL_URL;
    }
}
