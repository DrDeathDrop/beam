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
        CreateUserDto dto = new CreateUserDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");

        userService.createUser(dto);

        verify(passwordEncoder).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
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
        Long id = 1L;
        User existingUser = new User();
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldpass");

        UpdateUserDto dto = new UpdateUserDto();
        dto.setName("New Name");
        dto.setEmail("new@example.com");
        dto.setPassword("newpass");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpass")).thenReturn("hashed_newpass");

        userService.updateUser(id, dto);

        assertEquals("New Name", existingUser.getName());
        assertEquals("new@example.com", existingUser.getEmail());
        assertEquals("hashed_newpass", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
        verify(passwordEncoder).encode("newpass");
    }

    @Test
    void updateUser_notFound_throwsException() {
        Long id = 99L;
        UpdateUserDto dto = new UpdateUserDto();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(id, dto));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_success() {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@example.com");

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        ShowUserDto dto1 = new ShowUserDto();
        dto1.setName("Alice");
        dto1.setEmail("alice@example.com");

        ShowUserDto dto2 = new ShowUserDto();
        dto2.setName("Bob");
        dto2.setEmail("bob@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(dto1); // mock the mapper
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<ShowUserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("alice@example.com", result.get(0).getEmail());
        assertEquals("Bob", result.get(1).getName());
        assertEquals("bob@example.com", result.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }
}