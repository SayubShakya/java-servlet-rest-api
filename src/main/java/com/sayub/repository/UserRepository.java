package com.sayub.repository;

import com.sayub.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    void update(User user);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void deleteById(int id);
}