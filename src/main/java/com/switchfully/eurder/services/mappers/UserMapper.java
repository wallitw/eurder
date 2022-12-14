package com.switchfully.eurder.services.mappers;

import com.switchfully.eurder.api.dtos.CustomerDto;
import com.switchfully.eurder.domain.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public CustomerDto toDTO(Customer customer) {
        return new CustomerDto(customer.getUserName(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getAddress(), customer.getPhoneNumber());
    }
    public List<CustomerDto> toDTO(List<Customer> allCustomers) {
        return allCustomers.stream()
                .map(this::toDTO)
                .toList();
    }

}
