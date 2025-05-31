package com.example.trainingapp.controller;

import com.example.trainingapp.config.TestSecurityConfig;
import com.example.trainingapp.model.Role;
import com.example.trainingapp.model.User;
import com.example.trainingapp.model.UserTraining;
import com.example.trainingapp.service.UserService;
import com.example.trainingapp.service.UserTrainingService;
import com.example.trainingapp.strategy.TrainingFilterContext;
import com.example.trainingapp.strategy.TrainingFilterStrategy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserTrainingController.class)
@Import(TestSecurityConfig.class)
public class UserTrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserTrainingService userTrainingService;

    @MockBean
    private UserService userService;

    @MockBean
    private TrainingFilterContext trainingFilterContext;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnTrainingsForUser() throws Exception {
        Mockito.when(userTrainingService.getTrainingsForUser(1L))
                .thenReturn(List.of(new UserTraining()));

        mockMvc.perform(get("/api/user-trainings/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldAssignTrainingToUser() throws Exception {
        mockMvc.perform(post("/api/user-trainings/assign")
                        .param("userId", "1")
                        .param("trainingId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldCompleteTrainingIfAuthorized() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        Mockito.when(userService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(patch("/api/user-trainings/complete")
                        .param("userId", "1")
                        .param("trainingId", "2")
                        .param("completed", "true")
                        .param("comment", "done"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldForbidCompletingTrainingForOtherUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        Mockito.when(userService.getUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(patch("/api/user-trainings/complete")
                        .param("userId", "999")
                        .param("trainingId", "2")
                        .param("completed", "true"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldReturnFilteredTrainings() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        UserTraining training = new UserTraining();

        Mockito.when(userService.getUserByUsername("testuser")).thenReturn(user);
        Mockito.when(userTrainingService.getTrainingsForUser(1L)).thenReturn(List.of(training));

        TrainingFilterStrategy mockStrategy = Mockito.mock(TrainingFilterStrategy.class);
        Mockito.when(trainingFilterContext.getStrategy("incomplete")).thenReturn(mockStrategy);
        Mockito.when(mockStrategy.filter(anyList())).thenReturn(List.of(training));

        mockMvc.perform(get("/api/user-trainings/my-trainings")
                        .param("status", "incomplete"))
                .andExpect(status().isOk());
    }
}
