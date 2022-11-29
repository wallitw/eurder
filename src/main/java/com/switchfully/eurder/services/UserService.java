package com.switchfully.eurder.services;

import com.switchfully.eurder.api.dtos.CreateCustomerDto;
import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.repositories.UserRepository;
import com.switchfully.eurder.domain.security.Role;
import com.switchfully.eurder.services.mappers.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        Customer customer = new Customer(createCustomerDto.userName(), createCustomerDto.password(), Role.CUSTOMER, createCustomerDto.firstName(), createCustomerDto.lastName(), createCustomerDto.email(), createCustomerDto.address(), createCustomerDto.phoneNumber());
        return userMapper.toDTO(userRepository.save(customer));
    }


}
