package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.model.Game;
import org.example.beam.model.User;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//TODO: purchase ?

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody CreateUserDto createUserDto){
        if (createUserDto.getEmail() == null
                || createUserDto.getPassword() == null
                || createUserDto.getName() == null) {
            return "Please provide all the required fields";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        createUserDto.setPassword(encoder.encode(createUserDto.getPassword()));
        userService.createUser(createUserDto);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody CreateUserDto createUserDto) {
        Optional<User> userOpt = userRepository.findByName(createUserDto.getName());

        if (userOpt.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = userOpt.get();
            if (passwordEncoder.matches(createUserDto.getPassword(), user.getPassword())) {
                return "Login successful";
            }
        }
        return "Login failed";
    }

    @PostMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto){
        if (updateUserDto.getEmail() == null
                || updateUserDto.getPassword() == null
                || updateUserDto.getName() == null) {
            return "Please provide all the required fields";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        updateUserDto.setPassword(encoder.encode(updateUserDto.getPassword()));
        userService.updateUser(id, updateUserDto);
        return "User updated successfully";
    }
    @GetMapping("/profile/{id}")
    public ShowUserDto getMyProfile(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShowUserDto showUserDto = new ShowUserDto();
        showUserDto.setName(user.getName());
        showUserDto.setEmail(user.getEmail());

        return showUserDto;
    }

}
