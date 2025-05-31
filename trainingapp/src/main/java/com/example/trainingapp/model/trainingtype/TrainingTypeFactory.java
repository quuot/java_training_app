package com.example.trainingapp.model.trainingtype;

public class TrainingTypeFactory {
    public static TrainingType getTrainingType(String type) {
        return switch (type.toUpperCase()) {
            case "CARDIO" -> new CardioTraining();
            case "STRENGTH" -> new StrengthTraining();
            case "OTHER" -> new OtherTraining();
            default -> () -> "typ treningu nieznany";
        };
    }
}
