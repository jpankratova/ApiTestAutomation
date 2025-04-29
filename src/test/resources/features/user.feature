Feature: User API Tests

  Scenario: Create a user successfully
    Given the user payload is loaded
    When I send a POST request to "/user"
    Then the response status code should be 200
    And the response should confirm user was created

  Scenario: Get user by username
    When I send a GET request to "/user/testuser"
    Then the response status code should be 200
    And the response should contain username "testuser"

  Scenario: User not found in German
    When I send a GET request to "/user/unknownuser" with header "Accept-Language" = "de"
    Then the response status code should be 404
    And the response should contain error message "Benutzer nicht gefunden"

  Scenario: Get unknown user returns 404
    When I send a GET request to "/user/unknownuser"
    Then the response status code should be 404
    And the response should contain error message "User not found"

  Scenario: Update existing user successfully
    When I send a PUT request to "/user/testuser"
    Then the response status code should be 200
    And the response message should be "User updated successfully"

  Scenario: Update unknown user returns 404
    When I send a PUT request to "/user/unknownuser"
    Then the response status code should be 404
    And the response should contain error message "User not found"


  Scenario: Delete existing user successfully
    When I send a DELETE request to "/user/testuser"
    Then the response status code should be 200
    And the response message should be "User deleted successfully"

  Scenario: Delete unknown user returns 404
    When I send a DELETE request to "/user/unknownuser"
    Then the response status code should be 404
    And the response should contain error message "User not found"


# Example with dynamic data
  Scenario: Create, fetch, update, and delete user
    Given a user payload is generated dynamically
    When I send a POST request to "/user"
    Then the response status code should be 200
    And the response should confirm user was created

    When I send a GET request to the created user
    Then the response status code should be 200
    And the response should contain username

    When I send a PUT request to the created user
    Then the response status code should be 200
    And the response message should be "User updated successfully"

    When I send a DELETE request to the created user
    Then the response status code should be 200
    And the response message should be "User deleted successfully"


  Scenario Outline: Create user with missing fields
    Given a user with the following data
      | field     | value          |
      | username  | <username>     |
      | firstName | <firstName>    |
      | lastName  | <lastName>     |
      | email     | <email>        |
      | password  | <password>     |
    When I send a POST request to "/user"
    Then the response status code should be 400
    And the response should contain error message "<errorMessage>"

    Examples:
      | username   | firstName | lastName | email           | password | errorMessage                |
      | test1      |           | Doe      | test@mail.com   | secret   | First Name field is empty   |
      | test2      | John      |          | test@mail.com   | secret   | Last Name field is empty    |
      | test3      | John      | Doe      |                 | secret   | Email field is empty        |
      | test4      | John      | Doe      | test@mail.com   |          | Password field is empty     |


