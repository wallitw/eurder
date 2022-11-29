package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<String, User> userMap = new HashMap<>();
    public Customer save(Customer customer) {
        userMap.put(customer.getId(), customer);
        return customer;
    }
}
