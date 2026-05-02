package org.example.beam.controller;

import org.example.beam.dto.*;
import org.example.beam.model.*;
import org.example.beam.repository.UserRepository;
import org.example.beam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // inject the bean

    @PostMapping("/register")
    public String registerUser(@RequestBody CreateUserDto createUserDto) {
        if (createUserDto.getEmail() == null
                || createUserDto.getPassword() == null
                || createUserDto.getName() == null) {
            return "Please provide all the required fields";
        }
        userService.createUser(createUserDto); // no encoding here anymore
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody CreateUserDto createUserDto) {
        Optional<User> userOpt = userRepository.findByName(createUserDto.getName());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(createUserDto.getPassword(), user.getPassword())) {
                return "Login successful";
            }
        }
        return "Login failed";
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        userService.updateUser(id, updateUserDto); // no encoding here anymore
        return "User updated successfully";
    }


    @GetMapping("/profile/{id}")
    public ShowUserDto getMyProfile(@PathVariable Long id) {
        return userService.getUser(id);
    }


    @GetMapping("/show/all")
    public List<ShowUserDto> getAllUsers() {
        return userService.getAllUsers();

    }

}
