Feature: Execution of a database script

Scenario: I should be able to execute a database script which is on the classpath
          ExecuteScript "/myScript.sql"
          ExecuteQuery {SELECT COLOUR FROM CARS}
          AssertQueryResultContains column="COLOUR" value="red"
