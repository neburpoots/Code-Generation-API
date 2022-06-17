Feature: Bad registration

  Scenario: As a customer, register myself as a new customer with a bad email address
    Given the following register information with bad email
      | firstname        | lastname        | email            | password   |
      | exampleFirstname | exampleLastname | emailexample.com | Secret123! |

    When the customer registers with the given information with bad email
    Then a bad request error is returned with "Email address is invalid" message

  Scenario: As a customer, register myself as a new customer with a bad password
    Given the following register information with bad password
      | firstname        | lastname        | email            | password   |
      | exampleFirstname | exampleLastname | email@example.com | Secret123 |

    When the customer registers with the given information with bad password
    Then a bad request error is returned with "Invalid password" message