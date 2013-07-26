# this depends on the database being built and therefore tests aren't independant

Feature: Running a named database script

Background:
    SetupTest DB

Scenario: I can run the script called test-no-parameters which takes no parameters
    ExecuteNamedQuery "test-no-parameters"
    AssertResults contains a row with values [COLOUR="red"]
    
Scenario: I can execute the update called insert-no-parameters and the database is updated
    ExecuteNamedUpdate "insert-no-parameters"
    ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'zaq'}
    AssertResults contains a row with values [COLOUR="pink"]

Scenario: I can execute a named query with a String parameter
    FetchNamedQuery "test-named-with-parameters"
    AddStringParameter value="Fiesta"
    ExecuteQuery
    AssertResults contains a row with values [COLOUR="red"]

Scenario: I can execute an insert named query with parameters
    FetchNamedQuery "test-insert-with-parameters"
    AddStringParameter value="db7"
    AddStringParameter value="black"
    ExecuteUpdate
    ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'db7'}
    AssertResults contains a row with values [COLOUR="black"]

Scenario: The query named test-database-type is resolved with the h2 version
    ExecuteNamedQuery "test-database-type"
    AssertResults contains a row with values [NUM=2]