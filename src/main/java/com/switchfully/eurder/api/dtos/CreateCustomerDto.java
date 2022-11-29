package com.switchfully.eurder.api.dtos;

import com.switchfully.eurder.domain.Address;

public record CreateCustomerDto(String userName, String password, String firstName, String lastName, String email, Address address, String phoneNumber) {
}
