package com.example.trainingapp.service;

import com.example.trainingapp.model.Training;
import com.example.trainingapp.repository.TrainingRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TrainingServiceTest {

    @Autowired
    private TrainingService trainingService;

    @MockBean
    private TrainingRepository trainingRepository;

    @Test
    void shouldCreateTraining() {
        Training training = Training.builder().name("Test").description("cwiczenia testowe").build();

        Mockito.when(trainingRepository.save(training)).thenReturn(training);

        Training result = trainingService.createTraining(training);

        assertThat(result).isEqualTo(training);
        Mockito.verify(trainingRepository).save(training);
    }

    @Test
    void shouldDeleteTraining() {
        Long trainingId = 1L;

        trainingService.deleteTraining(trainingId);

        Mockito.verify(trainingRepository).deleteById(trainingId);
    }

    @Test
    void shouldReturnAllTrainings() {
        List<Training> trainings = List.of(
                Training.builder().name("Trening A").build(),
                Training.builder().name("Trening B").build()
        );

        Mockito.when(trainingRepository.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.getAllTrainings();

        assertThat(result).hasSize(2).containsAll(trainings);
        Mockito.verify(trainingRepository).findAll();
    }

    @Test
    void shouldReturnTrainingById() {
        Training training = Training.builder().name("Test").build();
        Long trainingId = 1L;

        Mockito.when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        Training result = trainingService.getTrainingById(trainingId);

        assertThat(result).isEqualTo(training);
        Mockito.verify(trainingRepository).findById(trainingId);
    }

    @Test
    void shouldThrowWhenTrainingNotFound() {
        Long trainingId = 999L;

        Mockito.when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingService.getTrainingById(trainingId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie znaleziono treningu.");

        Mockito.verify(trainingRepository).findById(trainingId);
    }
}
