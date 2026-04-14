package org.example.beam.service;

import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    public void createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());

        userRepository.save(user);
    }

    public void updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        user.setPassword(updateUserDto.getPassword());

        userRepository.save(user);
    }



}
