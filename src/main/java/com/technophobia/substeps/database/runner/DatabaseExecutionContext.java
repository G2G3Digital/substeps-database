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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.database.impl.Database;

public class DatabaseExecutionContext {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseExecutionContext.class);

    public static final String EXECUTION_CONTEXT_KEY = "_db_context_key";

    public static final String DEFAULT_RESULT_NAMESPACE = "_default_results_namespace";

    private Map<String, List<Map<String, Object>>> mapOfMultipleResults = null;


    public void clear() {

        this.mapOfMultipleResults = null;
    }


    private List<Map<String, Object>> getDefaultStash() {

        Assert.assertNotNull("getting default stash when not initialised", this.mapOfMultipleResults);

        final List<Map<String, Object>> results = this.mapOfMultipleResults.get(DEFAULT_RESULT_NAMESPACE);

        Assert.assertNotNull("no default stash", results);

        return results;
    }


    /**
     * @param rs
     */
    public void stashResultSet(final List<Map<String, Object>> rs) {

        if (this.mapOfMultipleResults == null) {
            this.mapOfMultipleResults = new HashMap<String, List<Map<String, Object>>>();
        }

        stashResultSetInternal(DEFAULT_RESULT_NAMESPACE, rs);
    }


    /**
     * @param rs
     */
    public void stashResultSet(final String key, final List<Map<String, Object>> rs) {

        if (DEFAULT_RESULT_NAMESPACE.equals(key)) {

            throw new IllegalArgumentException("key cannot be the same as the default: " + DEFAULT_RESULT_NAMESPACE
                    + ", please use a different key name");
        }

        stashResultSetInternal(key, rs);
    }


    private void stashResultSetInternal(final String key, final List<Map<String, Object>> rs) {

        if (this.mapOfMultipleResults == null) {
            this.mapOfMultipleResults = new HashMap<String, List<Map<String, Object>>>();
        }

        if (this.mapOfMultipleResults.containsKey(key)) {

            logger.warn("stashing new query results over the top of existing results...");
        }

        this.mapOfMultipleResults.put(key, rs);
    }


    /**
     * 
     */
    public List<Map<String, Object>> getResultSet() {

        return getResultSet(DEFAULT_RESULT_NAMESPACE);
    }


    public List<Map<String, Object>> getResultSet(final String key) {

        List<Map<String, Object>> rtn = null;

        if (this.mapOfMultipleResults != null) {
            rtn = this.mapOfMultipleResults.get(key);
        }

        return rtn;
    }


    public Set<String> getColumns() {

        final List<Map<String, Object>> defaultStash = getDefaultStash();

        Assert.assertFalse("expecting some results in the default stash", defaultStash.isEmpty());
        // return the columns from the first result
        return defaultStash.get(0).keySet();
    }


    public List<String> getResultsForColumn(final String columnName) {

        List<String> rtn = null;

        final List<Map<String, Object>> defaultStash = getDefaultStash();

        for (final Map<String, Object> row : defaultStash) {

            final Object val = row.get(columnName);

            if (rtn == null) {
                rtn = new ArrayList<String>();
            }
            rtn.add((String) val);
        }
        return rtn;
    }


    public void setQueryResult(final ResultSet rs) throws SQLException {

        final List<Map<String, Object>> resultsMap = Database.buildResultsMap(rs);

        stashResultSet(resultsMap);
    }
}
