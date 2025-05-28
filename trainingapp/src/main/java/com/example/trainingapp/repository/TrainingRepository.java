package com.example.trainingapp.repository;

import com.example.trainingapp.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
