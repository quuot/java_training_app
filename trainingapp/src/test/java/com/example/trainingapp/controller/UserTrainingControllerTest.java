package com.example.trainingapp.controller;

import com.example.trainingapp.config.TestSecurityConfig;
import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.service.UserTrainingService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserTrainingController.class)
@Import(TestSecurityConfig.class)
public class UserTrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserTrainingService userTrainingService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTrainingsForUser() throws Exception {
        Mockito.when(userTrainingService.getTrainingsForUser(1L))
                .thenReturn(List.of(new UserTraining()));

        mockMvc.perform(get("/api/user-trainings/user/1"))
                .andExpect(status().isOk());
    }
}
