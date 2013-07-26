Feature: Execution of a database script

Background:
    SetupTest DB

Scenario: I should be able to execute a database script which is on the classpath
          ExecuteQuery {SELECT COLOUR FROM CARS}
          AssertResults contains a row with values [COLOUR="red"]

Scenario: I should be able to create rows in the database
          ExecuteUpdate {INSERT INTO CARS (MODEL, COLOUR) VALUES ('DeLorean', 'silver')}
          ExecuteQuery {SELECT COLOUR FROM CARS WHERE MODEL = 'DeLorean'}
          AssertResults contains a row with values [COLOUR="silver"]