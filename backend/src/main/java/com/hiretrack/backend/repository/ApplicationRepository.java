package com.hiretrack.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiretrack.backend.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}


