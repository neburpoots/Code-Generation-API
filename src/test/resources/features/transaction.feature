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

    Scenario: Customer attempts to go over his daily limit
      Given the customer executes the given transaction 6 times
        |fromAccount        |toAccount          |amount |transaction_type |
        |NL01INHO0000000008 |NL01INHO0000000009 |500    |0                |
      When the customer tries making the sixth transaction it will fail
      Then a 400 bad request is returned

    Scenario: Making sure the received transactions have timestamp, to and from account and amount
      Given the customer wants to receive the transactions
      When the customer has received the transactions
      Then all transactions have an amount timestamp to and from account

    Scenario: Customer enters invalid type for transaction eg withdrawal and two different ibans
      Given the customer enters the wrong number for transaction type
        |fromAccount        |toAccount          |amount |transaction_type |
        |NL01INHO0000000008 |NL01INHO0000000009 |70     |2                |
      When the customer tries to create the transaction with the invalid type
      Then a 400 bad request is returned telling the customer what was wrong

      Scenario: a account goes over his absolute limit
        Given the employee wants to make the following transaction which goes under the absolute limit
          |fromAccount        |toAccount          |amount |transaction_type |
          |NL01INHO0000000006 |NL01INHO0000000007 |500     |0                |
        When the employee makes the transaction that will go under the limit
        Then the employee will reaceive a 400 status and a message telling him his limit is reached

        Scenario: A customer wants to look up an transaction by the id
          Given the customer enters a matching id
          When the customer request the getTransactionById endpoint
          Then the customer will receive the transaction object and 200 status

          Scenario: A customer request a strangers transaction by entering that transaction id
            Given the customer enters the matching id of a strangers transaction id
            When the customer request the endpoint
            Then the customer will receive an error message and a 404 not found error
