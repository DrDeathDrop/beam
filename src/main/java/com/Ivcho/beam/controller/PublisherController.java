package com.Ivcho.beam.controller;

import com.Ivcho.beam.dto.CreatePublisherDto;
import com.Ivcho.beam.dto.ShowPublisherDto;
import com.Ivcho.beam.dto.UpdatePublisherDto;
import org.example.beam.dto.*;
import com.Ivcho.beam.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("/add")
    public String addPublisher(@RequestBody CreatePublisherDto createPublisherDto) {
        if (createPublisherDto.name() == null
                || createPublisherDto.country() == null
                || createPublisherDto.founded() == null
                || createPublisherDto.website() == null
                || createPublisherDto.yearsOfEstablishment() == null) {
            return "Please provide all the required fields";
        }

        publisherService.createPublisher(createPublisherDto);
        return "Publisher added successfully";
    }

    @DeleteMapping("/delete/{id}")
    public void deletePublisher(@PathVariable Long id) {

        publisherService.deletePublisher(id);
    }

    @PutMapping("/update/{id}")
    public String updatePublisher(@PathVariable Long id, @RequestBody UpdatePublisherDto updatePublisherDto) {

        publisherService.updatePublisher(id, updatePublisherDto);
        return "Publisher updated successfully";
    }

    @GetMapping("/view/{id}")
    public ShowPublisherDto getPublisher(@PathVariable Long id) {

        return publisherService.getPublisher(id);
    }
}
