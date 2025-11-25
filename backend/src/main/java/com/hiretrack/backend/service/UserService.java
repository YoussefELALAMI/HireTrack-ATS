package com.hiretrack.backend.service;

import java.util.List;
import java.util.Optional;

import com.hiretrack.backend.entity.User;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);
}


