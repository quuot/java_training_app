package com.example.trainingapp.controller;

import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.model.User;
import com.example.trainingapp.service.UserService;
import com.example.trainingapp.service.UserTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.security.Principal;

@RestController
@RequestMapping("/api/user-trainings")
@RequiredArgsConstructor
public class UserTrainingController {

    private final UserTrainingService userTrainingService;
    private final UserService userService;

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

    @GetMapping("/my-trainings-alltime")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserTraining>> getMyTrainings(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<UserTraining> trainings = userTrainingService.getTrainingsForUser(user.getId());
        return ResponseEntity.ok(trainings);
    }

    @GetMapping("/my-trainings-todo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserTraining>> getMyIncompleteTrainings(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<UserTraining> allTrainings = userTrainingService.getTrainingsForUser(user.getId());

        //completed = false
        List<UserTraining> incompleteTrainings = allTrainings.stream()
                .filter(t -> !Boolean.TRUE.equals(t.isCompleted()))
                .toList();

        return ResponseEntity.ok(incompleteTrainings);
    }



}
