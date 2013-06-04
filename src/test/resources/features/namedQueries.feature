Feature: Running a named database script

Scenario: I can run the script called test-no-parameters which takes no parameters
    ExecuteNamedQuery "test-no-parameters"

Scenario: I can execute the update called insert-no-parameters and the database is updated
    ExecuteNamedUpdate "insert-no-parameters"
    ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'zaq'}
    AssertQueryResultContains column="COLOUR" value="pink"