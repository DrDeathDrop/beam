package org.example.beam.service;


import org.example.beam.dto.CreateUserDto;
import org.example.beam.dto.ShowUserDto;
import org.example.beam.dto.UpdateUserDto;

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

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, userMapper, jwtService);
    }

    @Test
    void createUser_success() {
        CreateUserDto dto = new CreateUserDto("alice", "alice@email.com", "secret");

        when(passwordEncoder.encode("secret")).thenReturn("hashed_secret");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.createUser(dto);

        verify(passwordEncoder).encode("secret");
        verify(userRepository).save(any(User.class));
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
        User user = new User();
        user.setId(id);
        user.setName("old");
        user.setEmail("old@email.com");

        UpdateUserDto dto = new UpdateUserDto("new", "new@email.com", "newpwd");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpwd")).thenReturn("hashed_newpwd");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.updateUser(id, dto);

        assertEquals("new", user.getName());
        assertEquals("new@email.com", user.getEmail());
        verify(passwordEncoder).encode("newpwd");
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_notFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateUserDto dto = new UpdateUserDto("name", null, null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(99L, dto));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_success() {
        User u1 = new User(); u1.setId(1L); u1.setName("alice"); u1.setEmail("a@email.com");
        User u2 = new User(); u2.setId(2L); u2.setName("bob");   u2.setEmail("b@email.com");

        ShowUserDto dto1 = new ShowUserDto(1L, "alice", "a@email.com", List.of());
        ShowUserDto dto2 = new ShowUserDto(2L, "bob",   "b@email.com", List.of());

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));
        when(userMapper.toDto(u1)).thenReturn(dto1);
        when(userMapper.toDto(u2)).thenReturn(dto2);

        List<ShowUserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("alice", result.get(0).name());
        assertEquals("bob",   result.get(1).name());
        verify(userRepository).findAll();
    }
}