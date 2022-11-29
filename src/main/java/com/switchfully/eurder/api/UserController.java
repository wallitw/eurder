package com.switchfully.eurder.api;



import com.switchfully.eurder.api.dtos.CreateCustomerDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "users")
public class UserController {
    private final UserService userService;
    //private final SecurityService securityService;

    public UserController(UserService userService) {
        this.userService = userService;
        //this.securityService = securityService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer (@RequestBody CreateCustomerDto createCustomerDto) {
        return userService.createCustomer(createCustomerDto);
    }
}
