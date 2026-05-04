package org.example.beam.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserDto {

    private String name;
    private String email;
    private String password;

}
