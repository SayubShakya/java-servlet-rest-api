package com.sayub.repository.impl;

import com.sayub.db.DatabaseConnector;
import com.sayub.constant.QueryConstant.Users;
import com.sayub.entity.User;
import com.sayub.repository.UserRepository;
import com.sayub.db.query.RowMapper;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final RowMapper<User> USER_ROW_MAPPER = rs -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    };

    @Override
    public void save(User user) {
        DatabaseConnector.update(Users.SAVE,
                user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                user.getEmail(), user.getPassword(), user.isActive());
    }

    @Override
    public void update(User user) {
        DatabaseConnector.update(Users.UPDATE,
                user.getFirstName(), user.getLastName(), user.getPhoneNumber(),
                user.getEmail(), user.getPassword(), user.getId());
    }

    @Override
    public Optional<User> findById(int id) {
        return DatabaseConnector.queryOne(Users.FIND_BY_ID, USER_ROW_MAPPER, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return DatabaseConnector.queryOne(Users.FIND_BY_EMAIL, USER_ROW_MAPPER, email);
    }

    @Override
    public List<User> findAll() {
        return DatabaseConnector.queryList(Users.FIND_ALL, USER_ROW_MAPPER);
    }

    @Override
    public void deleteById(int id) {
        DatabaseConnector.update(Users.DELETE_BY_ID, id);
    }
}