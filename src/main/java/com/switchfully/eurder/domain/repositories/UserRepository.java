package com.switchfully.eurder.domain.repositories;

import com.switchfully.eurder.domain.Customer;
import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.security.Role;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<String, User> userMap = new HashMap<>();

    public UserRepository() {
        User firstAdmin = new User("admin", "pwd", Role.ADMIN);
        this.userMap.put(firstAdmin.getId(), firstAdmin);
    }

    public Customer createCustomer(Customer customer) {
        userMap.put(customer.getId(), customer);
        return customer;
    }

    public Optional<User> getUserByUserName(String username) {
        return userMap.values().stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();
    }

    public List<User> getAllCustomers() {
        return userMap.values().stream().toList();
    }
}

