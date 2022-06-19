Feature: Transactions
  Scenario: An employee retrieves a list of filtered transactions
  Given the fromIban of "NL01INHO0000000004" filter parameter is applied
  When a employee requests the transaction endpoint
  Then a list of transactions is returned

  Scenario: An customer tries to make transaction to strangers savings account
    Given the following create transaction
      |fromAccount        |toAccount          |amount |transaction_type |
      |NL01INHO0000000008 |NL01INHO0000000005 |50     |0                |
    When a customer tries to make the transaction
    Then a 401 unauthorized exception is thrown


  Scenario: A customer attempts to make transaction over transaction limit
    Given the following transaction information
      |fromAccount        |toAccount          |amount |transaction_type |
      |NL01INHO0000000008 |NL01INHO0000000004 |1500   |0                |
    When the customer tries to make this transaction
    Then he will receive a 400 bad request exception with an error message

  Scenario: Employee performs transaction on behalf of another user
    Given the employee enters the following transaction
      |fromAccount        |toAccount          |amount |transaction_type |
      |NL01INHO0000000008 |NL01INHO0000000006 |100    |0                |
    When the employee creates the transaction
    Then a 201 created response is returned together with the just created object