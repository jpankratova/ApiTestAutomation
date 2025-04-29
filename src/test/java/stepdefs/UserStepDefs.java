package stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.UserPayloadBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserStepDefs {

    private Response response;
    private String requestBody;

    private String username;

    @Before
    public void setup() throws Exception {
        RestAssured.baseURI = "http://localhost:8089";
    }

    @Given("the user payload is loaded")
    public void the_user_payload_is_loaded() throws Exception {
        requestBody = new String(Files.readAllBytes(
                Paths.get("src/test/resources/__files/create-user-request.json")));

        // Extract the username to use in assertions later
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(requestBody, new TypeReference<>() {});
        username = (String) map.get("username");
    }

    @Given("a user payload is generated dynamically")
    public void a_user_payload_is_generated_dynamically() throws JsonProcessingException {
        Faker faker = new Faker();

        Map<String, Object> user = new HashMap<>();
        user.put("id", faker.number().numberBetween(1000, 9999));
        user.put("username", faker.name().username());
        user.put("firstName", faker.name().firstName());
        user.put("lastName", faker.name().lastName());
        user.put("email", faker.internet().emailAddress());
        user.put("password", faker.internet().password());
        user.put("phone", faker.phoneNumber().cellPhone());
        user.put("userStatus", 1);

        username = (String) user.get("username"); // Save for validation
        ObjectMapper mapper = new ObjectMapper();
        requestBody = mapper.writeValueAsString(user);
    }

    @Given("a user with the following data")
    public void a_user_with_the_following_data(DataTable dataTable) throws JsonProcessingException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> overrides = new HashMap<>();
        for (Map<String, String> row : rows) {
            String field = row.get("field");
            String value = row.get("value");

            if (field != null) {
                if (value == null || value.equalsIgnoreCase("[empty]")) {
                    overrides.put(field, ""); // Handle both null and "[empty]"
                } else {
                    overrides.put(field, value);
                }
            }
        }

        UserPayloadBuilder builder = new UserPayloadBuilder().fromOverrides(overrides);
        Map<String, Object> user = builder.build();

        username = builder.getUsername();

        ObjectMapper mapper = new ObjectMapper();
        requestBody = mapper.writeValueAsString(user);
    }

    @When("I send a POST request to {string}")
    public void i_send_a_post_request_to(String endpoint) {
        response = RestAssured
                .given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(endpoint);
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = RestAssured
                .given()
                .when()
                .get(endpoint);
    }

    @When("I send a GET request to {string} with header {string} = {string}")
    public void i_send_a_get_request_to_with_header(String endpoint, String headerName, String headerValue) {
        response = RestAssured
                .given()
                .header(headerName, headerValue)
                .when()
                .get(endpoint);
    }


    @When("I send a GET request to the created user")
    public void i_send_a_get_request_to_the_created_user() {
        response = RestAssured
                .given()
                .when()
                .get("/user/" + username);
    }

    @When("I send a PUT request to {string}")
    public void i_send_a_put_request_to(String endpoint) {
        response = RestAssured
                .given()
                .when()
                .put(endpoint);
    }
    @When("I send a PUT request to the created user")
    public void i_send_a_put_request_to_the_created_user() {
        response = RestAssured
                .given()
                .when()
                .put("/user/" + username);
    }


    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        response = RestAssured
                .given()
                .when()
                .delete(endpoint);
    }

    @When("I send a DELETE request to the created user")
    public void i_send_a_delete_request_to_the_created_user() {
        response = RestAssured
                .given()
                .when()
                .delete("/user/" + username);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        assertThat(response.getStatusCode(), is(statusCode));
    }

    @Then("the response message should be {string}")
    public void the_response_message_should_be(String message) {
        assertThat(response.jsonPath().getString("message"), equalTo(message));
    }

    @Then("the response should contain username {string}")
    public void the_response_should_contain_username(String username) {
        assertThat(response.jsonPath().getString("username"), equalTo(username));
    }

    @Then("the response should be {string}")
    public void the_response_should_be(String expected) {
        assertThat(response.asString(), equalTo(expected));
    }

    @Then("the response should contain error message {string}")
    public void the_response_should_contain_error_message(String expectedMessage) {
        String actual = response.jsonPath().getString("errors[0].message");
        assertThat(actual, equalTo(expectedMessage));
    }

    @Then("the response should confirm user was created")
    public void the_response_should_confirm_user_was_created() {
        String expected = "User " + username + " created successfully";
        assertThat(response.jsonPath().getString("message"), equalTo(expected));
    }

    @Then("the response should contain username")
    public void the_response_should_contain_username() {
        assertThat(response.jsonPath().getString("username"), equalTo(username));
    }
}
