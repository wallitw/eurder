package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Address;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.Role;
import com.switchfully.eurder.services.mappers.UserMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    void createCustomer_happyPath() {
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
    void createCustomer_whenEmptyFields_thenMessageContainsEmptyFields() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "");
        requestBody.put("password", "pwd");
        requestBody.put("firstName", "");
        requestBody.put("lastName", "Multani");
        requestBody.put("email", "william.multani@gmail.com");
        requestBody.put("address", new Address("", "9", "3000", "Leuven"));
        requestBody.put("phoneNumber", "0485300304");

        JSONObject response =
                RestAssured.given().port(port).contentType("application/json").body(requestBody)
                        .when().post("/users")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        assertEquals("Following fields are invalid: userName firstName streetName", new JSONObject(response).get("message").toString());
    }

    @Test
    void createCustomer_whenEmailWrongFormat_thenMessageContainsEmail() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userName", "wally");
        requestBody.put("password", "pwd");
        requestBody.put("firstName", "William");
        requestBody.put("lastName", "Multani");
        requestBody.put("email", "william.multanigmail.com");
        requestBody.put("address", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"));
        requestBody.put("phoneNumber", "0485300304");

        JSONObject response =
                RestAssured.given().port(port).contentType("application/json").body(requestBody)
                        .when().post("/users")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        assertEquals("Following fields are invalid: email", response.get("message").toString());
    }

    @Test
    void getAllCustomers_whenAdmin_happyPath() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        Customer wolf = new Customer("wolfgangster", "pwd", Role.CUSTOMER, "Wolf", "Perdieus", "wolf.perdieus@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        userRepository.createCustomer(wolf);
        List<CustomerDto> result =
                RestAssured.given().port(port).contentType("application/json").auth().preemptive().basic("admin", "pwd")
                        .when().get("/users")
                        .then().statusCode(200).and().extract().body().jsonPath().getList(".", CustomerDto.class);
        System.out.println(result);
        assertTrue(result.contains(userMapper.toDTO(wally)));
        assertTrue(result.contains(userMapper.toDTO(wolf)));

    }

    @Test
    void getAllCustomers_whenWrongPassword_thenForbiddenAndMessageUnauthorized() {
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd123").log().all().contentType(ContentType.JSON)
                        .when().get("/users")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void getCustomerByUsername_whenAdmin_happyPath() {
        Customer wally = new Customer("wally", "pwd", Role.CUSTOMER, "William", "Multani", "william.multani@gmail.com", new Address("Paul Lebrunstraat", "9", "3000", "Leuven"), "0485300304");
        userRepository.createCustomer(wally);
        CustomerDto result =
                RestAssured.given().port(port).contentType("application/json").auth().preemptive().basic("admin", "pwd")
                        .when().get("/users/wally")
                        .then().statusCode(200).and().extract().as(CustomerDto.class);
        System.out.println(result);
        assertEquals(result, userMapper.toDTO(wally));
    }

    @Test
    void getCustomerByUsername_whenWrongPassword_thenForbiddenAndMessageUnauthorized() {
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd123").log().all().contentType(ContentType.JSON)
                        .when().get("/users/wally")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("Unauthorized", response.get("message").toString());
    }

    @Test
    void getCustomerByUsername_whenUserNotExists_thenForbiddenAndMessage() {
        JSONObject response =
                RestAssured
                        .given().port(port).auth().preemptive().basic("admin", "pwd").log().all().contentType(ContentType.JSON)
                        .when().get("/users/wally")
                        .then().statusCode(403).and().extract().as(JSONObject.class);
        assertEquals("The user: wally is not found", response.get("message").toString());
    }
}