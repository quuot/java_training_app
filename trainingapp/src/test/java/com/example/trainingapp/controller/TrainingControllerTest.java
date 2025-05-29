package com.example.trainingapp.controller;

import com.example.trainingapp.config.TestSecurityConfig;
import com.example.trainingapp.model.Training;
import com.example.trainingapp.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingController.class)
@Import(TestSecurityConfig.class)
public class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnAllTrainings() throws Exception {
        Mockito.when(trainingService.getAllTrainings()).thenReturn(List.of(new Training()));
        mockMvc.perform(get("/api/trainings"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnTrainingById() throws Exception {
        Mockito.when(trainingService.getTrainingById(1L)).thenReturn(new Training());
        mockMvc.perform(get("/api/trainings/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateTraining() throws Exception {
        Training training = Training.builder()
                .name("Cardio")
                .description("Bieg 5km")
                .build();

        mockMvc.perform(post("/api/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteTraining() throws Exception {
        mockMvc.perform(delete("/api/trainings/1"))
                .andExpect(status().isOk());
    }
}
