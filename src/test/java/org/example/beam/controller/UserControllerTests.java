package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.model.User;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("test");
        dto.setEmail("test@email.com");
        dto.setPassword("1234");
        String response = userController.registerUser(dto);
        assertEquals("User registered successfully", response);
        verify(userService, times(1)).createUser(any(CreateUserDto.class));
    }

    @Test
    void registerUser_missingField() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName(null);
        dto.setEmail("test@email.com");
        dto.setPassword("pwd");
        String response = userController.registerUser(dto);
        assertEquals("Please provide all the required fields", response);
        verify(userService, never()).createUser(any());
    }

    @Test
    void login_success() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("test");
        dto.setPassword("pwd");
        User user = new User();
        user.setName("test");
        user.setPassword(new BCryptPasswordEncoder().encode("pwd"));
        when(userRepository.findByName("test")).thenReturn(Optional.of(user));
        String response = userController.login(dto);
        assertEquals("Login successful", response);
    }

    @Test
    void login_fail_wrongPassword() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("test");
        dto.setPassword("wrong");
        User user = new User();
        user.setName("test");
        user.setPassword(new BCryptPasswordEncoder().encode("pwd"));
        when(userRepository.findByName("test")).thenReturn(Optional.of(user));
        String response = userController.login(dto);
        assertEquals("Login failed", response);
    }

    @Test
    void login_fail_userNotFound() {
        CreateUserDto dto = new CreateUserDto();
        dto.setName("nope");
        dto.setPassword("anything");
        when(userRepository.findByName("nope")).thenReturn(Optional.empty());
        String response = userController.login(dto);
        assertEquals("Login failed", response);
    }

    @Test
    void deleteUser_callsService() {
        long id = 1L;
        userController.deleteUser(id);
        verify(userService).deleteUser(id);
    }

    @Test
    void updateUser_success() {
        long id = 1L;
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("updated");
        dto.setEmail("new@email.com");
        dto.setPassword("newpwd");
        String response = userController.updateUser(id, dto);
        assertEquals("User updated successfully", response);
        verify(userService, times(1)).updateUser(eq(id), any(UpdateUserDto.class));
    }

    @Test
    void updateUser_missingField() {
        long id = 2L;
        UpdateUserDto dto = new UpdateUserDto();
        dto.setName(null);
        dto.setEmail("z@y.com");
        dto.setPassword("pass");
        String response = userController.updateUser(id, dto);
        assertEquals("Please provide all the required fields", response);
        verify(userService, never()).updateUser(anyLong(), any());
    }

    @Test
    void getMyProfile_success() {
        long id = 3L;
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@beam.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        ShowUserDto dto = userController.getMyProfile(id);
        assertEquals("testuser", dto.getName());
        assertEquals("test@beam.com", dto.getEmail());
    }

    @Test
    void getMyProfile_notFound() {
        long id = 22L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userController.getMyProfile(id));
    }

    @Test
    void getAllUsers_callsService() {
        List<ShowUserDto> users = Arrays.asList(new ShowUserDto(), new ShowUserDto());
        when(userService.getAllUsers()).thenReturn(users);
        List<ShowUserDto> result = userController.getAllUsers();
        assertEquals(2, result.size());
        verify(userService, times(1)).getAllUsers();
    }
}