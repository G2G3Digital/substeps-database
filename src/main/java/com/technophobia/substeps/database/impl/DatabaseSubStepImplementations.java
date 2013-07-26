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

import com.technophobia.substeps.database.runner.DatabaseSetupTearDown;
import com.technophobia.substeps.model.SubSteps;

@SubSteps.StepImplementations(requiredInitialisationClasses = DatabaseSetupTearDown.class)
@SubSteps.AdditionalStepImplementations({SQLSubStepImplementations.class,
        NamedSqlSubStepImplementations.class})
public class DatabaseSubStepImplementations {

    //NoOP

}
