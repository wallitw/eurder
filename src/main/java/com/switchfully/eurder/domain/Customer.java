package com.switchfully.eurder.domain;

import com.switchfully.eurder.domain.security.Role;

public class Customer extends User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final Address address;
    private final String phoneNumber;

    public Customer(String userName, String password, Role role, String firstName, String lastName, String email, Address address, String phoneNumber) {
        super(userName, password, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
