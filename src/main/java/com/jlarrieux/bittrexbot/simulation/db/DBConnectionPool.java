package com.jlarrieux.bittrexbot.simulation.db;

import com.jlarrieux.bittrexbot.Properties.SimulationProperties;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Log
public class DBConnectionPool {
    private static DBConnectionPool dbConnectionPool;
    private ComboPooledDataSource cpds;

    @Autowired
    private DBConnectionPool(SimulationProperties simulationProperties) throws IOException, SQLException, PropertyVetoException {
        log.info("Creating connection Pool...");
        log.info(String.format("Sim start: %b", simulationProperties.isStartFromRecentDate()));
        cpds = new ComboPooledDataSource(true);
        cpds.setDriverClass(simulationProperties.getJdbcDriver());
        cpds.setJdbcUrl(simulationProperties.getConnection_url());
        cpds.setUser(simulationProperties.getUser());
        cpds.setPassword(simulationProperties.getPassword());
        cpds.setMinPoolSize(simulationProperties.getMinPoolSize());
        cpds.setAcquireIncrement(simulationProperties.getAcquireIncrement());
        cpds.setMaxPoolSize(simulationProperties.getMaxPoolSize());

        //todo put good control to check connection is successful before after configuration
        log.info(cpds.toString());
        log.info("Connection pool Configured!");
    }

    public static DBConnectionPool getInstance() throws IOException, SQLException, PropertyVetoException {
        return dbConnectionPool;
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}
