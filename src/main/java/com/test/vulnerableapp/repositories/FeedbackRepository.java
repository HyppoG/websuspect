package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findById(long id);
}
