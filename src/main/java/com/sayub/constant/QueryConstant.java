package com.sayub.constant;

public interface QueryConstant {

    interface Users {
        String SAVE = "INSERT INTO users (first_name, last_name, phone_number, email, password, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        String UPDATE = "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, email = ?, password = ? WHERE id = ? AND is_active = 1";
        String FIND_BY_ID = "SELECT * FROM users WHERE id = ? AND is_active = 1";
        String FIND_ALL = "SELECT * FROM users WHERE is_active = 1 ORDER BY id";
        String DELETE_BY_ID = "UPDATE users SET is_active = 0 WHERE id = ? AND is_active = 1"; // Soft delete
        String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ? AND is_active = 1";
    }
}