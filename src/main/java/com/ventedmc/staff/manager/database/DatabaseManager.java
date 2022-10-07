package com.ventedmc.staff.manager.database;

import com.ventedmc.staff.manager.database.config.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {
    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;

    public void init() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseConfig.getSQL_URL());
        hikariConfig.setUsername(databaseConfig.getSQL_USER());
        hikariConfig.setPassword(databaseConfig.getSQL_PASS());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.setConnectionTimeout(TimeUnit.SECONDS.toMillis(15));
        hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(10));
        hikariDataSource = new HikariDataSource(hikariConfig);
        System.out.println("Staff Plugin Database Initialized");
    }

    public void terminate() {
        hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
