package com.switchfully.eurder.api;



import com.switchfully.eurder.api.dtos.CreateCustomerDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.security.Feature;
import com.switchfully.eurder.services.SecurityService;
import com.switchfully.eurder.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "users")
public class UserController {
    private final UserService userService;
    private final SecurityService securityService;

    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer (@RequestBody CreateCustomerDto createCustomerDto) {
        return userService.createCustomer(createCustomerDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDto> getAllCustomers (@RequestHeader String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_ALL_CUSTOMERS);
        return userService.getAllCustomers();
    }

    @GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto getCustomerByUsername (@PathVariable String username, @RequestHeader String authorization) {
        securityService.validateAuthorisation(authorization, Feature.GET_CUSTOMER_BY_ID);
        return userService.getCustomerByUsername(username);
    }


}
