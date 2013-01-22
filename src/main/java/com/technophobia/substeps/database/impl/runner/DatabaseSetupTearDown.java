package com.technophobia.substeps.database.impl.runner;

import com.technophobia.substeps.runner.setupteardown.Annotations;

public class DatabaseSetupTearDown {

    @Annotations.BeforeEveryScenario
    public final void initialiseContext() {
        EmailServerContext emailServerContext = new EmailServerContext();
        emailServerContextSupplier.set(emailServerContext);
        emailServerContext.start();

        EmailExecutionContext emailExecutionContext = new EmailExecutionContext();
        emailExecutionContextSupplier.set(emailExecutionContext);
    }

    @Annotations.AfterEveryScenario
    public final void shutdownContext() {
        final EmailServerContext emailServerContext = emailServerContextSupplier.get();
        if (emailServerContext != null) {
            emailServerContext.stop();
        }
    }

}
