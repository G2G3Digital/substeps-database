package com.technophobia.substeps.database.impl;

import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.SubSteps;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SubSteps.StepImplementations
public class SQLSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(SQLSubStepImplementations.class);

    @SubSteps.Step("ExecuteQuery \\{([^\\}]*)\\}")
    public void executeQuery(final String sql) {

        LOG.debug("Executing sql [{}]", sql);

        //TODO: no good, as connection is now closed - need to convert data.
        ResultSet resultSet = execute(sql);

    }

    private ResultSet execute(String sql) {

        Connection connection = null;
        try {

            connection = DatabaseSetupTearDown.getConnectionContext().getConnection();
            final PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeQuery();

        } catch (SQLException ex) {
            throw new AssertionError("Executing sql [" + sql + "] failed.", ex);

        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOG.warn("Failed to close sql connection.", ex);
                }
            }
        }
    }

}
