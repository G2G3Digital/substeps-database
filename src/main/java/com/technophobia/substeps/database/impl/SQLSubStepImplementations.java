package com.technophobia.substeps.database.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.SubSteps;

@SubSteps.StepImplementations
public class SQLSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(SQLSubStepImplementations.class);

    /**
     * Executes the given query putting the result in the execution context, this
     * can then be accessed by other steps such as AssertQueryResultContains
     * 
     * @example ExecuteQuery {SELECT COLOUR FROM CARS}
     * @section Executions
     * @param sql
     *            the sql to execute
     */
    @SubSteps.Step("ExecuteQuery \\{([^\\}]*)\\}")
    public void executeQuery(final String sql) {

        LOG.debug("Executing sql [{}]", sql);

        execute(sql);

    }
    
    /**
     * Asserts the query results in the context have the given column
     * containing an entry with the given value
     * 
     * @example AssertQueryResultContains column="COLOUR" value="red"
     * @section Assertions
     * @param column
     *            the column name
     * @param value
     *            the expected value
     */
    @SubSteps.Step("AssertQueryResultContains column=\"([^\"]*)\" value=\"([^\"]*)\"")
    public void assertQueryResult(final String column, final String value) {
    	
        LOG.debug("Asserting query result, column = " + column + ", value = " + value);
    	
        List<String> resultsForColumn = DatabaseSetupTearDown.getExecutionContext().getResultsForColumn(column);

        Assert.assertNotNull("No results found for column: " + column + ", columns found were " + DatabaseSetupTearDown.getExecutionContext().getColumns(), resultsForColumn);

        Assert.assertTrue(column + " did not contain " + value, resultsForColumn.contains(value));
    }
    
    /**
     * Executes the given sql script
     * 
     * @example ExecuteScript "/myScript.sql"
     * @section Executions
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
        	ScriptRunner scriptRunner = new ScriptRunner(connection);

        	scriptStream = SQLSubStepImplementations.class.getResourceAsStream(script);

        	Assert.assertNotNull("Unable to find script: " + script, scriptStream);
        	
        	Reader scriptReader = new InputStreamReader(scriptStream);

        	scriptRunner.runScript(scriptReader);
        	
    	} finally {
    		
    		close(connection);
    		close(scriptStream);
    	}
    
    }


    private void execute(String sql) {

        Connection connection = null;
        try {

            connection = DatabaseSetupTearDown.getConnectionContext().getConnection();
            final PreparedStatement statement = connection.prepareStatement(sql);
            DatabaseSetupTearDown.getExecutionContext().setQueryResult(statement.executeQuery());

        } catch (SQLException ex) {
        	ex.printStackTrace();
            throw new AssertionError("Executing sql [" + sql + "] failed: " + ex.getMessage(), ex);

        } finally {

        	close(connection);
        }
    }

	private void close(InputStream scriptStream) {
		if(scriptStream != null) {
			
			try {
				
				scriptStream.close();
			
			} catch (IOException e) {

				LOG.warn("Failed to close script file", e);
			}
		}
	}
    
    private void close(Connection connection) {
    	
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                LOG.warn("Failed to close sql connection.", ex);
            }
        }
    }
}
