package com.technophobia.substeps.database.impl.runner;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.setupteardown.Annotations;

public class DatabaseSetupTearDown {

    private static final String DATABASE_EXECUTION_CONTEXT_KEY = DatabaseExecutionContext.class.getName();
    private static final String DATABASE_CONNECTION_CONTEXT_KEY = DatabaseConnectionContext.class.getName();

    private static final MutableSupplier<DatabaseExecutionContext> executionContextSupplier = new ExecutionContextSupplier<DatabaseExecutionContext>(Scope.SCENARIO, DATABASE_EXECUTION_CONTEXT_KEY);
    private static final MutableSupplier<DatabaseConnectionContext> connectioncontextSupplier = new ExecutionContextSupplier<DatabaseConnectionContext>(Scope.SUITE, DATABASE_CONNECTION_CONTEXT_KEY);

    public static DatabaseExecutionContext getExecutionContext() {
        return executionContextSupplier.get();
    }

    public static DatabaseConnectionContext getConnectionContext() {
        return connectioncontextSupplier.get();
    }

    @Annotations.BeforeAllFeatures
    public final void initialiseConnectionPool() {
        final DatabaseConnectionContext connectionContext = new C3PODatabaseConnectionContext();
        connectioncontextSupplier.set(connectionContext);
    }

    @Annotations.BeforeEveryScenario
    public final void initialiseContext() {
        final DatabaseExecutionContext executionContext = new DatabaseExecutionContext();
        executionContextSupplier.set(executionContext);
    }

    @Annotations.AfterAllFeatures
    public final void destroyConnectionPool() {
        getConnectionContext().destroy();
    }

}
