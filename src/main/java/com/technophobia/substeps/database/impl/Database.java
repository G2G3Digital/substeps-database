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

package com.technophobia.substeps.database.impl;

import static com.technophobia.substeps.database.runner.DatabaseSetupTearDown.getExecutionContext;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.exception.SubstepsException;

/**
 * Encapsulate DB operations
 * 
 * @author imoore
 * 
 */
public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);


    /**
     * This class should be solely responsible for executing queries and
     * returning results, what happens with those results is the responsibility
     * of the caller, typically to stash them, however this may not always be
     * the case
     * 
     * @deprecated
     * @param sql
     */
    @Deprecated
    public static void execute(final String sql) {

        final List<Map<String, Object>> results = query(sql);

        getExecutionContext().stashResultSet(results);
    }


    public static void update(final String sql) {

        Connection connection = null;
        try {
            connection = DatabaseSetupTearDown.getConnectionContext().getConnection();
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (final SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new SubstepsException("Executing sql update [" + sql + "] failed:" + ex.getMessage(), ex);
        } finally {
            close(connection);
        }
    }


    public static List<Map<String, Object>> query(final String sql) {
        logger.debug("sql = " + sql);
        Connection conn = null;

        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> results = null;

        try {
            // Get a connection
            conn = getConnection();

            // if no connection just return
            if (conn == null) {
                return results;
            }

            stmt = conn.createStatement();

            // Execute query
            rs = stmt.executeQuery(sql);

            results = buildResultsMap(rs);

        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            // Close all resource associated with this call
            close(conn);
        }
        return results;
    }


    /**
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> buildResultsMap(final ResultSet rs) throws SQLException {

        final List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            final Map<String, Object> row = mapResultSetRow(rs);

            if (row != null) {
                results.add(row);
            }
        }

        return results;
    }


    // Puts the ResultSet data into a map
    private static Map<String, Object> mapResultSetRow(final ResultSet rs) throws SQLException {
        final Map<String, Object> map = new HashMap<String, Object>();
        final ResultSetMetaData meta = rs.getMetaData();
        // Load ResultSet into map by column name
        final int numberOfColumns = meta.getColumnCount();
        for (int i = 1; i <= numberOfColumns; ++i) {
            final String name = meta.getColumnName(i);
            final Object value = rs.getObject(i);
            // place into map
            map.put(name, value);
        }
        return map;
    }


    /**
     * Create a DBMS Connection
     * 
     * @return a Connection
     */
    protected static Connection getConnection() {

        return DatabaseSetupTearDown.getConnectionContext().getConnection();
    }


    public static boolean executeSQL(final String sql) {
        logger.debug("execute sql : " + sql);
        Connection conn = null;
        Statement stmt = null;
        boolean rtn = false;
        try {
            // Get a connection
            conn = getConnection();

            stmt = conn.createStatement();

            // Execute query
            rtn = stmt.execute(sql);

        } catch (final Exception e) {
            logger.error(e.getMessage(), e);

            // need to fail the test there and then...
            Assert.fail("DB update [" + sql + "]\nfailed: " + e.getMessage());
        } finally {
            // Close all resource associated with this call
            close(conn);
        }
        return rtn;
    }


    public static void close(final InputStream scriptStream) {
        if (scriptStream != null) {

            try {

                scriptStream.close();

            } catch (final IOException e) {

                logger.warn("Failed to close script file", e);
            }
        }
    }


    public static void close(final Connection connection) {

        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException ex) {
                logger.warn("Failed to close sql connection.", ex);
            }
        }
    }

}
