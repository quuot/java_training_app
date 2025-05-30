package com.example.trainingapp.repository;

import com.example.trainingapp.model.UserTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.List;

public interface UserTrainingRepository extends JpaRepository<UserTraining, Long> {
    List<UserTraining> findByUserId(Long userId);
    List<UserTraining> findByTrainingId(Long trainingId);
    Optional<UserTraining> findByUserIdAndTrainingId(Long userId, Long trainingId);
}
