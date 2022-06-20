Feature: account feature

  @getAccountsSuccessfully
  Scenario: A employee retrieves the accounts
    Given the following accounts
      |account_id       |balance|absoluteLimit |accountType |status|
      |NLINHO0000000003 |5000.00   |-340.00    |PRIMARY     |true  |
      |NLINHO0000000004 |1000.00   |0.00       |SAVINGS     |true  |

    When the user requests all the accounts
    Then all the accounts are returned

  @getAccountsSuccessfullyForCustomerWithUserId
  Scenario: A customer retrieves his own accounts with his user id as url parameter
    Given the customers own accounts
      |account_id       |balance   |absoluteLimit |accountType |status|
      |NLINHO0000000003 |5000.00   |-340.00       |PRIMARY     |true  |
      |NLINHO0000000004 |1000.00   |0.00          |SAVINGS     |true  |
    When the customer requests all the accounts with his user id as url parameter
    Then all the accounts are returned for the specific customer

  @getAccountsSuccessfullyWithAllFilters
  Scenario: A employee retrieves the accounts for a user with accountType PRIMARY and pagination
    Given the account that the employee will retrieve with the filters
      |account_id       |balance   |absoluteLimit |accountType |status|
      |NLINHO0000000003 |2000.00   |-500.00       |PRIMARY     |true  |
    When the employee requests the accounts for a user with accountType PRIMARY and pagination
    Then the accounts that matches all the filters is returned

  @getAccountsAsCustomerReturns403
  Scenario: A customer retrieves the accounts and receives a 403
    When a customer retrieves the accounts and receives a 403 and a message containing "You are not authorized to make this request.""
    Then a customer receives a 403 statuscode and receives a message

  @getAccountsNotLoggedInReturns403
  Scenario: A not logged in user retrieves the accounts and receives a 403
    When a not logged in user retrieves the accounts and receives a 403 and a message containing "Access Denied""
    Then a not logged in user receives a 403 statuscode and receives a message

  @getAccountsAsEmployeeWithNonExistentUserIdSteps
  Scenario: A employee retrieves the accounts for a user which does not exist
    When a employee retrieves the accounts with the id f3e7e0cb-6e21-40ff-878a-3ed56361bf6f
    Then a 404 should be returned with the error message "User with id: 'f3e7e0cb-6e21-40ff-878a-3ed56361bf6f' not found"

  @createAccountSuccessfully
  Scenario: A employee creates an account
    Given the account post dto for the account that will be created with email noaccount@student.inholland.nl
      |absoluteLimit |accountType|
      |-500.00       |PRIMARY|
    When a employee posts a new account with the given data
    Then it is in the database
    And it has an id

  @createAccountAsCustomerReturns403
  Scenario: A customer tries to create an account
    Given the valid account post dto with the email noaccount@student.inholland.nl
      |absoluteLimit |accountType|
      |-500.00       |PRIMARY|
    When a customer posts a new account with post dto
    Then the customer will receive a 403 with the error message "Forbidden"

  @createAccountForUserThatHas2AccountsReturns409
  Scenario: A employee tries to create an account for a user with two accounts
    Given the valid account post dto for a new account and the email ruben@student.inholland.nl
      |absoluteLimit |accountType|
      |-500.00       |PRIMARY|
    When a employee posts a new accounts with the given dto and uuid
    Then the customer will receive a 409 conflict with the error message "Customer already has a primary and savings account"

  @createAccountForUserWithWrongAbsoluteLimitReturns400
  Scenario: A employee tries to create an account with invalid absolute limit
    Given the invalid account post dto for a new account and the email ruben@student.inholland.nl
      |absoluteLimit |accountType|
      |-20000.00     |PRIMARY|
    When a employee posts the invalid post data with the uuid
    Then the customer will receive a 400 bad request with the error message "[moet groter dan -10000.01 zijn]"
    And the account has not been added to the database

  @editAccountSuccessfully
  Scenario: A employee edits the account of an user with valid credentials
    Given the valid account patch dto for the primary account of ruben@student.inholland.nl
      |absoluteLimit |status|
      |-5000.00      |false|
    When a employee patches the valid patch dto for an account with the iban
    Then the updated account is in the database
    And it has the updated properties

  @editAccountAsCustomerReturns403
  Scenario: A customer tries to edit his own account and receives a error
    Given the valid account patch dto for the savings account of ruben@student.inholland.nl
      |absoluteLimit |status|
      |-5000.00      |false|
    When a customer tries to update the account with the given dto
    Then a 403 should be returned and the following error message given "Access Denied"


  @editAccountWithBadDTOReturns400
  Scenario: A employee tries to edit an account to an invalid absolute limit
    Given the invalid account patch dto for the savings account of ruben@student.inholland.nl
      |absoluteLimit |status|
      |1000.00     |false|
    When a employee tries to update the account with the invalid patch dto
    Then a 400 should be returned and the following error message given: "[moet kleiner dan 0.01 zijn]"
    And the account has not been updated in the database

  @editAccountWithWrongHttpMethodReturns405
  Scenario: A employee tries to do a post request to the patch endpoint
    Given the valid dto for the wrong http request
      |absoluteLimit |status|
      |1000.00     |false|
    When a employee makes a post request to the patch endpoint
    Then a 405 method not allowed should be returned and the following error message given: "Request method 'POST' not supported"
    And the existing account has not been updated in the database