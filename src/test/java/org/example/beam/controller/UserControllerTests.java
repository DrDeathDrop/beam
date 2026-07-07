package org.example.beam.controller;

import org.example.beam.dto.CreateUserDto;
import org.example.beam.dto.LoginUserDto;
import org.example.beam.dto.ShowUserDto;
import org.example.beam.dto.UpdateUserDto;
import org.mockito.*;

import org.example.beam.service.UserService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        CreateUserDto dto = new CreateUserDto("test", "test@email.com", "1234");
        String response = userController.registerUser(dto);
        assertEquals("User registered successfully", response);
        verify(userService, times(1)).createUser(any(CreateUserDto.class));
    }

    @Test
    void registerUser_missingField() {
        CreateUserDto dto = new CreateUserDto(null, "test@email.com", "pwd");
        String response = userController.registerUser(dto);
        assertEquals("Please provide all the required fields", response);
        verify(userService, never()).createUser(any());
    }

    @Test
    void login_success() {
        LoginUserDto dto = new LoginUserDto( null, "pwd");
        when(userService.login(dto)).thenReturn("Login successful");
        String response = userController.login(dto);
        assertEquals("Login successful", response);
    }

    @Test
    void login_fail_wrongPassword() {
        LoginUserDto dto = new LoginUserDto( null, "wrong");
        when(userService.login(dto)).thenThrow(new RuntimeException("Invalid password"));
        assertThrows(RuntimeException.class, () -> userController.login(dto));
    }

    @Test
    void login_fail_userNotFound() {
        LoginUserDto dto = new LoginUserDto( "hi", null);
        when(userService.login(dto)).thenThrow(new RuntimeException("User not found"));
        assertThrows(RuntimeException.class, () -> userController.login(dto));
    }

    @Test
    void deleteUser_callsService() {
        userController.deleteUser(1L);
        verify(userService).deleteUser(1L);
    }

    @Test
    void updateUser_success() {
        UpdateUserDto dto = new UpdateUserDto("updated", "new@email.com", "newpwd");
        String response = userController.updateUser(1L, dto);
        assertEquals("User updated successfully", response);
        verify(userService, times(1)).updateUser(eq(1L), any(UpdateUserDto.class));
    }

    @Test
    void getMyProfile_success() {
        ShowUserDto dto = new ShowUserDto(3L, "testuser", "test@beam.com", List.of());
        when(userService.getUser(3L)).thenReturn(dto);
        ShowUserDto result = userController.getMyProfile(3L);
        assertEquals("testuser", result.name());
        assertEquals("test@beam.com", result.email());
    }

    @Test
    void getMyProfile_notFound() {
        when(userService.getUser(22L)).thenThrow(new RuntimeException("User not found"));
        assertThrows(RuntimeException.class, () -> userController.getMyProfile(22L));
    }

    @Test
    void getAllUsers_callsService() {
        List<ShowUserDto> users = List.of(new ShowUserDto(1L, "user1", "user1@email.com", List.of()), new ShowUserDto(2L, "user2", "user2@email.com", List.of()));
        when(userService.getAllUsers()).thenReturn(users);
        List<ShowUserDto> result = userController.getAllUsers();
        assertEquals(2, result.size());
        verify(userService, times(1)).getAllUsers();
    }
}