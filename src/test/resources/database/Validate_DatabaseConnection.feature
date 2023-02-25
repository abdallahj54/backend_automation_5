Feature: Validate database connection

  @db
  Scenario Outline: Validate the minimum salary
    Given user is able to connect to database
    When user sends "<query>" to database
    Then Validate the <salary>

    Examples: Database Query
      | query                             | salary |
      | select min(salary) from employees | 2100   |