package com.jlarrieux.bittrexbot.simulation.DAO;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {


    private static DBConnectionPool dbConnectionPool;
    private ComboPooledDataSource cpds;

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTTION_URL = "jdbc:mysql://localhost/bittrex";

    private DBConnectionPool() throws IOException, SQLException, PropertyVetoException {
         cpds = new ComboPooledDataSource();
        cpds.setDriverClass(JDBC_DRIVER); //loads the jdbc driver
        cpds.setJdbcUrl(DB_CONNECTTION_URL);
        cpds.setUser("root");
        cpds.setPassword("root");
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
    }

    public static DBConnectionPool getInstance() throws IOException, SQLException, PropertyVetoException {
        if (dbConnectionPool == null) {
            dbConnectionPool = new DBConnectionPool();
            return dbConnectionPool;
        } else {
            return dbConnectionPool;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
}
