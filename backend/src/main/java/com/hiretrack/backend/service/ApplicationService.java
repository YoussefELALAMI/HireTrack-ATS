package com.hiretrack.backend.service;

import java.util.List;
import java.util.Optional;

import com.hiretrack.backend.entity.Application;

public interface ApplicationService {

    List<Application> findAll();

    Optional<Application> findById(Long id);

    Application save(Application application);

    void deleteById(Long id);
}


