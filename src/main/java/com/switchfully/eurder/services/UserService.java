package com.switchfully.eurder.services;

import com.switchfully.eurder.api.dtos.CreateCustomerDto;
import com.switchfully.eurder.api.dtos.CreateUserDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.exceptions.UnknownUserException;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.EmailValidation;
import com.switchfully.eurder.domain.security.Role;
import com.switchfully.eurder.services.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        //input validation
        String error = validateUserInput(new CreateUserDto(createCustomerDto.userName(), createCustomerDto.password()));
        error += validateCustomerInput(createCustomerDto);
        if (!error.isEmpty()) {
            logger.info("A user tried to create an account but entered invalid fields: " + error);
            throw new IllegalArgumentException("Following fields are invalid:" + error);
        }

        Customer customer = new Customer(createCustomerDto.userName(),
                createCustomerDto.password(),
                Role.CUSTOMER,
                createCustomerDto.firstName(),
                createCustomerDto.lastName(),
                createCustomerDto.email(),
                createCustomerDto.address(),
                createCustomerDto.phoneNumber());
        return userMapper.toDTO(userRepository.createCustomer(customer));
    }

    public String validateUserInput(CreateUserDto createUserDto) {
        String result = "";
        if (createUserDto.userName().isEmpty()) {
            result += " userName";
        }
        if (createUserDto.password().isEmpty()) {
            result += " password";
        }
        return result;
    }

    public String validateCustomerInput(CreateCustomerDto createCustomerDto) {
        String result = "";
        if (createCustomerDto.firstName().isEmpty()) {
            result += " firstName";
        }
        if (createCustomerDto.lastName().isEmpty()) {
            result += " lastName";
        }
        if (!EmailValidation.checkMail(createCustomerDto.email())) {
            result += " email";
        }
        if (createCustomerDto.address().streetName().isEmpty()) {
            result += " streetName";
        }
        if (createCustomerDto.address().houseNumber().isEmpty()) {
            result += " houseNumber";
        }
        if (createCustomerDto.address().postalCode().isEmpty()) {
            result += " postalCode";
        }
        if (createCustomerDto.address().cityName().isEmpty()) {
            result += " cityName";
        }
        if (createCustomerDto.phoneNumber().isEmpty()) {
            result += " phoneNumber";
        }

        return result;
    }

    //UNIQUE USERNAME AND EMAIL STILL NEED TO IMPLEMENT! (zoals bij item)
    public List<CustomerDto> getAllCustomers() {
        return userMapper.toDTO(userRepository.getAllCustomers().stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .map(user -> (Customer) user).collect(Collectors.toList()));
    }

    public CustomerDto getCustomerByUsername(String username) {
        return userMapper.toDTO((Customer) userRepository.getUserByUserName(username)
                .orElseThrow(() -> new UnknownUserException("The user: " + username + " is not found")));
    }
}
