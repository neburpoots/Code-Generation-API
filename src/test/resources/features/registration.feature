Feature: Registration

  Scenario: As a customer, register myself as a new customer
    Given the following information
      | firstname        | lastname        | email             | password   |
      | exampleFirstname | exampleLastname | email@example.com | Secret123! |

    When the customer registers with the given information
    Then their user details are returned