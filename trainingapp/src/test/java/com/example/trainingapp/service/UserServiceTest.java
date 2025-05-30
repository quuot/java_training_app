package com.example.trainingapp.service;

import com.example.trainingapp.model.Role;
import com.example.trainingapp.model.User;
import com.example.trainingapp.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldCreateUser() {
        User user = User.builder()
                .username("jan")
                .password("pass")
                .role(Role.USER)
                .build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result).isEqualTo(user);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(
                User.builder().username("user1").build(),
                User.builder().username("user2").build()
        );

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2).containsAll(users);
    }

    @Test
    void shouldReturnUserById() {
        User user = User.builder().username("admin").build();
        Long id = 1L;

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenUserByIdNotFound() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Użytkownik nie został znaleziony");
    }

    @Test
    void shouldReturnUserByUsername() {
        User user = User.builder().username("admin").build();

        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("admin");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenUserByUsernameNotFound() {
        Mockito.when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByUsername("ghost"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Użytkownik nie został znaleziony");
    }

    @Test
    void shouldLoadUserByUsernameWithAuthorities() {
        User user = User.builder()
                .username("john")
                .password("123")
                .role(Role.USER)
                .build();

        Mockito.when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("john");

        assertThat(userDetails.getUsername()).isEqualTo("john");
        assertThat(userDetails.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }

    @Test
    void shouldThrowWhenLoadUserByUsernameFails() {
        Mockito.when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("ghost"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Użytkownik nieznany");
    }
}
