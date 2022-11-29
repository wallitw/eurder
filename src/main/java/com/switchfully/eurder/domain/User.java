package com.switchfully.eurder.domain;

import com.switchfully.eurder.domain.security.Feature;
import com.switchfully.eurder.domain.security.Role;

import java.util.UUID;

public class User {
    private final String id;
    private final String userName;
    private final String password;
    private final Role role;

    public User(String userName, String password, Role role) {
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean doesPasswordMatch(String password) {
        return this.password.equals(password);
    }

    public boolean hasAccessTo(Feature feature) {
        return this.role.hasFeature(feature);
    }
}
