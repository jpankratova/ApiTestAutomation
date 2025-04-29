package utils;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Map;

public class UserPayloadBuilder {
    private final Map<String, Object> user = new HashMap<>();
    private final Faker faker = new Faker();

    private String generatedUsername;

    public UserPayloadBuilder fromOverrides(Map<String, String> overrides) {
        user.put("id", faker.number().numberBetween(1000, 9999));

        String username = resolveField(overrides, "username", faker.name().username());
        generatedUsername = username;
        user.put("username", username);

        user.put("firstName", resolveField(overrides, "firstName", faker.name().firstName()));
        user.put("lastName", resolveField(overrides, "lastName", ""));
        user.put("email", resolveField(overrides, "email", faker.internet().emailAddress()));
        user.put("password", resolveField(overrides, "password", faker.internet().password()));
        user.put("phone", resolveField(overrides, "phone", faker.phoneNumber().cellPhone()));
        user.put("userStatus", 1);

        return this;
    }

    private String resolveField(Map<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        if (value == null) return defaultValue;
        if (value.equalsIgnoreCase("[empty]")) return "";
        return value;
    }

    public Map<String, Object> build() {
        return user;
    }

    public String getUsername() {
        return generatedUsername;
    }
}
