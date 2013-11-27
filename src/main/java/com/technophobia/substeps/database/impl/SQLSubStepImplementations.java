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
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.database.runner.DatabaseExecutionContext;
import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import com.technophobia.substeps.model.parameter.IntegerConverter;

@SubSteps.StepImplementations(requiredInitialisationClasses = DatabaseSetupTearDown.class)
public class SQLSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(SQLSubStepImplementations.class);


    /**
     * Executes the given query putting the result in the execution context,
     * this can then be accessed by other steps such as
     * AssertQueryResultContains
     * 
     * @example ExecuteQuery {SELECT COLOUR FROM CARS}
     * @section Database
     * @param sql
     *            the sql to execute
     */
    @SubSteps.Step("ExecuteQuery \\{([^\\}]*)\\}")
    public void executeQuery(final String sql) {

        LOG.debug("Executing sql [{}]", sql);

        Database.execute(sql);

    }


    @SubSteps.Step("ExecuteUpdate \\{([^\\}]*)\\}")
    public void executeUpdate(final String sql) {

        LOG.debug("Executing sql update [{}]", sql);

        Database.update(sql);
    }


    /**
     * Executes the given sql script
     * 
     * @example ExecuteScript "/myScript.sql"
     * @section Database
     * @param script
     *            the sql script which should be on the classpath
     */
    @SubSteps.Step("ExecuteScript \"([^\"]*)\"")
    public void executeScript(final String script) {

        LOG.debug("Executing sql script [{}]", script);

        Connection connection = null;
        InputStream scriptStream = null;

        try {

            connection = DatabaseSetupTearDown.getConnectionContext().getConnection();
            final ScriptRunner scriptRunner = new ScriptRunner(connection);

            scriptStream = SQLSubStepImplementations.class.getResourceAsStream(script);

            Assert.assertNotNull("Unable to find script: " + script, scriptStream);

            final Reader scriptReader = new InputStreamReader(scriptStream);

            scriptRunner.runScript(scriptReader);

        } finally {

            Database.close(connection);
            Database.close(scriptStream);
        }

    }


    /**
     * Executes the supplied sql query and store the results in the default
     * stash
     * 
     * @example ExecuteQueryAndStashResults {select * from wherever where
     *          something="something-else"}
     * @section Database
     * @param sql
     *            the sql query to run
     */
    @Step("ExecuteQueryAndStashResults \\{([^\\}]*)\\}")
    public void executeQueryAndStashResults(final String sql) {

        final List<Map<String, Object>> results = Database.query(sql);

        getExecutionContext().stashResultSet(results);
    }


    /**
     * Executes the supplied sql query and store the results under the specified
     * name
     * 
     * @example ExecuteQuery {select * from wherever where
     *          something="something-else"} and stash results under key fred
     * @section Database
     * @param sql
     *            the sql query to run
     * @param key
     *            the key under which the results should be stored
     */
    @Step("ExecuteQuery \\{([^\\}]*)\\} and stash results under key ([^\"]*)")
    public void executeQueryAndStashResultsUnderKey(final String sql, final String key) {

        final List<Map<String, Object>> results = Database.query(sql);

        LOG.debug("query executed returning " + results.size() + " rows");

        getExecutionContext().stashResultSet(key, results);
    }


    /**
     * Checks the default stash for a row that has all of the supplied values
     * 
     * @example AssertResults contains a row with values [COLOUR="red",
     *          name="bob", age=26]
     * @section Database
     * @param rowDetails
     *            a comma separated list of column names and values. String
     *            values should be quoted, column names should not be quoted
     */
    @Step("AssertResults contains a row with values \\[([^\\]]*)\\]")
    public void checkDefaultResultsContainsARow(final String rowDetails) {

        checkResultsContainsARow(DatabaseExecutionContext.DEFAULT_RESULT_NAMESPACE, rowDetails);
    }


    /**
     * Checks the named stash for a row that has all of the supplied values
     * 
     * @param key
     *            the key under which the results to be checked can be found
     * @example AssertResults under my-stash contains a row with values
     *          [COLOUR="red", name="bob", age=26]
     * @section Database
     * @param rowDetails
     *            a comma separated list of column names and values. String
     *            values should be quoted, column names should not be quoted
     */

    @Step("AssertResults under ([^\"]*) contains a row with values \\[([^\\]]*)\\]")
    public void checkResultsContainsARow(final String key, final String rowDetails) {

        final Map<String, String> expectedRow = convertToMap(rowDetails);

        final List<Map<String, Object>> resultSet = getExecutionContext().getResultSet(key);

        // does the resultSet contain the values we're after ?

        boolean found = false;
        for (final Map<String, Object> resultRow : resultSet) {

            // compare resultRow with expectedRow
            // type conversion...?
            boolean foundInThisIteration = false;

            for (final Entry<String, String> entry : expectedRow.entrySet()) {

                final Object actualVal = resultRow.get(entry.getKey());
                if (actualVal != null) {

                    final String stringVal = actualVal.toString();
                    if (entry.getValue().equalsIgnoreCase(stringVal)) {

                        foundInThisIteration = true;
                    }
                } else if (entry.getValue().equalsIgnoreCase("null")) {
                    foundInThisIteration = true;
                }
                if (!foundInThisIteration) {
                    break;
                }
            }

            if (foundInThisIteration) {
                found = true;
                break;
            }
        }

        Assert.assertTrue("didn't find a row like: " + rowDetails, found);
    }


    /**
     * Convert a string of the form [type="submit",value="Search"] to a map,
     * minus [].
     * 
     * TODO this came from AbstractWebDriverSubStepImplementations - refactor
     * into somewhere core.
     * 
     * @param attributes
     * @return
     */
    private Map<String, String> convertToMap(final String attributes) {
        Map<String, String> attributeMap = null;

        // split the attributes up, will be received as a comma separated list
        // of name value pairs
        final String[] nvps = attributes.split(",");

        if (nvps != null) {
            for (final String nvp : nvps) {
                final String[] split = nvp.split("=");
                if (split != null && split.length == 2) {
                    if (attributeMap == null) {
                        attributeMap = new HashMap<String, String>();
                    }
                    attributeMap.put(split[0], split[1].replaceAll("\"", ""));
                }
            }
        }

        return attributeMap;
    }


    /**
     * Checks the size of results under the default stash
     * 
     * @example AssertResults are of size 53
     * @section Database
     * @param size
     *            the expected size of the results
     */
    @Step("AssertResults are of size ([\\d])")
    public void assertResultsAreOfSize(@StepParameter(converter = IntegerConverter.class) final Integer size) {
        assertResultsAreOfSize(DatabaseExecutionContext.DEFAULT_RESULT_NAMESPACE, size);
    }


    /**
     * Checks the size of results under a named stash
     * 
     * @param key
     *            the key under which the results to be checked can be found
     * @example AssertResults under my-stash are of size 53
     * @section Database
     * @param size
     *            the expected size of the results
     */
    @Step("AssertResults under ([^\"]*) are of size ([\\d])")
    public void assertResultsAreOfSize(final String key,
            @StepParameter(converter = IntegerConverter.class) final Integer size) {

        final List<Map<String, Object>> resultSet = getExecutionContext().getResultSet(key);

        if (size > 0) {
            Assert.assertNotNull("mismatch of expected result size, found None, expected: " + size, resultSet);
        }

        Assert.assertThat("mismatch of expected result size", Integer.valueOf(resultSet.size()), equalTo(size));
    }

}
