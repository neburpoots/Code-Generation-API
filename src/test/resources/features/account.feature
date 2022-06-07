Feature: account feature

  Scenario: A employee retrieves the accounts
    Given the following accounts
      |account_id       |balance|absoluteLimit |accountType |status|
      |NLINHO0000000003 |5000.00   |-340.00    |PRIMARY     |true  |
      |NLINHO0000000004 |1000.00   |0.00       |SAVINGS     |true  |

    When the user requests all the accounts
    Then all the accounts are returned