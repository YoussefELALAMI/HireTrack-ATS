package com.hiretrack.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hiretrack.backend.entity.Interview;
import com.hiretrack.backend.repository.InterviewRepository;
import com.hiretrack.backend.service.InterviewService;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Override
    public List<Interview> findAll() {
        return interviewRepository.findAll();
    }

    @Override
    public Optional<Interview> findById(Long id) {
        return interviewRepository.findById(id);
    }

    @Override
    public Interview save(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public void deleteById(Long id) {
        interviewRepository.deleteById(id);
    }
}


