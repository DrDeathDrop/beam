package org.example.beam.service;

import org.example.beam.dto.CreateUserDto;
import org.example.beam.dto.LoginUserDto;
import org.example.beam.dto.ShowUserDto;
import org.example.beam.dto.UpdateUserDto;
import org.example.beam.model.User;
import jakarta.transaction.Transactional;
import org.example.beam.mapper.UserMapper;
import org.example.beam.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }


    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setName(createUserDto.name());
        user.setEmail(createUserDto.email());
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateUserDto.name() != null) {
            user.setName(updateUserDto.name());
        }
        if (updateUserDto.email() != null) {
            user.setEmail(updateUserDto.email());
        }
        if (updateUserDto.password() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        }

        userRepository.save(user);
    }

    public String login(LoginUserDto loginUserDto) {
        User user = userRepository.findByEmail(loginUserDto.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginUserDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(user.getEmail(), user.getRole().name());

    }

    @Transactional
    public List<ShowUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    public ShowUserDto getProfile(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public ShowUserDto getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDto(user);
    }

}