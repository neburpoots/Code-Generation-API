Feature: Get User

  @getuser
  Scenario: A user is requested by UUID
    Given A valid UUID
    When A user is requested
    Then The user object is returned