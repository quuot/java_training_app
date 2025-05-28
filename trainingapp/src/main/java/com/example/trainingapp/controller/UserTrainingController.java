package com.example.trainingapp.controller;

import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/user-trainings")
@RequiredArgsConstructor
public class UserTrainingController {

    private final UserTrainingService userTrainingService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserTraining> getTrainingsForUser(@PathVariable Long userId) {
        return userTrainingService.getTrainingsForUser(userId);
    }
}
