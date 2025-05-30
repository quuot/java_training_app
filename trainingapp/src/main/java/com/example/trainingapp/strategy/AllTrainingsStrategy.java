package com.example.trainingapp.strategy;

import com.example.trainingapp.model.UserTraining;

import java.util.List;

public class AllTrainingsStrategy implements TrainingFilterStrategy {
    @Override
    public List<UserTraining> filter(List<UserTraining> trainings) {
        return trainings;
    }
}
