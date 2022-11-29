package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public CustomerDto toDTO(Customer customer) {
        return new CustomerDto(customer.getUserName(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getAddress(), customer.getPhoneNumber());
    }
}
