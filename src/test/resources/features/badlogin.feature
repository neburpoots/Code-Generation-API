#Feature: Bad login
#
#  Scenario: As a customer, I could fill in a bad email address
#    Given the following bad email address
#      | email                   | password  |
#      | tim@student.inholland.n | Secret123! |
#
#    When the customer logs in with the bad email
#    Then a resource not found error is returned
#
#  Scenario: As a customer, I could fill in a bad password
#    Given the following bad login password
#      | email                    | password   |
#      | tim@student.inholland.nl | Secret123  |
#
#    When the customer logs in with the bad password
#    Then an unauthorized error is returned
#
#  Scenario: As a customer, I could not fill in an email
#    Given the following bad login information with missing email
#      | password   |
#      | Secret123! |
#
#    When the customer logs in with the email missing
#    Then a bad request error is returned with "email missing" message
#
#  Scenario: As a customer, I could not fill in a password
#    Given the following bad login information with missing password
#      | email                    |
#      | tim@student.inholland.nl |
#
#    When the customer logs in with the password missing
#    Then a bad request error is returned with "password missing" message
