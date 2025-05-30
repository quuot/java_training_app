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

    // oznaczenie treingu jako wykonany + komentarz

    public void completeTraining(Long userId, Long trainingId, boolean completed, String comment) {
        UserTraining userTraining = userTrainingRepository.findByUserIdAndTrainingId(userId, trainingId)
                .orElseThrow(() -> new RuntimeException("uzytkownik nie ma przypisanego treningu"));

        userTraining.setCompleted(completed);
        userTraining.setComment(comment);
        userTraining.setCompletedAt(LocalDateTime.now());

        userTrainingRepository.save(userTraining);
    }

    public void assignTrainingToUser(Long userId, Long trainingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono uzytkownika"));

        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono treningu"));

        UserTraining userTraining = new UserTraining();
        userTraining.setUser(user);
        userTraining.setTraining(training);
        userTraining.setCompleted(false); // domyślnie
        userTrainingRepository.save(userTraining);
    }

}
