#Feature: transaction feature
#
#  Scenario: A employee gets all transactions
#    Given the following transaction
##  {
##  "toAccount": "NL01INHO0000000003",
##  "fromAccount": "NL01INHO0000000004",
##  "timestamp": "2022-06-07T21:40:33.319",
##  "date": "2022-06-07",
##  "amount": 30.00,
##  "type": 1,
##  "transaction_type": "withdrawal"
##  }
#      |toAccount          |fromAccount        |type |amount
#      |NL01INHO0000000004 |NL01INHO0000000003 |1    |30.00
#
#    When the employee requests all transactions
#    Then transactions are returned