Feature: Running a named database script

Scenario: I can run the script called test-no-parameters which takes no parameters
    ExecuteNamedQuery "test-no-parameters"
    AssertQueryResultContains column="COLOUR" value="red"

Scenario: I can execute the update called insert-no-parameters and the database is updated
    ExecuteNamedUpdate "insert-no-parameters"
    ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'zaq'}
    AssertQueryResultContains column="COLOUR" value="pink"

Scenario: I can execute a named query with a String parameter
    FetchNamedQuery "test-named-with-parameters"
    AddStringParameter value="Fiesta"
    ExecuteQuery
    AssertQueryResultContains column="COLOUR" value="red"

Scenario: I can execute an insert named query with parameters
    FetchNamedQuery "test-insert-with-parameters"
    AddStringParameter value="db7"
    AddStringParameter value="black"
    ExecuteUpdate
    ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'db7'}
    AssertQueryResultContains column="COLOUR" value="black"

Scenario: I can run multiple named queries in one scenario
    ExecuteUpdate {CREATE TABLE types (s VARCHAR(10), i INTEGER, l BIGINT, d DOUBLE, b BOOLEAN)}
    FetchNamedQuery "test-insert-string"
    AddIntegerParameter value=4
    ExecuteUpdate
    FetchNamedQuery "test-insert-boolean"
    AddBooleanParameter value=true
    ExecuteUpdate
    ExecuteQuery {SELECT * FROM types WHERE s = '4'}
    AssertQueryResultContains column="S" value="4"
    FetchNamedQuery "test-insert-integer"
    AddIntegerParameter value=10
    ExecuteUpdate
    ExecuteQuery {SELECT * FROM types WHERE S = '4'}
    AssertQueryResultContains column="S" value="4"
