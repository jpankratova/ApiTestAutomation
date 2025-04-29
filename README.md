# API Test Automation Project

Welcome to the **API Test Automation** project!
This project demonstrates full CRUD (Create, Read, Update, Delete) API testing using **Java**, **RestAssured**, **WireMock**, and **Cucumber**.

It includes:

- ✅ Mocked API server (WireMock)
- ✅ Dynamic request payloads (using Faker)
- ✅ Positive and negative scenarios
- ✅ Localization testing (German / English)
- ✅ Full end-to-end dynamic user testing (Create ➔ Fetch ➔ Update ➔ Delete)

---

## Project Structure

```plaintext
src/test/java
|-- stepdefs/         (Cucumber Step Definitions)
|-- utils/            (Payload builders, helper classes)
|-- runners/          (Test runners)

src/test/resources
|-- features/         (Cucumber Feature Files)
|-- mappings/         (WireMock Mappings)
|-- __files/          (WireMock Response Templates)

pom.xml               (Maven Configuration)
```

---

## Technologies Used

- **Java 17**
- **RestAssured** (HTTP requests)
- **WireMock** (mock server)
- **Cucumber 7** (BDD test framework)
- **Maven** (build automation)
- **Faker** (dynamic random test data)

---

## How to Run the Tests

### 1. Clone the Repository

```bash
git clone <your-repository-link>
cd api-test-automation
```

### 2. Prerequisites

- Java 17 or higher
- Maven 3.8+

Make sure Java and Maven are installed and available from your terminal:

```bash
java -version
mvn -version
```

### 3. Run Tests with Maven

Run all tests:

```bash
mvn test -Dtest='*CucumberTestRunner'
```

✅ This will:

- Start a WireMock server
- Run Cucumber scenarios
- Send API requests (POST, GET, PUT, DELETE)
- Validate API responses

### 4. Run Specific Feature

You can run a specific feature (e.g., only user tests):

```bash
mvn clean test -Dcucumber.features=src/test/resources/features/user.feature
```

---

## Available API Endpoints (Mocked by WireMock)

| Method | Endpoint         | Description         |
| ------ | ---------------- | ------------------- |
| POST   | /user            | Create a user       |
| GET    | /user/{username} | Get user details    |
| PUT    | /user/{username} | Update user details |
| DELETE | /user/{username} | Delete user         |

**Localization:**

- `Accept-Language: de` returns German error messages.
- Default returns English errors.

---

## Example Scenarios

- Create a user successfully
- Create a user with missing fields (negative validation)
- Get existing user
- Get unknown user (404 error)
- Delete existing user
- Delete unknown user (404 error)
- Update existing user
- Update unknown user (404 error)
- 🔄 End-to-End dynamic scenario: Create ➔ Fetch ➔ Update ➔ Delete user with dynamically generated data

---


## Author

Developed by Jelena Pankratova

---

## License

This project is open source under the MIT License.

