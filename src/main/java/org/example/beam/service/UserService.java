package org.example.beam.service;

import org.example.beam.dto.CreateUserDto;
import org.example.beam.dto.UpdateUserDto;
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

    public void createUser(CreateUserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userRepository.save(user);
    }

    public void updateUser(Long id, UpdateUserDto update) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(update.getName());
        user.setEmail(update.getEmail());
        user.setPassword(update.getPassword());

        userRepository.save(user);
    }



}
