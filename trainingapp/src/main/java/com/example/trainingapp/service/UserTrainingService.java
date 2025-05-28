package com.example.trainingapp.service;

import com.example.trainingapp.model.Training;
import com.example.trainingapp.model.User;
import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.repository.TrainingRepository;
import com.example.trainingapp.repository.UserRepository;
import com.example.trainingapp.repository.UserTrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTrainingService {

    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final UserTrainingRepository userTrainingRepository;


    // Pobierz przypisane treningi użytkownika
    public List<UserTraining> getTrainingsForUser(Long userId) {
        return userTrainingRepository.findByUserId(userId);
    }
}
