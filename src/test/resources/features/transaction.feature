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

    Scenario: Customer attempts to transfer money to strangers savings account
      Given the customer enters following transaction information
        |fromAccount        |toAccount          |amount |transaction_type |
        |NL01INHO0000000008 |NL01INHO0000000005 |100    |0                |
      When the employee makes the transaction
      Then a 401 response is returned together with an error message.

    Scenario: Customer request transactions endpoint
      Given the customer enters the following filter parameter as amountEqual "80"
      When the customer request the transaction
      Then transactions are returned, status code 200 and transactions amount is 80

    Scenario: Customer deposits money into his primary account
      Given the customer enters the following deposit information
        |fromAccount        |toAccount          |amount |transaction_type |
        |NL01INHO0000000008 |NL01INHO0000000008 |100    |2                |
      When the customer makes the transaction
      Then a 201 created response with the created object is returned and deposit is added on balance

    Scenario: Customer withdraws money from his primary account
      Given the customer enter the following withdrawal information
        |fromAccount        |toAccount          |amount |transaction_type |
        |NL01INHO0000000008 |NL01INHO0000000008 |500    |1                |
      When the customer makes the withdrawal
      Then a 201 created response with created object is returned and


