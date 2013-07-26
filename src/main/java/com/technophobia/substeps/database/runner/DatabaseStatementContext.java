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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A context to contain a statement being operated on
 * 
 * @author Alan Raison
 */
public class DatabaseStatementContext {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseStatementContext.class);
    private PreparedStatement statement;
    private Connection connection;
    private int argumentIndex = 1;


    public void prepareStatement(final String sql) {
        if (this.connection == null) {
            this.connection = DatabaseSetupTearDown.getConnectionContext().getConnection();
        } else {
            closeStatement();
        }

        try {
            this.statement = this.connection.prepareStatement(sql);
        } catch (final SQLException e) {
            LOG.error(e.getMessage());
            throw new AssertionError(e);
        }
    }


    public PreparedStatement getStatement() {
        return this.statement;
    }


    public void addStringParameter(final String value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            this.statement.setString(this.argumentIndex++, value);
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set string parameter on statement");
        }
    }


    public void addIntegerParameter(final Integer value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.INTEGER);
            } else {
                this.statement.setInt(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set int parameter on statement");
        }
    }


    public void addBigDecimalParameter(final BigDecimal value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            this.statement.setBigDecimal(this.argumentIndex++, value);
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set big decimal parameter on statement");
        }
    }


    public void addBooleanParameter(final Boolean value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.BOOLEAN);
            } else {
                this.statement.setBoolean(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set boolean parameter on statement");
        }
    }


    public void addByteParameter(final Byte value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.TINYINT);
            } else {
                this.statement.setByte(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set byte parameter on statement");
        }
    }


    public void addBytesParameter(final byte[] value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            this.statement.setBytes(this.argumentIndex++, value);
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set bytes parameter on statement");
        }
    }


    public void addDateParameter(final Date value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.DATE);
            } else {
                this.statement.setDate(this.argumentIndex++, new java.sql.Date(value.getTime()));
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set date parameter on statement");
        }
    }


    public void addDoubleParameter(final Double value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.DOUBLE);
            } else {
                this.statement.setDouble(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set double parameter on statement");
        }
    }


    public void addFloatParameter(final Float value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.FLOAT);
            } else {
                this.statement.setFloat(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set float parameter on statement");
        }
    }


    public void addLongParameter(final Long value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.BIGINT);
            } else {
                this.statement.setLong(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set long parameter on statement");
        }
    }


    public void addShortParameter(final Short value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.SMALLINT);
            } else {
                this.statement.setShort(this.argumentIndex++, value);
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set short parameter on statement");
        }
    }


    public void addTimeParameter(final Date value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.TIME);
            } else {
                this.statement.setTime(this.argumentIndex++, new Time(value.getTime()));
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set time parameter on statement");
        }
    }


    public void addTimestampParameter(final Date value) {
        Assert.assertNotNull("Trying to add parameter to StatementContext before statement has been set",
                this.statement);

        try {
            if (value == null) {
                this.statement.setNull(this.argumentIndex++, Types.TIMESTAMP);
            } else {
                this.statement.setTimestamp(this.argumentIndex++, new Timestamp(value.getTime()));
            }
        } catch (final SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new AssertionError("Failed to set short parameter on statement");
        }
    }


    public void closeStatement() {
        this.argumentIndex = 1;

        if (this.statement != null) {
            LOG.debug("closing statement");

            try {
                this.statement.close();
            } catch (final SQLException e) {
                LOG.warn("Error closing database statement", e);
            } finally {
                this.statement = null;
            }
        }
    }


    public void closeConnection() {
        closeStatement();

        if (this.connection != null) {
            try {
                LOG.debug("closing connection");
                this.connection.close();
            } catch (final SQLException e) {
                LOG.warn(e.getMessage());
            } finally {
                this.connection = null;
            }
        }
    }
}
