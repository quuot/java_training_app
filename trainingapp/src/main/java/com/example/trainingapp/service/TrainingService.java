package com.example.trainingapp.service;

import com.example.trainingapp.model.Training;
import com.example.trainingapp.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.trainingapp.model.trainingtype.TrainingTypeFactory;
import com.example.trainingapp.model.trainingtype.TrainingType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    public Training getTrainingById(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono treningu."));
    }

    public String getTrainingDescription(Long trainingId) {
        Training training = getTrainingById(trainingId);
        TrainingType type = TrainingTypeFactory.getTrainingType(training.getTrainingTypeName());
        return type.getDescription();
    }
}
