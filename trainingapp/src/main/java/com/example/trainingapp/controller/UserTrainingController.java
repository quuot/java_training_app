package com.example.trainingapp.controller;

import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.model.User;
import com.example.trainingapp.service.UserService;
import com.example.trainingapp.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import com.example.trainingapp.strategy.TrainingFilterContext;

import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/user-trainings")
@RequiredArgsConstructor
public class UserTrainingController {

    private final UserTrainingService userTrainingService;
    private final UserService userService;
    private final TrainingFilterContext trainingFilterContext;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserTraining> getTrainingsForUser(@PathVariable Long userId) {
        return userTrainingService.getTrainingsForUser(userId);
    }

    @PatchMapping("/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> completeTraining(
            @RequestParam Long userId,
            @RequestParam Long trainingId,
            @RequestParam boolean completed,
            @RequestParam(required = false) String comment,
            Principal principal) {

        // Pobierz nazwe zalogowanego
        User loggedUser = userService.getUserByUsername(principal.getName());

        if (!loggedUser.getId().equals(userId) && !loggedUser.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).build(); //zwroci 403 przy ingerencji w innego usera
        }

        userTrainingService.completeTraining(userId, trainingId, completed, comment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignTrainingToUser(@RequestParam Long userId,
                                                     @RequestParam Long trainingId) {
        userTrainingService.assignTrainingToUser(userId, trainingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-trainings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserTraining>> getMyFilteredTrainings(
            Principal principal,
            @RequestParam(defaultValue = "incomplete") String status) {

        User user = userService.getUserByUsername(principal.getName());
        List<UserTraining> allTrainings = userTrainingService.getTrainingsForUser(user.getId());

        List<UserTraining> filtered = trainingFilterContext.getStrategy(status).filter(allTrainings);
        return ResponseEntity.ok(filtered);
    }




}
