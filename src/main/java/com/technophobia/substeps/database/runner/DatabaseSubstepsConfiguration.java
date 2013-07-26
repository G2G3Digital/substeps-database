/*
 *  Copyright Technophobia Ltd & Alan Raison 2013
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.technophobia.substeps.database.runner;

import java.net.URL;

import com.technophobia.substeps.model.Configuration;

/**
 * Created with IntelliJ IDEA. User: dmoss Date: 22/01/13 Time: 16:27
 */
public class DatabaseSubstepsConfiguration {

    private static final String DRIVER_CLASS;
    private static final String JDBC_URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String NAMED_QUERY_PROPERTY_FILE;
    private static final String DATABASE_TYPE;
    private static final int MAX_POOLED_STATEMENTS;
    private static final int MIN_POOL_SIZE;
    private static final int MAX_POOL_SIZE;
    private static final int ACQUIRE_INCREMENT;
    private static final int MAX_IDLE_TIME;
    private static final int INITIAL_POOL_SIZE;

    static {
        final URL defaultConfigurationURL = DatabaseSubstepsConfiguration.class
                .getResource("/default-database-substeps.properties");
        Configuration.INSTANCE.addDefaultProperties(defaultConfigurationURL, "default-database");

        DRIVER_CLASS = Configuration.INSTANCE.getString("database.driver.class");
        JDBC_URL = Configuration.INSTANCE.getString("database.jdbc.url");
        USER = Configuration.INSTANCE.getString("database.user");
        PASSWORD = Configuration.INSTANCE.getString("database.password");
        NAMED_QUERY_PROPERTY_FILE = Configuration.INSTANCE.getString("database.query.file");
        DATABASE_TYPE = Configuration.INSTANCE.getString("database.type");
        MAX_POOLED_STATEMENTS = Configuration.INSTANCE.getInt("database.max.pooled.statements");
        MIN_POOL_SIZE = Configuration.INSTANCE.getInt("database.min.pool.size");
        MAX_POOL_SIZE = Configuration.INSTANCE.getInt("database.max.pool.size");
        ACQUIRE_INCREMENT = Configuration.INSTANCE.getInt("database.acquire.increment");
        MAX_IDLE_TIME = Configuration.INSTANCE.getInt("database.max.idle.time");
        INITIAL_POOL_SIZE = Configuration.INSTANCE.getInt("database.initial.pool.size");
    }


    public static String getDriverClass() {
        return DRIVER_CLASS;
    }


    public static String getJdbcUrl() {
        return JDBC_URL;
    }


    public static String getUser() {
        return USER;
    }


    public static String getPassword() {
        return PASSWORD;
    }


    public static String getNamedQueryPropertyFile() {
        return NAMED_QUERY_PROPERTY_FILE;
    }


    public static int getMaxPooledStatements() {
        return MAX_POOLED_STATEMENTS;
    }


    public static String getDatabaseType() {
        return DATABASE_TYPE;
    }


    public static int getMinPoolSize() {
        return MIN_POOL_SIZE;
    }


    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }


    public static int getAcquireIncrement() {
        return ACQUIRE_INCREMENT;
    }


    public static int getMaxIdleTime() {
        return MAX_IDLE_TIME;
    }


    public static int getInitialPoolSize() {
        return INITIAL_POOL_SIZE;
    }
}
