package org.example.beam.controller;

import org.example.beam.dto.CreateUserDto;
import org.example.beam.dto.ShowUserDto;
import org.example.beam.dto.UpdateUserDto;

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
        if (createUserDto.email() == null
                || createUserDto.password() == null
                || createUserDto.name() == null) {
            return "Please provide all the required fields";
        }
        userService.createUser(createUserDto); // no encoding here anymore
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody CreateUserDto createUserDto) {
        return userService.login(createUserDto);
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
