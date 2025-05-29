package com.example.trainingapp.controller;

import com.example.trainingapp.config.TestSecurityConfig;
import com.example.trainingapp.model.Role;
import com.example.trainingapp.model.User;
import com.example.trainingapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(new User()));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldReturnUserById() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenReturn(new User());
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateUser() throws Exception {
        User user = User.builder()
                .username("john")
                .password("john")
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotAllowUserToCreateUser() throws Exception {
        User user = User.builder()
                .username("john")
                .password("pass")
                .role(Role.USER)
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
