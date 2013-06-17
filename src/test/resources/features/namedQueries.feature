Feature: Running a named database script

Scenario: I can run the script called test-no-parameters which takes no parameters
    ExecuteNamedQuery "select-no-parameters"
    AssertQueryResultContains column="VALUE" value="test-no-parameters"

Scenario: I can execute the update called insert-no-parameters and the database is updated
    ExecuteUpdate {CREATE TABLE insert_no_parameters (title VARCHAR(20), value VARCHAR(20))}
    ExecuteNamedUpdate "insert-no-parameters"
    ExecuteQuery {SELECT value FROM insert_no_parameters WHERE title = 'insert'}
    AssertQueryResultContains column="VALUE" value="no parameters"

Scenario: I can execute a named query with a String parameter
    ExecuteUpdate {CREATE TABLE select_with_parameters (title VARCHAR(20), value VARCHAR(20))}
    ExecuteUpdate {INSERT INTO select_with_parameters (title,value) VALUES ('select', 'with parameters')}
    FetchNamedQuery "select-with-parameters"
    AddStringParameter value="select"
    ExecuteQuery
    AssertQueryResultContains column="VALUE" value="with parameters"

Scenario: I can execute an insert named query with parameters
    ExecuteUpdate {CREATE TABLE insert_with_parameters (title VARCHAR(20), value VARCHAR(30))}
    FetchNamedQuery "insert-with-parameters"
    AddStringParameter value="test"
    AddStringParameter value="insert-with-parameters"
    ExecuteUpdate
    ExecuteQuery {SELECT VALUE FROM insert_with_parameters WHERE title = 'test'}
    AssertQueryResultContains column="VALUE" value="insert-with-parameters"

Scenario: I can run multiple named queries in one scenario
    ExecuteUpdate {CREATE TABLE types (s VARCHAR(10), i INTEGER, l BIGINT, d DOUBLE, b BOOLEAN)}
    FetchNamedQuery "insert-string"
    AddIntegerParameter value=4
    ExecuteUpdate
    FetchNamedQuery "insert-boolean"
    AddBooleanParameter value=true
    ExecuteUpdate
    ExecuteQuery {SELECT * FROM types WHERE s = '4'}
    AssertQueryResultContains column="S" value="4"
    FetchNamedQuery "insert-integer"
    AddIntegerParameter value=10
    ExecuteUpdate
    ExecuteQuery {SELECT * FROM types WHERE S = '4'}
    AssertQueryResultContains column="S" value="4"
