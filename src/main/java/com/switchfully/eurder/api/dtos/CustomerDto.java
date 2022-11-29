package com.switchfully.eurder.api.dtos;

import com.switchfully.eurder.domain.Address;

public record CustomerDto(String userName, String firstName, String lastName, String email, Address address, String phoneNumber) {
}
