package org.example.beam.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePublisherDto {
    private Long id;
    private String name;
    private String country;
    private String yearsOfEstablishment;
    private String website;
    private String founded;

}
