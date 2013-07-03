package com.technophobia.substeps.database.runner;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.setupteardown.Annotations;

public class DatabaseSetupTearDown {

    private static final String DATABASE_EXECUTION_CONTEXT_KEY = DatabaseExecutionContext.class.getName();
    private static final String DATABASE_CONNECTION_CONTEXT_KEY = DatabaseConnectionContext.class.getName();
    private static final String DATABASE_STATEMENT_CONTEXT_KEY = DatabaseStatementContext.class.getName();

    private static final MutableSupplier<DatabaseExecutionContext> executionContextSupplier = new ExecutionContextSupplier<DatabaseExecutionContext>(Scope.SCENARIO, DATABASE_EXECUTION_CONTEXT_KEY);
    private static final MutableSupplier<DatabaseConnectionContext> connectioncontextSupplier = new ExecutionContextSupplier<DatabaseConnectionContext>(Scope.SUITE, DATABASE_CONNECTION_CONTEXT_KEY);
    private static final MutableSupplier<DatabaseStatementContext> statementContextSupplier = new ExecutionContextSupplier<DatabaseStatementContext>(Scope.SCENARIO, DATABASE_STATEMENT_CONTEXT_KEY);

    public static DatabaseExecutionContext getExecutionContext() {
        return executionContextSupplier.get();
    }

    public static DatabaseConnectionContext getConnectionContext() {
        return connectioncontextSupplier.get();
    }

    public static DatabaseStatementContext getStatementContext() {
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
