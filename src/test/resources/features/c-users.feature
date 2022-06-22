Feature: Users

  #  Login scenario's
  Scenario: As a customer, I want to be able to login
    Given the following login information
      | email                    | password   |
      | tim@student.inholland.nl | Secret123! |

    When the customer logs in
    Then their user details and a jwt token are returned

  #  Login bad scenario's
  Scenario: As a customer, I could fill in a bad email address
    Given the following bad email address
      | email                   | password   |
      | tim@student.inholland.n | Secret123! |

    When the customer logs in with the bad email
    Then a resource not found error is returned

  Scenario: As a customer, I could fill in a bad password
    Given the following bad login password
      | email                    | password  |
      | tim@student.inholland.nl | Secret123 |

    When the customer logs in with the bad password
    Then an unauthorized error is returned

  Scenario: As a customer, I could not fill in an email
    Given the following bad login information with missing email
      | password   |
      | Secret123! |

    When the customer logs in with the email missing
    Then a bad request error is returned with "email missing" message

  Scenario: As a customer, I could not fill in a password
    Given the following bad login information with missing password
      | email                    |
      | tim@student.inholland.nl |

    When the customer logs in with the password missing
    Then a bad request error is returned with "password missing" message

  #  Registration scenario's
  Scenario: As a customer, register myself as a new customer
    Given the following register information
      | firstname        | lastname        | email             | password   |
      | exampleFirstname | exampleLastname | email@example.com | Secret123! |

    When the customer registers with the given information
    Then their user details are returned

  #  Registration bad scenario's
  Scenario: As a customer, register myself as a new customer with a bad email address
    Given the following register information with bad email
      | firstname        | lastname        | email            | password   |
      | exampleFirstname | exampleLastname | emailexample.com | Secret123! |

    When the customer registers with the given information with bad email
    Then a bad request error is returned with "Email address is invalid" message

  Scenario: As a customer, register myself as a new customer with a bad password
    Given the following register information with bad password
      | firstname        | lastname        | email              | password  |
      | exampleFirstname | exampleLastname | email2@example.com | Secret123 |

    When the customer registers with the given information with bad password
    Then a bad request error is returned with "Invalid password" message

  #  Get User scenario's
  @getUser
  Scenario: A user is requested by UUID
    Given A valid UUID
    When A user is requested
    Then The user object is returned

  #  Get User bad scenario's
  Scenario: A user is requested by with a not existing UUID
    Given A not existing UUID of "123e4567-e89b-12d3-a456-426614174000"
    When A user is requested with the not existing UUID
    Then A resource not found error is returned with "User with id: '123e4567-e89b-12d3-a456-426614174000' not found" message

  Scenario: A user is requested by with a bad UUID
    Given A bad UUID of "404"
    When A user is requested with the bad UUID
    Then A bad request error is returned with "Invalid UUID string: 404' not found" message

  # Get Users scenario's
  Scenario: An employee retrieves a list of users
    Given the pageNo of "0" and the pageSize of "2" for the getUsers endpoint

    When the user requests all the users with the given page info
    Then a list of users is returned

  # Get Users bad scenario's
  Scenario: An employee retrieves a list of users with the page number missing
    Given the pageNo missing and the pageSize of "2" for the getUsers endpoint

    When the user requests all the users with the given page info and the page number missing
    Then a bad request error is returned with "Required Integer parameter 'pageNo' is not present" message

  Scenario: An employee retrieves a list of users with the page size missing
    Given the pageNo of "0" and the pageSize missing for the getUsers endpoint

    When the user requests all the users with the given page info and the page size missing
    Then a bad request error is returned with "Required Integer parameter 'pageSize' is not present" message

  Scenario: A list of users is requested while not logged in
    Given the pageNo of "0" and the pageSize of "2" for the getUsers endpoint while not logged in

    When the user requests all the users with the given page info while not being logged in
    Then a forbidden error is returned with "Access Denied" message
