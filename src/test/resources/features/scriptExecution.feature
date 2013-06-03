Feature: Execution of a database script

Scenario: I should be able to execute a database script which is on the classpath
          ExecuteScript "/myScript.sql"
          ExecuteQuery {SELECT COLOUR FROM CARS}
          AssertQueryResultContains column="COLOUR" value="red"

Scenario: I should be able to create rows in the database
          ExecuteScript "/myScript.sql"
          ExecuteUpdate {INSERT INTO CARS (MODEL, COLOUR) VALUES ('DeLorean', 'silver')}
          ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'DeLorean'}
          AssertQueryResultContains column="COLOUR" value="silver"