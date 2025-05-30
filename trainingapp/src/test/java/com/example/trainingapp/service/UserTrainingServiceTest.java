package com.example.trainingapp.service;

import com.example.trainingapp.model.Training;
import com.example.trainingapp.model.User;
import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.repository.TrainingRepository;
import com.example.trainingapp.repository.UserRepository;
import com.example.trainingapp.repository.UserTrainingRepository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserTrainingServiceTest {

    @Autowired
    private UserTrainingService userTrainingService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TrainingRepository trainingRepository;

    @MockBean
    private UserTrainingRepository userTrainingRepository;

    @Test
    void shouldReturnTrainingsForUser() {
        Long userId = 1L;
        List<UserTraining> trainings = List.of(new UserTraining());
        Mockito.when(userTrainingRepository.findByUserId(userId)).thenReturn(trainings);

        List<UserTraining> result = userTrainingService.getTrainingsForUser(userId);

        assertThat(result).hasSize(1).isEqualTo(trainings);
    }

    @Test
    void shouldAssignTrainingToUser() {
        Long userId = 1L;
        Long trainingId = 2L;

        User user = User.builder().id(userId).build();
        Training training = Training.builder().id(trainingId).build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        userTrainingService.assignTrainingToUser(userId, trainingId);

        ArgumentCaptor<UserTraining> captor = ArgumentCaptor.forClass(UserTraining.class);
        Mockito.verify(userTrainingRepository).save(captor.capture());

        UserTraining saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getTraining()).isEqualTo(training);
        assertThat(saved.isCompleted()).isFalse();
    }

    @Test
    void shouldThrowWhenUserNotFoundDuringAssignment() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTrainingService.assignTrainingToUser(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie znaleziono uzytkownika");
    }

    @Test
    void shouldThrowWhenTrainingNotFoundDuringAssignment() {
        User user = User.builder().id(1L).build();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(trainingRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTrainingService.assignTrainingToUser(1L, 2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie znaleziono treningu");
    }

    @Test
    void shouldCompleteTrainingWithComment() {
        Long userId = 1L;
        Long trainingId = 2L;

        UserTraining userTraining = new UserTraining();
        Mockito.when(userTrainingRepository.findByUserIdAndTrainingId(userId, trainingId))
                .thenReturn(Optional.of(userTraining));

        userTrainingService.completeTraining(userId, trainingId, true, "Super");

        assertThat(userTraining.isCompleted()).isTrue();
        assertThat(userTraining.getComment()).isEqualTo("Super");
        assertThat(userTraining.getCompletedAt()).isNotNull();

        Mockito.verify(userTrainingRepository).save(userTraining);
    }

    @Test
    void shouldThrowWhenCompletingNotAssignedTraining() {
        Mockito.when(userTrainingRepository.findByUserIdAndTrainingId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTrainingService.completeTraining(1L, 2L, true, ""))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("uzytkownik nie ma przypisanego treningu");
    }
}
