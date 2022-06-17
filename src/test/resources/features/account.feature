Feature: account feature

  Scenario: A employee retrieves the accounts
    Given the following accounts
      |account_id       |balance|absoluteLimit |accountType |status|
      |NLINHO0000000003 |5000.00   |-340.00    |PRIMARY     |true  |
      |NLINHO0000000004 |1000.00   |0.00       |SAVINGS     |true  |

    When the user requests all the accounts
    Then all the accounts are returned

  Scenario: A employee creates an account
    When a employee posts a new account with absolute limit 0 for customer noaccount@student.inholland.nl
    Then it is in the database
    And it has an id