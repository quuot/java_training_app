package com.example.trainingapp.strategy;

import com.example.trainingapp.model.UserTraining;

import java.util.List;

public interface TrainingFilterStrategy {
    List<UserTraining> filter(List<UserTraining> trainings);
}
