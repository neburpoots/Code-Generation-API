Feature: user feature

  Scenario: An employee retrieves a list of users
    Given the following users
      | user_id                              | firstname | lastname | email                      | password                                                     |
      | c6a2b719-4868-4af6-a668-2b8af0f9be8f | Bank      | Account  | bankaccount@bankaccount.nl | Secret123! |

    When the user requests all the users
    Then all the users are returned