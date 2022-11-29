package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.repositories.UserRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    private UserRepository userRepository;

    @Test
    void createCustomer_happyPath()  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "wally");
        requestBody.put("password", "pwd");
        requestBody.put("firstName", "William");
        requestBody.put("lastName", "Multani");
        requestBody.put("email", "william.multani@gmail.com");
        requestBody.put("address", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"));
        requestBody.put("phoneNumber", "0485300304");

        CustomerDto result =
                RestAssured.given().port(port).contentType("application/json").body(requestBody)
                        .when().post("/users")
                        .then().statusCode(201).and().extract().as(CustomerDto.class);

        assertEquals("william.multani@gmail.com", result.email());
    }

    @Test
    void createCustomer_whenMissingFields_thenMessageContainsMissingFields()  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "");
        requestBody.put("password", "pwd");
        requestBody.put("firstName", "");
        requestBody.put("lastName", "Multani");
        requestBody.put("email", "william.multani@gmail.com");
        requestBody.put("address", new Address("", "9", "3000", "Leuven"));
        requestBody.put("phoneNumber", "0485300304");

        Map<String, String> response =
                RestAssured.given().port(port).contentType("application/json").body(requestBody)
                        .when().post("/users")
                        .then().statusCode(400).and().extract().as(new TypeRef<Map<String, String>>(){});

        assertEquals("Following fields are invalid: userName firstName streetName", new JSONObject(response).get("message").toString());
    }

    @Test
    void createCustomer_whenEmailWrongFormat_thenMessageContainsEmail()  {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "wally");
        requestBody.put("password", "pwd");
        requestBody.put("firstName", "William");
        requestBody.put("lastName", "Multani");
        requestBody.put("email", "william.multanigmail.com");
        requestBody.put("address", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"));
        requestBody.put("phoneNumber", "0485300304");

        Map<String, String> response =
                RestAssured.given().port(port).contentType("application/json").body(requestBody)
                        .when().post("/users")
                        .then().statusCode(400).and().extract().as(new TypeRef<Map<String, String>>(){});

        assertEquals("Following fields are invalid: email", new JSONObject(response).get("message").toString());
    }


}