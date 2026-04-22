package org.example.beam.service;

import jakarta.transaction.Transactional;
import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }

        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }

        userRepository.save(user);
    }

    @Transactional
    public List<ShowUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            ShowUserDto dto = new ShowUserDto();
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            return dto;
        }).toList();
    }




}
