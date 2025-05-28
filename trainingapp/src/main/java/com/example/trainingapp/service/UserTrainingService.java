package trainingapp.service;

import trainingapp.model.Training;
import trainingapp.model.User;
import trainingapp.model.UserTraining;
import trainingapp.repository.TrainingRepository;
import trainingapp.repository.UserRepository;
import trainingapp.repository.UserTrainingRepository;
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
