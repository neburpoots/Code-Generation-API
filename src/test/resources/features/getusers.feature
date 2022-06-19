Feature: user feature

  Scenario: An employee retrieves a list of users
    Given the pageNo of "0" and the pageSize of "2" for the getUsers endpoint

    When the user requests all the users with the given page info
    Then a list of users is returned