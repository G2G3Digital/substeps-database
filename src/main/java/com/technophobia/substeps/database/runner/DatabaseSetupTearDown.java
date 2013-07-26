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

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.setupteardown.Annotations;

public class DatabaseSetupTearDown {

    private static final String DATABASE_EXECUTION_CONTEXT_KEY = DatabaseExecutionContext.class.getName();
    private static final String DATABASE_CONNECTION_CONTEXT_KEY = DatabaseConnectionContext.class.getName();
    private static final String DATABASE_STATEMENT_CONTEXT_KEY = DatabaseStatementContext.class.getName();

    private static final MutableSupplier<DatabaseExecutionContext> executionContextSupplier = new ExecutionContextSupplier<DatabaseExecutionContext>(
            Scope.SCENARIO, DATABASE_EXECUTION_CONTEXT_KEY);

    private static final MutableSupplier<DatabaseConnectionContext> connectioncontextSupplier = new ExecutionContextSupplier<DatabaseConnectionContext>(
            Scope.SUITE, DATABASE_CONNECTION_CONTEXT_KEY);

    private static final MutableSupplier<DatabaseStatementContext> statementContextSupplier = new ExecutionContextSupplier<DatabaseStatementContext>(
            Scope.SCENARIO, DATABASE_STATEMENT_CONTEXT_KEY);


    public static final DatabaseExecutionContext getExecutionContext() {
        return executionContextSupplier.get();
    }


    public static final DatabaseConnectionContext getConnectionContext() {
        return connectioncontextSupplier.get();
    }


    public static final DatabaseStatementContext getStatementContext() {
        return statementContextSupplier.get();
    }


    @Annotations.BeforeAllFeatures
    public final void initialiseConnectionPool() {
        final DatabaseConnectionContext connectionContext = new C3P0DatabaseConnectionContext();
        connectioncontextSupplier.set(connectionContext);
    }


    @Annotations.BeforeEveryScenario
    public final void initialiseContext() {
        final DatabaseExecutionContext executionContext = new DatabaseExecutionContext();
        final DatabaseStatementContext statementContext = new DatabaseStatementContext();
        executionContextSupplier.set(executionContext);
        statementContextSupplier.set(statementContext);
    }


    @Annotations.AfterEveryScenario
    public final void destroyOpenConnection() {
        getStatementContext().closeConnection();
    }


    @Annotations.AfterAllFeatures
    public final void destroyConnectionPool() {
        getConnectionContext().destroy();
    }

}
