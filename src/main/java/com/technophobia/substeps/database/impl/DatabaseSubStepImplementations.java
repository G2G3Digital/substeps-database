package com.technophobia.substeps.database.impl;

import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.SubSteps;

@SubSteps.StepImplementations(requiredInitialisationClasses = DatabaseSetupTearDown.class)
@SubSteps.AdditionalStepImplementations({SQLSubStepImplementations.class,
        NamedSqlSubStepImplementations.class})
public class DatabaseSubStepImplementations {

    //NoOP

}
