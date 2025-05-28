package com.example.trainingapp.controller;

import com.example.trainingapp.model.Training;
import com.example.trainingapp.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Training createTraining(@RequestBody Training training) {
        return trainingService.createTraining(training);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteTraining(@PathVariable Long id) {
        trainingService.deleteTraining(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Training> getAllTrainings() {
        return trainingService.getAllTrainings();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Training getTrainingById(@PathVariable Long id) {
        return trainingService.getTrainingById(id);
    }
}
