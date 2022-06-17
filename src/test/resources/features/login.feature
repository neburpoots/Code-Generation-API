Feature: Login

  Scenario: As a customer, I want to be able to login
    Given the following login information
      | email                    | password   |
      | tim@student.inholland.nl | Secret123! |

    When the customer logs in
    Then their user details and a jwt token are returned