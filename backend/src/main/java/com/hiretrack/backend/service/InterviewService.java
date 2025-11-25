package com.hiretrack.backend.service;

import java.util.List;
import java.util.Optional;

import com.hiretrack.backend.entity.Interview;

public interface InterviewService {

    List<Interview> findAll();

    Optional<Interview> findById(Long id);

    Interview save(Interview interview);

    void deleteById(Long id);
}


