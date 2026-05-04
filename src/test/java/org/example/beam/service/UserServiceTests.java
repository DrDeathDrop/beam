package org.example.beam.service;


import org.example.beam.dto.*;
import org.example.beam.mapper.UserMapper;
import org.example.beam.model.User;
import org.example.beam.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    private UserService userService; // class field, not local variable

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, userMapper);
    }

    @Test
    void createUser_success() {

    }

    @Test
    void deleteUser_success() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        Long id = 99L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(id));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void updateUser_success() {

    }

    @Test
    void updateUser_notFound_throwsException() {

    }

    @Test
    void getAllUsers_success() {

    }
}