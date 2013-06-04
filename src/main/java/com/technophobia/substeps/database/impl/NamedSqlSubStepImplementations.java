package com.technophobia.substeps.database.impl;

import com.technophobia.substeps.database.runner.DatabaseSubstepsConfiguration;
import com.technophobia.substeps.model.Configuration;
import com.technophobia.substeps.model.SubSteps;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Run named queries from a properties file
 */
public class NamedSqlSubStepImplementations extends SQLSubStepImplementations {
    private static final Logger LOG = LoggerFactory.getLogger(NamedSqlSubStepImplementations.class);

    protected Properties properties = new Properties();

    public NamedSqlSubStepImplementations() {

        final String queryFileName = DatabaseSubstepsConfiguration.getNamedQueryPropertyFile();

        if (queryFileName != null) {

            LOG.debug("Loading queries from file {}", queryFileName);

            final InputStream queryFileStream = NamedSqlSubStepImplementations.class.getResourceAsStream(queryFileName);

            if (queryFileStream != null) {
                try {
                    properties.load(queryFileStream);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }
    }

    @SubSteps.Step("ExecuteNamedQuery \"([^\"]*)\"")
    public void executeNamedQuery(final String name) {

        String sql = properties.getProperty(name);

        Assert.assertNotNull("No query found with name " + name, sql);

        LOG.debug("Running query {} ({})", name, sql);

        executeQuery(sql);
    }

}
