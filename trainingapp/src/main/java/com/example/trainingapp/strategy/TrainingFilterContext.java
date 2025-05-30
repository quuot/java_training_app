package com.example.trainingapp.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingFilterContext {

    private final Map<String, TrainingFilterStrategy> strategies = new HashMap<>();

    public TrainingFilterContext() {
        strategies.put("all", new AllTrainingsStrategy());
        strategies.put("completed", new CompletedTrainingsStrategy());
        strategies.put("incomplete", new IncompleteTrainingsStrategy());
    }

    public TrainingFilterStrategy getStrategy(String type) {
        return strategies.getOrDefault(type.toLowerCase(), new AllTrainingsStrategy());
    }
}
