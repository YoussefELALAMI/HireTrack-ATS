package com.hiretrack.backend.service;

import java.util.List;
import java.util.Optional;

import com.hiretrack.backend.entity.Candidate;

public interface CandidateService {

    List<Candidate> findAll();

    Optional<Candidate> findById(Long id);

    Candidate save(Candidate candidate);

    void deleteById(Long id);
}


