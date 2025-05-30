package com.example.trainingapp.strategy;

import com.example.trainingapp.model.UserTraining;

import java.util.List;
import java.util.stream.Collectors;

public class IncompleteTrainingsStrategy implements TrainingFilterStrategy {
    @Override
    public List<UserTraining> filter(List<UserTraining> trainings) {
        return trainings.stream()
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
    }
}
