package com.hiretrack.backend.service;

import java.util.List;
import java.util.Optional;

import com.hiretrack.backend.entity.Job;

public interface JobService {

    List<Job> findAll();

    Optional<Job> findById(Long id);

    Job save(Job job);

    void deleteById(Long id);
}


