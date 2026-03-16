package com.airquality.service;

import com.airquality.entity.User;

import java.util.List;

public interface UserService {

    User login(String username, String password);

    List<User> findAll();

    User findById(Long id);

    void save(User user);

    void update(User user);

    void deleteById(Long id);
}
